package tourGuide.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import tourGuide.dto.NearByAttractionDto;
import tourGuide.dto.PotentialAttraction;
import tourGuide.exception.ValidationException;
import tourGuide.feign.GpsApi;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDto.User;
import tourGuide.feign.dto.UserDto.UserPreferences;
import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.feign.dto.gpsDto.Location;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tourGuide.feign.dto.mapper.ProviderMapper;
import tourGuide.helper.DateTimeHelper;
import tourGuide.helper.InternalTestHelper;
import tourGuide.tracker.Tracker;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Lazy
public class TourGuideService {
    private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
    private final RewardsService rewardsService;
    private final TripPricer tripPricer = new TripPricer();
    public final Tracker tracker;
    @Value("${test.mode}")
    private String testModeAsString;
    boolean testMode = Boolean.parseBoolean("true");
    private final DateTimeHelper dateTimeHelper = new DateTimeHelper();
    GpsApi gpsApi;
    UserApi userApi;

    ProviderMapper providerMapper = new ProviderMapper();
    ExecutorService trackUserExecutorService = Executors.newFixedThreadPool(100);
    ExecutorService getRewardExecutorService = Executors.newFixedThreadPool(600);

    public ExecutorService getTrackUserExecutorService() {
        return trackUserExecutorService;
    }

    public ExecutorService getGetRewardExecutorService() {
        return getRewardExecutorService;
    }

    @Autowired
    public TourGuideService(RewardsService rewardsService, GpsApi gpsApi, UserApi userApi) {

        this.gpsApi = gpsApi;
        this.userApi = userApi;
        this.rewardsService = rewardsService;

        if (testMode) {
            logger.info("TestMode enabled");
            logger.debug("Initializing users");
            userApi.initUser(dateTimeHelper.getTimeStamp(), InternalTestHelper.getInternalUserNumber());
            //initializeInternalUsers();
            logger.debug("Finished initializing users");
        }
        tracker = new Tracker(this, userApi);
        addShutDownHook();
    }

    /**
     * Get last user visited location
     *
     * @param user user to get last visited location
     * @return last visited location
     */
    public VisitedLocation getUserLocation(User user) {
        VisitedLocation visitedLocation = null;
        visitedLocation = (user.getVisitedLocations().size() > 0) ?
                getLastVisitedLocation(user.getVisitedLocations()) :
                trackUserLocation(user);
        return visitedLocation;
    }

    private VisitedLocation getLastVisitedLocation(List<VisitedLocation> visitedLocations) {
        return visitedLocations.get(visitedLocations.size() - 1);
    }

    /**
     * Get trip deal list and for user and set to this user by username
     *
     * @param user user to get trip deal
     * @return list of trip deals
     */
    public List<Provider> getTripDeals(User user) {
        int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
        List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
        userApi.setTripDeals(user.getUserName()
                , dateTimeHelper.getTimeStamp()
                , providerMapper.providerListToProviderDtoList(providers));
        return providers;
    }

    /**
     * Track user location and calculate reward point
     *
     * @param user user to track
     * @return user visited location
     */
    public VisitedLocation trackUserLocation(User user) {

        VisitedLocation visitedLocation = gpsApi.getUserLocation(user.getUserId().toString(), dateTimeHelper.getTimeStamp());
        userApi.addToVisitedLocations(dateTimeHelper.getTimeStamp(), user.getUserName(), visitedLocation.getTimeVisited().toString(), visitedLocation);
        getRewardExecutorService.submit(() -> {
            rewardsService.calculateRewards(user);
        });
        trackedUserMap.putIfAbsent(user.getUserName(), true);
        return visitedLocation;
    }

    /**
     * Track all users
     *
     * @param userList all user
     * @return completableFuture when done tracking is done
     */
    public CompletableFuture trackAllUserLocation(List<User> userList) {
        CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> null);
        for (User user : userList) {
            completableFuture = completableFuture.thenCombine(CompletableFuture.supplyAsync(
                    () -> trackUserLocation(user), trackUserExecutorService), (x, y) -> null);
        }
        return completableFuture;
    }

    /**
     * Get top five nearby attraction for user by username
     *
     * @param userName username
     * @return list of nearby attraction
     */
    public NearByAttractionDto getTopFiveNearByAttractions(String userName) {
        User user = userApi.getUserByUserName(userName, new Date().toString());
        if (user == null) throw new ValidationException("user with username =[" + userName + "] not found");
        VisitedLocation visitedLocation = getUserLocation(user);
        Map<Double, Attraction> attractionTreeMap = new TreeMap<>();
        List<PotentialAttraction> potentialAttractions = new ArrayList<>();
        getAttractionTreeMap(visitedLocation, attractionTreeMap);
        int counter = 0;
        for (Map.Entry<Double, Attraction> entry : attractionTreeMap.entrySet()) {

            Attraction attraction = entry.getValue();
            PotentialAttraction potentialAttraction = new PotentialAttraction(attraction.getAttractionName(),
                    attraction.getLatitude(), attraction.getLongitude(),
                    entry.getKey(), rewardsService.calculateRewardsForPotentialAttraction(attraction, user));
            potentialAttractions.add(potentialAttraction);
            counter++;
            if (counter >= 5) {
                break;
            }
        }

        return new NearByAttractionDto(visitedLocation.getLocation().longitude,
                visitedLocation.getLocation().latitude,
                potentialAttractions);
    }

    /**
     * Get nearby attraction Map
     */
    private void getAttractionTreeMap(VisitedLocation visitedLocation, Map<Double, Attraction> attractionTreeMap) {
        for (Attraction attraction : gpsApi.getAllAttractions(dateTimeHelper.getTimeStamp())) {
            attractionTreeMap.putIfAbsent(rewardsService.getDistance(attraction, visitedLocation.location), attraction);
        }
    }

    private void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                tracker.stopTracking();
            }
        });
    }

    /**
     * Get current location for all users
     *
     * @return Map contains all user last location
     */
    public Map<UUID, Location> getCurrentLocationsMap() {
        List<User> allUser = userApi.getAllUsers(dateTimeHelper.getTimeStamp());
        Map<UUID, Location> userLocationMap = new HashMap<>();
        allUser.parallelStream().map(user -> userLocationMap.putIfAbsent(user.getUserId(), getUserLocation(user).getLocation()));
        allUser.forEach(user -> {
            userLocationMap.putIfAbsent(user.getUserId(), getUserLocation(user).getLocation());
        });
        return userLocationMap;
    }

    /**
     * Update user preferences for user by username
     *
     * @param userName        username
     * @param userPreferences new user preferences
     * @return user after update preferences
     */
    public User addUserPreferences(String userName, UserPreferences userPreferences) {
        User user = userApi.getUserByUserName(userName, dateTimeHelper.getTimeStamp());
        if (user == null) throw new ValidationException("user with username =[" + userName + "] not found");
        user.setUserPreferences(userPreferences);
        user = userApi.addUser(dateTimeHelper.getTimeStamp(), user);
        return user;
    }

    /**********************************************************************************
     *
     * Methods Below: For Internal Testing
     *
     **********************************************************************************/
    private static final String tripPricerApiKey = "test-server-api-key";

    private Map<String, Boolean> trackedUserMap = new ConcurrentHashMap<>();

    public Map<String, Boolean> getTrackedUserMap() {
        return trackedUserMap;
    }

    public Map<String, Boolean> getCalculatedRewardForUserMap() {
        return rewardsService.getCalculatedRewardForUserMap();
    }

    public void calculateRewardForPerfTest(List<User> userList) {
        for (User user : userList) {
            getRewardExecutorService.submit(() -> {
                rewardsService.calculateRewards(user);
            });
        }
    }

}

package tourGuide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.feign.GpsApi;
import tourGuide.feign.RewardApi;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.AttractionVisitedLocationPair;
import tourGuide.feign.dto.UserDte.User;
import tourGuide.feign.dto.UserDte.UserReward;
import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.feign.dto.gpsDto.Location;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tourGuide.helper.DateTimeHelper;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    // proximity in miles
    private int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;

    GpsApi gpsApi;
    RewardApi rewardApi;
    UserApi userApi;
    ExecutorService executorService = Executors.newFixedThreadPool(100);
    // ExecutorService apiExecutorService = Executors.newFixedThreadPool(10);
    private final DateTimeHelper dateTimeHelper = new DateTimeHelper();


    @Autowired
    public RewardsService(GpsApi gpsApi, RewardApi rewardApi, UserApi userApi) {
        this.gpsApi = gpsApi;
        this.rewardApi = rewardApi;
        this.userApi = userApi;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    private List<AttractionVisitedLocationPair> getAttVlPairList(List<Attraction> attractionList, User user) {

        Map<String, AttractionVisitedLocationPair> attractionVisitedLocationPairs = new HashMap<>();
        List<Attraction> attractions = attractionList.parallelStream().
                filter(attraction -> userApi.getUserRewardsById(user.getUserName(), dateTimeHelper.getTimeStamp()).
                        stream().parallel().
                        map(UserReward::getAttraction).collect(Collectors.toList()).stream().
                        map(Attraction::getAttractionName).
                        collect(Collectors.toList()).
                        contains(attraction.attractionName)).collect(Collectors.toList());
        attractionList.removeAll(attractions);


        for (Attraction attraction : attractionList) {
            for (VisitedLocation visitedlocation : user.getVisitedLocations()) {
                if ((nearAttraction(visitedlocation, attraction))) {
                    attractionVisitedLocationPairs.putIfAbsent(attraction.getAttractionName(), new AttractionVisitedLocationPair(attraction, visitedlocation));
                }
            }
        }
        System.out.print(attractionVisitedLocationPairs.size());
        return new ArrayList<>(attractionVisitedLocationPairs.values());
    }

    private List<UserReward> getUserRewardList(List<AttractionVisitedLocationPair> attVlPairList, User user) {
        List<UserReward> userRewards = new ArrayList<>();
        for (AttractionVisitedLocationPair attVlPair : attVlPairList) {
            userRewards.add(new UserReward(attVlPair.getVisitedLocation(), attVlPair.getAttraction(),
                    rewardApi.getRewardPoints(dateTimeHelper.getTimeStamp(), user.getUserId().toString(), attVlPair.getAttraction().getAttractionId().toString())));
        }
        return userRewards;
    }

    public CompletableFuture<List<UserReward>> calculateRewards(User user) {

        CompletableFuture<List<Attraction>> attractionListCF = CompletableFuture.supplyAsync(() -> gpsApi.getAllAttraction(dateTimeHelper.getTimeStamp()), executorService);

        CompletableFuture<List<AttractionVisitedLocationPair>> attVlPairCF = attractionListCF.thenApply((attractionList) -> getAttVlPairList(attractionList, user));

        CompletableFuture<List<UserReward>> userRewardListCF = attVlPairCF.thenApply((attVlPairList) -> getUserRewardList(attVlPairList, user));

        CompletableFuture<List<UserReward>> addListOfUserRewardsCF = userRewardListCF.thenApply(userRewards -> {
            if (userRewards.size() > 0)
                return userApi.addUserRewardList(dateTimeHelper.getTimeStamp(), user.getUserName(), userRewards);
            return userRewards;
        });

        return addListOfUserRewardsCF;
    }

    public CompletableFuture<List<UserReward>> calculateRewardsForListOfUser(List<User> users) {
        CompletableFuture<List<UserReward>> completableFuture = CompletableFuture.supplyAsync(() -> null);
        for (User user : users) {
            completableFuture = completableFuture.thenCombine(CompletableFuture.supplyAsync(() -> calculateRewards(user)
                    , executorService), (x, y) -> null);
        }

        return completableFuture;
    }

    private UserReward getUserReward(User user, VisitedLocation visitedLocation, Attraction attraction) {
        if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
            if (nearAttraction(visitedLocation, attraction)) {
                UserReward userReward = (new UserReward(visitedLocation, attraction,
                        rewardApi.getRewardPoints(dateTimeHelper.getTimeStamp(), user.getUserId().toString(), attraction.getAttractionId().toString())));
                return userReward;
            }
        }
        return null;
    }

    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return getDistance(attraction, location) > attractionProximityRange ? false : true;
    }

    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
    }

    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }

}

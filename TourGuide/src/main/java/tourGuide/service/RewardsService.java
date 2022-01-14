package tourGuide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.feign.GpsApi;
import tourGuide.feign.RewordApi;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.AttractionVisitedLocationPair;
import tourGuide.feign.dto.UserDte.User;
import tourGuide.feign.dto.UserDte.UserReward;
import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.feign.dto.gpsDto.Location;
import tourGuide.feign.dto.gpsDto.VisitedLocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    // proximity in miles
    private int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;
    // private final GpsUtil gpsUtil;
    //private final RewardCentral rewardsCentral;
    GpsApi gpsApi;
    RewordApi rewordApi;
    UserApi userApi;
    ExecutorService executorService = Executors.newFixedThreadPool(50);
    // ExecutorService apiExecutorService = Executors.newFixedThreadPool(10);


    @Autowired
    public RewardsService(GpsApi gpsApi, RewordApi rewordApi, UserApi userApi) {
        this.gpsApi = gpsApi;
        this.rewordApi = rewordApi;
        this.userApi = userApi;
        // this.gpsUtil = gpsUtil;
        //this.rewardsCentral = rewardCentral;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    /*    public CompletableFuture calculateRewardsForAllUser(List<User> userList) {
            CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> null);
            for (User user : userList) {
                completableFuture = completableFuture.thenCombine(CompletableFuture.supplyAsync(() -> calculateRewards(user), cpuBound),
                        (x, y) -> null);
            }
            return completableFuture;
        }*/
    private List<AttractionVisitedLocationPair> getAttVlPairList(List<Attraction> attractionList, User user) {
        List<AttractionVisitedLocationPair> attractionVisitedLocationPairs = new ArrayList<>();
        List<Attraction> attractions = attractionList.parallelStream().
                filter(attraction -> user.
                        getUserRewards().
                        stream().parallel().
                        map(UserReward::getAttraction).
                        map(Attraction::getAttractionName).
                        collect(Collectors.toList()).
                        contains(attraction.attractionName)).collect(Collectors.toList());
        attractionList.removeAll(attractions);


        for (Attraction attraction : attractionList) {
            for (VisitedLocation visitedlocation : user.getVisitedLocations()) {
                if (nearAttraction(visitedlocation, attraction)) {
                    attractionVisitedLocationPairs.add(new AttractionVisitedLocationPair(attraction, visitedlocation));
                }
            }
        }
        return attractionVisitedLocationPairs;
    }

    private List<UserReward> getUserRewordList(List<AttractionVisitedLocationPair> attVlPairList, User user) {
        List<UserReward> userRewards = new ArrayList<>();
        for (AttractionVisitedLocationPair attVlPair : attVlPairList) {
            userRewards.add(new UserReward(attVlPair.getVisitedLocation(), attVlPair.getAttraction(),
                    rewordApi.getRewardPoints(new Date().toString(), user.getUserId().toString(), attVlPair.getAttraction().getAttractionId().toString())));
        }
        return userRewards;
    }

    public CompletableFuture<List<UserReward>> calculateRewards(User user) {

        CompletableFuture<List<Attraction>> attractionListCF = CompletableFuture.supplyAsync(() -> gpsApi.getAllAttraction(new Date().toString()), executorService);

        CompletableFuture<List<AttractionVisitedLocationPair>> attVlPairCF = attractionListCF.thenApply((attractionList) -> getAttVlPairList(attractionList, user));

        CompletableFuture<List<UserReward>> userRewordListCF = attVlPairCF.thenApply((attVlPairList) -> getUserRewordList(attVlPairList, user));

        CompletableFuture<List<UserReward>> addListOfUserRewardsCF = userRewordListCF.thenApply(userRewards -> {
            if (userRewards.size() > 0)
                return userApi.addUserRewardList(new Date().toString(), user.getUserName(), userRewards);
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

    private UserReward getUserReword(User user, VisitedLocation visitedLocation, Attraction attraction) {
        if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
            if (nearAttraction(visitedLocation, attraction)) {
                UserReward userReward = (new UserReward(visitedLocation, attraction,
                        rewordApi.getRewardPoints(new Date().toString(), user.getUserId().toString(), attraction.getAttractionId().toString())));
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

/*    private int getRewardPoints(Attraction attraction, User user) {
        return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
    }*/

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

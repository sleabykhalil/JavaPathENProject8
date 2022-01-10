package tourGuide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.feign.GpsApi;
import tourGuide.feign.RewordApi;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDte.User;
import tourGuide.feign.dto.UserDte.UserReward;
import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.feign.dto.gpsDto.Location;
import tourGuide.feign.dto.gpsDto.VisitedLocation;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    ExecutorService executorService = Executors.newCachedThreadPool();

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

    public CompletableFuture calculateRewards(User user) {
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = gpsApi.getAllAttraction();
        //Set<UserReward> userRewardSet = new HashSet<>();
        CompletableFuture completableFuture = new CompletableFuture<>();
        for (VisitedLocation visitedLocation : userLocations) {
            for (Attraction attraction : attractions) {
                completableFuture = CompletableFuture.supplyAsync(() -> getUserReword(user, visitedLocation, attraction),
                                executorService)
                        .thenAccept((userReward) -> {
                            if (userReward != null) {
                                userApi.addUserReward(user.getUserName(), userReward);
                            }
                        });
/*                completableFuture = CompletableFuture.supplyAsync(() -> {
                            UserReward userReward = getUserReword(user, visitedLocation, attraction);
                            if (userReward != null) {
                                userApi.addUserReward(user.getUserName(), userReward);
                            }
                            return null;
                        },
                        executorService);*/
                //userRewardSet.add(getUserReword(user, visitedLocation, attraction));
            }
        }

        //addUserRewordPoints(user, userRewardSet);
        return completableFuture;
    }

    public CompletableFuture calculateRewardsForListOfUser(List<User> userList) {
        CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> null);
        for (User user : userList) {
            completableFuture = completableFuture.thenCombine(calculateRewards(user), (x, y) -> null);
        }
/*        try {
            completableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        return completableFuture;
    }

    private UserReward getUserReword(User user, VisitedLocation visitedLocation, Attraction attraction) {
        if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
            if (nearAttraction(visitedLocation, attraction)) {
                UserReward userReward = (new UserReward(visitedLocation, attraction,
                        rewordApi.getRewardPoints(user.getUserId().toString(), attraction.getAttractionId().toString())));
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

package tourGuide.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    // proximity in miles
    private int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;
    private final GpsUtil gpsUtil;
    private final RewardCentral rewardsCentral;

    public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
        this.gpsUtil = gpsUtil;
        this.rewardsCentral = rewardCentral;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    public void calculateRewards(User user) {
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = gpsUtil.getAttractions();

        Set<UserReward> rewardHashSet = new HashSet<>();

        //	for(VisitedLocation visitedLocation : userLocations) {
        //	for(Attraction attraction : attractions) {

/*        userLocations.parallelStream().forEach(visitedLocation -> {
            attractions.parallelStream().forEach(attraction -> {
                if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
                    if (nearAttraction(visitedLocation, attraction)) {
                        //user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
                        rewardHashSet.add(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
                    }
                }
                //user.getUserRewards().addAll(rewardHashSet);
                rewardHashSet.forEach(user::addUserReward);
                rewardHashSet.clear();
            });
        });*/
        List<UserReward> userRewards = user.getUserRewards();
        CompletableFuture<UserReward> completableFuture = CompletableFuture.supplyAsync(() -> null);
        for (VisitedLocation visitedLocation : userLocations) {
            for (Attraction attraction : attractions) {
                //for (UserReward userReward : user.getUserRewards()) {

                if (userRewards.stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {

                    completableFuture = completableFuture.thenCombineAsync(
                            CompletableFuture.supplyAsync(() -> {
                                //System.out.println("inside completableFuture");
                                if (nearAttraction(visitedLocation, attraction)) {
                                    //if (!attraction.attractionName.equals(userReward.attraction.attractionName)) {
                                    // System.out.println("return user reward");
                                    return (new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
                                    // }
                                }
                                //System.out.println("return null 1");
                                return null;
                            }), (x, y) -> {
                                if (x != null) {
                                    rewardHashSet.add(x);
                                    // System.out.println(x.attraction.attractionName);
                                }
                                if (y != null) {
                                    rewardHashSet.add(y);
                                    // System.out.println(y.attraction.attractionName);
                                }
                                //System.out.println("return null 2");
                                return null;
                            }
                    );
                }
                //}
            }
        }
        try {
            completableFuture.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rewardHashSet.forEach(user::addUserReward);
        // rewardHashSet.clear();
    }

    private void addReword(VisitedLocation visitedLocation, Attraction attraction, UserReward userReward) {

    }


    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return getDistance(attraction, location) > attractionProximityRange ? false : true;
    }

    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
    }

    private int getRewardPoints(Attraction attraction, User user) {
        return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
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

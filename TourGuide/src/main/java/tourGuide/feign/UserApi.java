package tourGuide.feign;

import gpsUtil.location.VisitedLocation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tripPricer.Provider;

import java.util.List;

@FeignClient(value = "userApi", url = "http://localhost:8081/")
public interface UserApi {

    @GetMapping("/users")
    List<User> getAllUsers();

    @GetMapping("/user/initForTest")
    void initUser(int internalUserNumber);

    @GetMapping("/user/rewords")
    List<UserReward> getUserRewords(String userId, User user);

    @GetMapping("/user/visitedLocations")
    List<VisitedLocation> getVisitedLocations(User user);

    @PostMapping("/user/tripDeals")
    void setTripDeals(User user, List<Provider> providers);

    @PostMapping("/user/visitedLocation")
    void addToVisitedLocations(User user, VisitedLocation visitedLocation);

    @PostMapping("/user/rewords")
    void addUserReward(User user, UserReward userReward);

}

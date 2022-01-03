package tourGuide.feign;

import gpsUtil.location.VisitedLocation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tripPricer.Provider;

import java.util.List;

@FeignClient(name = "user-Api", url = "http://localhost:8081")
public interface UserApi {

    @GetMapping("/users")
    List<User> getAllUsers();

    @GetMapping("/user/initForTest")
    void initUser(int internalUserNumber);

    @GetMapping("/user/rewords")
    List<UserReward> getUserRewords(@RequestBody User user);

    @GetMapping("/user/rewords/{userName}")
    List<UserReward> getUserRewordsById(@PathVariable String userName);

    @GetMapping("/user/visitedLocations")
    List<VisitedLocation> getVisitedLocations(@RequestBody User user);

    @PostMapping("/user/tripDeals/{userName}")
    void setTripDeals(@PathVariable String userName, @RequestBody List<Provider> providers);

    @PostMapping("/user/visitedLocation/{userName}")
    void addToVisitedLocations(@PathVariable String userName, @RequestBody VisitedLocation visitedLocation);

    @PostMapping("/user/rewords/{userName}")
    void addUserReward(@PathVariable String userName, @RequestBody UserReward userReward);

}

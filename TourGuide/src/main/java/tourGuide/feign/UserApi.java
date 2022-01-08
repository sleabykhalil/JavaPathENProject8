package tourGuide.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tourGuide.feign.dto.UserDte.User;
import tourGuide.feign.dto.UserDte.UserReward;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tripPricer.Provider;

import java.util.List;

@FeignClient(name = "user-Api", url = "http://localhost:8081")
public interface UserApi {

    @GetMapping("/users")
    List<User> getAllUsers();

    @GetMapping("/users/{userName}")
    User getUserByUserName(@PathVariable String userName);

    @PostMapping("/user/initForTest")
    void initUser(@RequestParam int internalUserNumber);

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

    @PostMapping("/users/addUser")
    User addUser(@RequestBody User user);
}

package tourGuide.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tourGuide.feign.dto.UserDte.User;
import tourGuide.feign.dto.UserDte.UserReward;
import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tripPricer.Provider;

import java.util.List;

//@FeignClient(name = "user-Api", url = "http://localhost:8081")
@FeignClient(name = "user-Api", url = "http://user.localhost:81")
public interface UserApi {

    @GetMapping("/users/{timeStamp}")
    List<User> getAllUsers(@PathVariable String timeStamp);

    @GetMapping("/users/{userName}/{timeStamp}")
    User getUserByUserName(@PathVariable String userName, @PathVariable String timeStamp);

    @PostMapping("/users/addUser/{timeStamp}")
    User addUser(@PathVariable String timeStamp, @RequestBody User userDto);

    @PostMapping("/users/initForTest/{timeStamp}")
    void initUser(@PathVariable String timeStamp, @RequestParam int internalUserNumber);

    @PostMapping("/users/initForTest/addVisitedLocation/{timeStamp}")
    void initUserByAddVisitedLocation(@PathVariable String timeStamp, @RequestBody Attraction attractionDto) ;

    @GetMapping("/users/rewards/{timeStamp}")
    List<UserReward> getUserReward(@PathVariable String timeStamp, @RequestBody User user);

    @GetMapping("/users/rewards/{userName}/{timeStamp}")
    List<UserReward> getUserRewardsById(@PathVariable String userName, @PathVariable String timeStamp);

    @GetMapping("/users/visitedLocations/{timeStamp}")
    List<VisitedLocation> getVisitedLocations(@PathVariable String timeStamp, @RequestBody User user);

    @PostMapping("/users/tripDeals/{userName}/{timeStamp}")
    void setTripDeals(@PathVariable String userName, @PathVariable String timeStamp, @RequestBody List<Provider> providers);

    @PostMapping("/users/addVisitedLocation/{timeStamp}")
    void addToVisitedLocations(@PathVariable String timeStamp, @RequestParam String userName, @RequestParam String visitDate, @RequestBody VisitedLocation visitedLocation);

    @PostMapping("/users/addReward/{userName}/{timeStamp}")
    void addUserReward(@PathVariable String timeStamp, @PathVariable String userName, @RequestBody UserReward userReward);

    @PostMapping("/users/addRewardList/{userName}/{timeStamp}")
    List<UserReward> addUserRewardList(@PathVariable String timeStamp, @PathVariable String userName, @RequestBody List<UserReward> userRewardListDto);
}

package tourGuide.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tourGuide.feign.dto.UserDto.User;
import tourGuide.feign.dto.UserDto.UserReward;
import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tourGuide.feign.dto.tripPricerDto.ProviderDto;

import java.util.List;

@FeignClient(name = "user-Api", url = "${feign.users.url}")
public interface UserApi {

    @GetMapping("/users/{timeStamp}")
    List<User> getAllUsers(@PathVariable String timeStamp);

    @GetMapping("/users/{userName}/{timeStamp}")
    User getUserByUserName(@PathVariable String userName, @PathVariable String timeStamp);

    @PostMapping("/users/addUser/{timeStamp}")
    User addUser(@PathVariable String timeStamp, @RequestBody User userDto);

    @GetMapping("/users/rewards/{userName}/{timeStamp}")
    List<UserReward> getUserRewardsById(@PathVariable String userName, @PathVariable String timeStamp);

    @PostMapping("/users/tripDeals/{userName}/{timeStamp}")
    void setTripDeals(@PathVariable String userName, @PathVariable String timeStamp, @RequestBody List<ProviderDto> providers);

    @PostMapping("/users/addVisitedLocation/{timeStamp}")
    void addToVisitedLocations(@PathVariable String timeStamp, @RequestParam String userName, @RequestParam String visitDate, @RequestBody VisitedLocation visitedLocation);

    @PostMapping("/users/addRewardList/{userName}/{timeStamp}")
    List<UserReward> addUserRewardList(@PathVariable String timeStamp, @PathVariable String userName, @RequestBody List<UserReward> userRewardListDto);

    //*****************************//
    //***********For TEST**********//
    //*****************************//
    @PostMapping("/users/initForTest/{timeStamp}")
    void initUser(@PathVariable String timeStamp, @RequestParam int internalUserNumber);

    @PostMapping("/users/initForTest/addVisitedLocation/{timeStamp}")
    void addFirstAttractionForAllUser(@PathVariable String timeStamp, @RequestBody Attraction attractionDto);


}

package userApi.controllers;

import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tripPricer.Provider;
import userApi.dto.*;
import userApi.model.User;
import userApi.model.UserReward;
import userApi.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    //@Autowired
    Mapper mapper = new Mapper();
//    @Autowired
//    UserMapper userMapper;

    @GetMapping("/users/{timeStamp}")
    public List<User> getAllUsers(@PathVariable String timeStamp) {
        logger.info("/users/timeStamp{}",timeStamp);
        return userService.getAllUsers();
    }

    @GetMapping("/users/{userName}/{timeStamp}")
    public User getUserByUserName(@PathVariable String userName, @PathVariable String timeStamp) {
        logger.info("/users/userName{}/timeStamp{}", userName, timeStamp);
        return userService.getUser(userName);
    }

    @PostMapping("/users/addUser/{timeStamp}")
    public User addUser(@PathVariable String timeStamp, @RequestBody UserDto userDto) {
        logger.info("/users/addUser/timeStamp{} userName={}", timeStamp, userDto.getUserName());
        User user = mapper.userDtoToUser(userDto);
        return userService.addUser(user);
    }


    @PostMapping("/users/initForTest/{timeStamp}")
    void initUser(@PathVariable String timeStamp, @RequestParam int internalUserNumber) {
        logger.info("/users/initForTest/timeStamp{}", timeStamp);
        userService.initializeInternalUsers(internalUserNumber);
    }
    @PostMapping("/users/initForTest/addVisitedLocation/{timeStamp}")
    void initUserByAddVisitedLocation(@PathVariable String timeStamp, @RequestBody AttractionDto attractionDto) {
        logger.info("/users/initForTest/addVisitedLocation/timeStamp{}", timeStamp);
        userService.addVisitedLocationForTest(mapper.toAttraction(attractionDto));
    }
    @GetMapping("/users/rewards/{timeStamp}")
    public List<UserReward> getUserRewords(@PathVariable String timeStamp, @RequestBody User user) {
        logger.info("/users/rewards/timeStamp{}", timeStamp);
        if (!(user == null)) {
            return userService.getUserRewards(user);
        }
        return new ArrayList<>();
    }

    @GetMapping("/users/rewards/{userName}/{timeStamp}")
    public List<UserReward> getUserRewordsById(@PathVariable String userName, @PathVariable String timeStamp) {
        logger.info("/users/rewards/userName{}/timeStamp{}", userName, timeStamp);
        if (userName != null) {
            return userService.getUserRewards(userName);
        }
        return new ArrayList<>();
    }

    @GetMapping("/users/visitedLocations/{timeStamp}")
    public List<VisitedLocation> getVisitedLocations(@PathVariable String timeStamp, @RequestBody User user) {
        logger.info("/users/visitedLocations/timeStamp{}", timeStamp);
        return userService.getVisitedLocation(user);
    }

    @PostMapping("/users/tripDeals/{userName}/{timeStamp}")
    public void setTripDeals(@PathVariable String userName, @PathVariable String timeStamp, @RequestBody List<Provider> providers) {
        logger.info("/users/tripDeals/userName{}/timeStamp{}", userName, timeStamp);
        userService.setTripDeals(userName, providers);
    }

    @PostMapping("/users/addVisitedLocation/{timeStamp}")
    public void addToVisitedLocations(@PathVariable String timeStamp, @RequestParam String userName, @RequestParam String visitDate, @RequestBody VisitedLocationDto visitedLocationDto) {
        logger.info("/users/addVisitedLocation/userName{}/timeStamp{}", userName, timeStamp);
        VisitedLocation visitedLocation = mapper.toVisitedLocation(visitedLocationDto);
        userService.addToVisitedLocations(userName, visitedLocation);
    }

    @PostMapping("/users/addReward/{userName}/{timeStamp}")
    public void addUserReward(@PathVariable String timeStamp, @PathVariable String userName, @RequestBody UserRewardDto userRewardDto) {
        logger.info("/users/addReward/userName{}/timeStamp{}", userName, timeStamp);
        UserReward userReward = mapper.toUserReword(userRewardDto);
        userService.addUserReward(userName, userReward);
    }

    @PostMapping("/users/addRewardList/{userName}/{timeStamp}")
    public List<UserRewardDto> addUserRewardList(@PathVariable String timeStamp, @PathVariable String userName, @RequestBody List<UserRewardDto> userRewardDto) {
        logger.info("/users/addRewardList/userName{}/timeStamp{}", userName, timeStamp);
        List<UserReward> userRewards = mapper.toUserRewardList(userRewardDto);
       return mapper.toUserRewordListDto( userService.addUserRewardList(userName, userRewards));
    }
}

package userApi.controllers;

import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tripPricer.Provider;
import userApi.dto.Mapper;
import userApi.dto.UserDto;
import userApi.dto.UserRewardDto;
import userApi.dto.VisitedLocationDto;
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

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{userName}")
    public User getUserByUserName(@PathVariable String userName) {
        return userService.getUser(userName);
    }

    @PostMapping("/users/addUser")
    public User addUser(@RequestBody UserDto userDto) {
        User user = mapper.userDtoToUser(userDto);
        return userService.addUser(user);
    }


    @PostMapping("/user/initForTest")
    void initUser(@RequestParam int internalUserNumber) {
        userService.initializeInternalUsers(internalUserNumber);
    }

    @GetMapping("/user/rewords")
    public List<UserReward> getUserRewords(@RequestBody User user) {

        if (!(user == null))
            return userService.getUserRewards(user);

        return new ArrayList<>();
    }

    @GetMapping("/user/rewords/{userName}")
    public List<UserReward> getUserRewordsById(@PathVariable String userName) {
        if (!(userName == null))
            return userService.getUserRewards(userName);
        return new ArrayList<>();
    }

    @GetMapping("/user/visitedLocations")
    public List<VisitedLocation> getVisitedLocations(@RequestBody User user) {
        return userService.getVisitedLocation(user);
    }

    @PostMapping("/user/tripDeals/{userName}")
    public void setTripDeals(@PathVariable String userName, @RequestBody List<Provider> providers) {
        userService.setTripDeals(userName, providers);
    }

    @PostMapping("/user/visitedLocation/{userName}")
    public void addToVisitedLocations(@PathVariable String userName, @RequestBody VisitedLocationDto visitedLocationDto) {
        VisitedLocation visitedLocation = mapper.toVisitedLocation(visitedLocationDto);
        userService.addToVisitedLocations(userName, visitedLocation);
    }

    @PostMapping("/user/rewords/{userName}")
    public void addUserReward(@PathVariable String userName, @RequestBody UserRewardDto userRewardDto) {
        logger.info("add user reword{} to user{}", userRewardDto.toString(), userName);
        UserReward userReward = mapper.toUserReword(userRewardDto);
        userService.addUserReward(userName, userReward);
    }
}

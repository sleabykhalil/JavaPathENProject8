package userApi.controllers;

import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tripPricer.Provider;
import userApi.model.User;
import userApi.model.UserReward;
import userApi.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{userName}")
    public User getUserByUserName(@PathVariable String userName) {
        return userService.getUser(userName);
    }

    @PostMapping("/users/addUser")
    public User addUser(@RequestBody User user) {
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
    public void addToVisitedLocations(@PathVariable String userName, @RequestBody VisitedLocation visitedLocation) {
        userService.addToVisitedLocations(userName, visitedLocation);
    }

    @PostMapping("/user/rewords/{userName}")
    public void addUserReward(@PathVariable String userName, @RequestBody UserReward userReward) {
        userService.addUserReward(userName, userReward);
    }
}

package userApi.controllers;

import gpsUtil.location.VisitedLocation;
import org.springframework.web.bind.annotation.PostMapping;
import tripPricer.Provider;
import userApi.model.User;
import userApi.model.UserReward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import userApi.service.UserService;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;


    @GetMapping("/user/initForTest")
    public void initUser(int internalUserNumber) {
        userService.initializeInternalUsers(internalUserNumber);
    }

    @GetMapping("/user/rewords")
    public List<UserReward> getUserRewords(@RequestParam String userId) {
        return userService.getUserRewards(userId);
    }

    @GetMapping("/user/rewords")
    public List<UserReward> getUserRewords(@RequestParam User user) {
        return userService.getUserRewards(user);
    }

    @GetMapping("/user/visitedLocations")
    public List<VisitedLocation> getVisitedLocations(@RequestParam User user) {
        return userService.getVisitedLocation(user);
    }

    @PostMapping("/user/tripDeals")
    public void setTripDeals(@RequestParam User user, @RequestParam List<Provider> providers) {
        userService.setTripDeals(user, providers);
    }

    @PostMapping("/user/visitedLocation")
    public void addToVisitedLocations(@RequestParam User user, @RequestParam VisitedLocation visitedLocation) {
        userService.addToVisitedLocations(user, visitedLocation);
    }

    @PostMapping("/user/rewords")
    public void addUserReward(@RequestParam User user, @RequestParam UserReward userReward) {
        userService.addUserReward(user, userReward);
    }
}

package userApi.controllers;

import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tripPricer.Provider;
import userApi.dto.*;
import userApi.model.User;
import userApi.model.UserPreferences;
import userApi.model.UserReward;
import userApi.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;
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
        User user = userDtoToUser(userDto);
        return userService.addUser(user);
    }

    private User userDtoToUser(UserDto userDto) {
        User user = new User(userDto.getUserId(), userDto.getUserName(), userDto.getPhoneNumber(), userDto.getEmailAddress());
        user.setLatestLocationTimestamp(userDto.getLatestLocationTimestamp());
        user.setVisitedLocations(toVisitedLocations(userDto.getVisitedLocations()));
        user.setUserRewards(toUserReward(userDto.getUserRewards()));
        user.setUserPreferences(toUserPreferences(userDto.getUserPreferences()));
        user.setTripDeals(toTripDeals(userDto.getTripDeals()));
        return user;
    }

    private List<Provider> toTripDeals(List<ProviderDto> tripDeals) {
        List<Provider> providerList = new ArrayList<>();
        for (ProviderDto td :
                tripDeals) {
            providerList.add(new Provider(td.getTripId(), td.getName(), td.getPrice()));
        }
        return providerList;
    }

    private UserPreferences toUserPreferences(UserPreferencesDto userPreferencesDto) {
        return new UserPreferences(userPreferencesDto.getAttractionProximity(),
                userPreferencesDto.getCurrency(),
                userPreferencesDto.getLowerPricePoint(),
                userPreferencesDto.getHighPricePoint(),
                userPreferencesDto.getTripDuration(),
                userPreferencesDto.getTicketQuantity(),
                userPreferencesDto.getNumberOfAdults(),
                userPreferencesDto.getNumberOfChildren());
    }

    private List<UserReward> toUserReward(List<UserRewardDto> userRewards) {
        List<UserReward> userRewardList = new ArrayList<>();
        for (UserRewardDto ur : userRewards) {
            userRewardList.add(new UserReward(ur.getVisitedLocation(), ur.getAttraction(), ur.getRewardPoints()));
        }
        return userRewardList;
    }

    private List<VisitedLocation> toVisitedLocations(List<VisitedLocationDto> visitedLocations) {
        List<VisitedLocation> visitedLocationList = new ArrayList<>();
        for (VisitedLocationDto vl : visitedLocations) {
            visitedLocationList.add(new VisitedLocation(vl.getUserId(), vl.getLocation(), vl.getTimeVisited()));
        }
        return visitedLocationList;
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

package userApi.controllers;

import gpsUtil.location.VisitedLocation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    Mapper mapper = new Mapper();

    @Operation(summary = "Get all users")
    @GetMapping("/users/{timeStamp}")
    public List<User> getAllUsers(@Parameter(description = "TimeStamp", required = true)
                                  @PathVariable String timeStamp) {
        logger.info("/users/timeStamp{}", timeStamp);
        return userService.getAllUsers();
    }

    @Operation(summary = "Get user by user name")
    @GetMapping("/users/{userName}/{timeStamp}")
    public User getUserByUserName(@Parameter(description = "username", required = true)
                                  @PathVariable String userName,
                                  @Parameter(description = "TimeStamp", required = true)
                                  @PathVariable String timeStamp) {
        logger.info("/users/userName{}/timeStamp{}", userName, timeStamp);
        return userService.getUser(userName);
    }

    @Operation(summary = "Add user")
    @PostMapping("/users/addUser/{timeStamp}")
    public User addUser(@Parameter(description = "TimeStamp", required = true)
                        @PathVariable String timeStamp,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "New user",
                                required = true, content = @Content(schema = @Schema(implementation = UserDto.class)))
                        @RequestBody UserDto userDto) {
        logger.info("/users/addUser/timeStamp{} userName={}", timeStamp, userDto.getUserName());
        User user = mapper.userDtoToUser(userDto);
        return userService.addUser(user);
    }

    @Operation(summary = "Get list of user rewards by user id")
    @GetMapping("/users/rewards/{userName}/{timeStamp}")
    public List<UserReward> getUserRewardsById(@Parameter(description = "username", required = true)
                                               @PathVariable String userName,
                                               @Parameter(description = "TimeStamp", required = true)
                                               @PathVariable String timeStamp) {
        logger.info("/users/rewards/userName{}/timeStamp{}", userName, timeStamp);
        if (userName != null) {
            return userService.getUserRewards(userName);
        }
        return new ArrayList<>();
    }

    @Operation(summary = "Set tripDeals to user by user name")
    @PostMapping("/users/tripDeals/{userName}/{timeStamp}")
    public void setTripDeals(@Parameter(description = "username", required = true)
                             @PathVariable String userName,
                             @Parameter(description = "TimeStamp", required = true)
                             @PathVariable String timeStamp,
                             @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "List of Provider to add",
                                     required = true, content = @Content(schema = @Schema(implementation = ProviderDto.class)))
                             @RequestBody List<ProviderDto> providerDtoList) {
        logger.info("/users/tripDeals/userName{}/timeStamp{}", userName, timeStamp);
        List<Provider> providers = new ArrayList<>();
        providers = mapper.toTripDeals(providerDtoList);
        userService.setTripDeals(userName, providers);
    }

    @Operation(summary = "Add visited location to visited location list")
    @PostMapping("/users/addVisitedLocation/{timeStamp}")
    public void addToVisitedLocations(@Parameter(description = "TimeStamp", required = true)
                                      @PathVariable String timeStamp,
                                      @Parameter(description = "username", required = true)
                                      @RequestParam String userName,
                                      @Parameter(description = "visited date", required = false)
                                      @RequestParam String visitDate,
                                      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "new visited location to add",
                                              required = true, content = @Content(schema = @Schema(implementation = VisitedLocationDto.class)))
                                      @RequestBody VisitedLocationDto visitedLocationDto) {
        logger.info("/users/addVisitedLocation/userName{}/timeStamp{}", userName, timeStamp);
        VisitedLocation visitedLocation = mapper.toVisitedLocation(visitedLocationDto);
        userService.addToVisitedLocations(userName, visitedLocation);
    }

    @Operation(summary = "Add user rewards list to user reward list by user name")
    @PostMapping("/users/addRewardList/{userName}/{timeStamp}")
    public List<UserRewardDto> addUserRewardList(@Parameter(description = "TimeStamp", required = true)
                                                 @PathVariable String timeStamp,
                                                 @Parameter(description = "username", required = true)
                                                 @PathVariable String userName,
                                                 @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "List of user reward to add",
                                                         required = true, content = @Content(schema = @Schema(implementation = UserRewardDto.class)))
                                                 @RequestBody List<UserRewardDto> userRewardDto) {
        logger.info("/users/addRewardList/userName{}/timeStamp{}", userName, timeStamp);
        List<UserReward> userRewards = mapper.toUserRewardList(userRewardDto);
        return mapper.toUserRewardListDto(userService.addUserRewardList(userName, userRewards));
    }

    //*****************************//
    //***********For TEST**********//
    //*****************************//

    @Operation(summary = "Initialize List of user for test mode")
    @PostMapping("/users/initForTest/{timeStamp}")
    void initUser(@PathVariable String timeStamp, @RequestParam int internalUserNumber) {
        logger.info("pass from here/timeStamp{}", timeStamp);
        logger.info("/users/initForTest/timeStamp{}", timeStamp);
        userService.initializeInternalUsers(internalUserNumber);
    }

    @Operation(summary = "Add first attraction to all user for test mode")
    @PostMapping("/users/initForTest/addVisitedLocation/{timeStamp}")
    List<User> addFirstAttractionForAllUser(@Parameter(description = "TimeStamp", required = true)
                                            @PathVariable String timeStamp,
                                            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Attraction add to all user *for test only*",
                                                    required = true, content = @Content(schema = @Schema(implementation = AttractionDto.class)))
                                            @RequestBody AttractionDto attractionDto) {
        logger.info("/users/initForTest/addVisitedLocation/timeStamp{}", timeStamp);
        return userService.addVisitedLocationForTest(mapper.toAttraction(attractionDto));
    }
}
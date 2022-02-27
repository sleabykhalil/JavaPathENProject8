package tourGuide.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.dto.NearByAttractionDto;
import tourGuide.exception.ValidationException;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDto.User;
import tourGuide.feign.dto.UserDto.UserPreferences;
import tourGuide.feign.dto.UserDto.UserReward;
import tourGuide.feign.dto.gpsDto.Location;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tourGuide.helper.DateTimeHelper;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
//@Lazy
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TourGuideController {
    private final TourGuideService tourGuideService;
    private final UserApi userApi;

    DateTimeHelper dateTimeHelper = new DateTimeHelper();

    @Operation(summary = "Get greeting message")
    @GetMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    @Operation(summary = "Get user location")
    @GetMapping("/getLocation")
    public Location getLocation(@Parameter(description = "username", required = true)
                                @RequestParam String userName) {
        User user = userApi.getUserByUserName(userName, dateTimeHelper.getTimeStamp());
        if (user == null) throw new ValidationException("user with username =[" + userName + "] not found");
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
        return visitedLocation.location;
    }

    //  TO DO: Change this method to no longer return a List of Attractions.
    //  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
    //  Return a new JSON object that contains:
    // Name of Tourist attraction,
    // Tourist attractions lat/long,
    // The user's location lat/long,
    // The distance in miles between the user's location and each of the attractions.
    // The reward points for visiting each Attraction.
    //    Note: Attraction reward points can be gathered from RewardsCentral
    @Operation(summary = "Get top 5 near by attraction for user by username")
    @GetMapping("/getNearbyAttractions")
    public NearByAttractionDto getNearbyAttractions(@Parameter(description = "username", required = true)
                                                    @RequestParam String userName) {
        return tourGuideService.getTopFiveNearByAttractions(userName);
    }

    @Operation(summary = "Get list of rewards for user by username")
    @GetMapping("/getRewards")
    public List<UserReward> getRewards(@Parameter(description = "username", required = true)
                                       @RequestParam String userName) {
        List<UserReward> userRewards = userApi.getUserRewardsById(userName, dateTimeHelper.getTimeStamp());
        if (userRewards == null) throw new ValidationException("user with username =[" + userName + "] not found");
        return userRewards;
    }

    @Operation(summary = "Get all users")
    @GetMapping("/getAllCurrentLocations")
    public Map<UUID, Location> getAllCurrentLocations() {
        // TO DO: Get a list of every user's most recent location as JSON
        //- Note: does not use gpsUtil to query for their current location,
        //        but rather gathers the user's current location from their stored location history.
        //
        // Return object should be the just a JSON mapping of userId to Locations similar to:
        //     {
        //        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
        //        ...
        //     }
        return tourGuideService.getCurrentLocationsMap();
    }

    @Operation(summary = "Add trip deals to user by username")
    @GetMapping("/getTripDeals")
    public List<Provider> getTripDeals(@Parameter(description = "username", required = true)
                                       @RequestParam String userName) {
        User user = userApi.getUserByUserName(userName, dateTimeHelper.getTimeStamp());
        if (user == null) throw new ValidationException("user with username =[" + userName + "] not found");
        return tourGuideService.getTripDeals(user);
    }

    @Operation(summary = "Update preferences for user by username  ")
    @PutMapping("/users/addUserPreferences")
    public User addUserPreferences(@Parameter(description = "username", required = true)
                                   @RequestParam String userName,
                                   @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User preferences to update",
                                           required = true, content = @Content(schema = @Schema(implementation = UserPreferences.class)))
                                   @RequestBody UserPreferences userPreferences) {
        return tourGuideService.addUserPreferences(userName, userPreferences);

    }

}
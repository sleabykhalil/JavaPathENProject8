package tourGuide;

import com.jsoniter.output.JsonStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import tourGuide.dto.NearByAttractionDto;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDto.User;
import tourGuide.feign.dto.UserDto.UserPreferences;
import tourGuide.feign.dto.UserDto.UserReward;
import tourGuide.feign.dto.gpsDto.Location;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tourGuide.helper.DateTimeHelper;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

import java.util.*;

@RestController
@Lazy
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TourGuideController {
    private final TourGuideService tourGuideService;
    private final UserApi userApi;

    DateTimeHelper dateTimeHelper = new DateTimeHelper();

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    @RequestMapping("/getLocation")
    public Location getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(userApi.getUserByUserName(userName, dateTimeHelper.getTimeStamp()));
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
    @RequestMapping("/getNearbyAttractions")
    public NearByAttractionDto getNearbyAttractions(@RequestParam String userName) {
        return tourGuideService.getTopFiveNearByAttractions(userName);
    }

    @RequestMapping("/getRewards")
    public List<UserReward> getRewards(@RequestParam String userName) {
        return userApi.getUserRewardsById(userName, dateTimeHelper.getTimeStamp());
    }

    @RequestMapping("/getAllCurrentLocations")
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

    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
        return tourGuideService.getTripDeals(userApi.getUserByUserName(userName, dateTimeHelper.getTimeStamp()));
    }

    @PutMapping("/users/addUserPreferences")
    public User addUserPreferences(@RequestParam String userName, @RequestBody UserPreferences userPreferences) {
        return tourGuideService.addUserPreferences(userName,userPreferences);

    }

}
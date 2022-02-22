package tourGuide;

import com.jsoniter.output.JsonStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDte.User;
import tourGuide.feign.dto.UserDte.UserPreferences;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tourGuide.helper.DateTimeHelper;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

import java.util.Date;
import java.util.List;

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
    public String getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(userApi.getUserByUserName(userName, dateTimeHelper.getTimeStamp()));
        return JsonStream.serialize(visitedLocation.location);
    }

    //  TODO: Change this method to no longer return a List of Attractions.
    //  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
    //  Return a new JSON object that contains:
    // Name of Tourist attraction,
    // Tourist attractions lat/long,
    // The user's location lat/long,
    // The distance in miles between the user's location and each of the attractions.
    // The reward points for visiting each Attraction.
    //    Note: Attraction reward points can be gathered from RewardsCentral
    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(userApi.getUserByUserName(userName, new Date().toString()));
        return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocation));
    }

    @RequestMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        return JsonStream.serialize(userApi.getUserRewardsById(userName, dateTimeHelper.getTimeStamp()));
    }

    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        // TODO: Get a list of every user's most recent location as JSON
        //- Note: does not use gpsUtil to query for their current location,
        //        but rather gathers the user's current location from their stored location history.
        //
        // Return object should be the just a JSON mapping of userId to Locations similar to:
        //     {
        //        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
        //        ...
        //     }

        return JsonStream.serialize("");
    }

    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        List<Provider> providers = tourGuideService.getTripDeals(userApi.getUserByUserName(userName, dateTimeHelper.getTimeStamp()));
        return JsonStream.serialize(providers);
    }

    @PutMapping("/users/addUserPreferences")
    public User addUserPreferences(@RequestParam String userName, @RequestBody UserPreferences userPreferences) {
        User user = userApi.getUserByUserName(userName, dateTimeHelper.getTimeStamp());
        user.setUserPreferences(userPreferences);
        user = userApi.addUser(dateTimeHelper.getTimeStamp(), user);
        return user;
//        return JsonStream.serialize(user);
    }

}
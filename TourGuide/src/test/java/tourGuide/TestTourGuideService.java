package tourGuide;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.dto.NearByAttractionDto;
import tourGuide.feign.GpsApi;
import tourGuide.feign.RewardApi;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDto.User;
import tourGuide.feign.dto.UserDto.UserPreferences;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tourGuide.helper.DateTimeHelper;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestTourGuideService {
    @Autowired
    GpsApi gpsApi;
    @Autowired
    UserApi userApi;
    @Autowired
    RewardApi rewardApi;
    private final DateTimeHelper dateTimeHelper = new DateTimeHelper();
    CurrencyUnit currency = Monetary.getCurrency("USD");

    @Test
    public void getUserLocation() {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        userApi.addUser(dateTimeHelper.getTimeStamp(), user);
        VisitedLocation visitedLocation = null;

        visitedLocation = tourGuideService.trackUserLocation(user);
        tourGuideService.tracker.stopTracking();
        assertEquals(visitedLocation.userId, user.getUserId());
    }

    @Test
    public void addUser() {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        user = userApi.addUser(dateTimeHelper.getTimeStamp(), user);
        user2 = userApi.addUser(dateTimeHelper.getTimeStamp(), user2);

        User retrivedUser = userApi.getUserByUserName(user.getUserName(), dateTimeHelper.getTimeStamp());
        User retrivedUser2 = userApi.getUserByUserName(user2.getUserName(), dateTimeHelper.getTimeStamp());

        tourGuideService.tracker.stopTracking();

        assertEquals(user.getUserId(), retrivedUser.getUserId());
        assertEquals(user2.getUserId(), retrivedUser2.getUserId());

        assertEquals(user.getUserName(), retrivedUser.getUserName());
        assertEquals(user2.getUserName(), retrivedUser2.getUserName());

        assertEquals(user.getPhoneNumber(), retrivedUser.getPhoneNumber());
        assertEquals(user2.getPhoneNumber(), retrivedUser2.getPhoneNumber());

        assertEquals(user.getEmailAddress(), retrivedUser.getEmailAddress());
        assertEquals(user2.getEmailAddress(), retrivedUser2.getEmailAddress());

    }

    @Test
    public void getAllUsers() {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        userApi.addUser(dateTimeHelper.getTimeStamp(), user);
        userApi.addUser(dateTimeHelper.getTimeStamp(), user2);

        List<User> allUsers = userApi.getAllUsers(dateTimeHelper.getTimeStamp());

        tourGuideService.tracker.stopTracking();
        List<String> userNameList = allUsers.stream().map(User::getUserName).collect(Collectors.toList());

        assertTrue(userNameList
                .contains(userApi.getUserByUserName(user.getUserName(), dateTimeHelper.getTimeStamp()).getUserName()));
        assertTrue(userNameList
                .contains(userApi.getUserByUserName(user2.getUserName(), dateTimeHelper.getTimeStamp()).getUserName()));
    }

    @Test
    public void trackUser() {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        userApi.addUser(dateTimeHelper.getTimeStamp(), user);

        VisitedLocation visitedLocation = null;
        visitedLocation = tourGuideService.trackUserLocation(user);
        tourGuideService.tracker.stopTracking();
        user = userApi.getUserByUserName(user.getUserName(), dateTimeHelper.getTimeStamp());
        assertEquals(user.getUserId(), visitedLocation.userId);
    }


    @Test
    public void getTopFiveNearByAttractions() {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        userApi.addUser(dateTimeHelper.getTimeStamp(), user);

        VisitedLocation visitedLocation = null;
        visitedLocation = tourGuideService.trackUserLocation(user);

        NearByAttractionDto nearByAttractionDto = tourGuideService.getTopFiveNearByAttractions(user.getUserName());

        tourGuideService.tracker.stopTracking();

        assertThat(nearByAttractionDto.getPotentialAttractions().size()).isEqualTo(5);
    }

    @Test
    public void getTripDeals() {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        userApi.addUser(dateTimeHelper.getTimeStamp(), user);

        List<Provider> providers = tourGuideService.getTripDeals(user);

        tourGuideService.tracker.stopTracking();

        assertEquals(5, providers.size());
    }

    @Test
    public void getTripDealsWithUserPreferences() {
        RewardsService rewardsService = new RewardsService(gpsApi, rewardApi, userApi);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, gpsApi, userApi);

        User user1 = new User(UUID.randomUUID(), "jon000", "000", "jon@tourGuide.com");
        UserPreferences userPreferences = new UserPreferences(10,
                Money.of(10, currency),
                Money.of(100, currency),
                0,
                1,
                0,
                0);
        user1.setUserPreferences(userPreferences);
        userApi.addUser(dateTimeHelper.getTimeStamp(), user1);

        List<Provider> user1Providers = tourGuideService.getTripDeals(user1);

        tourGuideService.tracker.stopTracking();

        assertEquals(0.99, user1Providers.get(0).price);
    }

}

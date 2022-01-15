package tourGuide;

import gpsUtil.GpsUtil;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import tourGuide.feign.GpsApi;
import tourGuide.feign.RewordApi;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDte.User;
import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tourGuide.helper.DateTimeHelper;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestTourGuideService {
    GpsApi gpsApi;
    UserApi userApi;
    RewordApi rewordApi;
    private final DateTimeHelper dateTimeHelper = new DateTimeHelper();

    @Test
    public void getUserLocation() {
        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService(gpsApi, rewordApi, userApi);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, gpsApi, userApi);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = null;
        visitedLocation = tourGuideService.trackUserLocation(user);
        tourGuideService.tracker.stopTracking();
        assertEquals(visitedLocation.userId, user.getUserId());
    }

    @Test
    public void addUser() {
        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService(gpsApi, rewordApi, userApi);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, gpsApi, userApi);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        userApi.addUser(dateTimeHelper.getTimeStamp(), user);
        userApi.addUser(dateTimeHelper.getTimeStamp(), user2);

        User retrivedUser = userApi.getUserByUserName(user.getUserName(),dateTimeHelper.getTimeStamp());
        User retrivedUser2 = userApi.getUserByUserName(user2.getUserName(), dateTimeHelper.getTimeStamp());

        tourGuideService.tracker.stopTracking();

        assertEquals(user, retrivedUser);
        assertEquals(user2, retrivedUser2);
    }

    @Test
    public void getAllUsers() {
        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService(gpsApi, rewordApi, userApi);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, gpsApi, userApi);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        userApi.addUser(dateTimeHelper.getTimeStamp(), user);
        userApi.addUser(dateTimeHelper.getTimeStamp(), user2);

        List<User> allUsers = userApi.getAllUsers(dateTimeHelper.getTimeStamp());

        tourGuideService.tracker.stopTracking();

        assertTrue(allUsers.contains(user));
        assertTrue(allUsers.contains(user2));
    }

    @Test
    public void trackUser() {
        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService(gpsApi, rewordApi, userApi);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, gpsApi, userApi);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = null;
        visitedLocation = tourGuideService.trackUserLocation(user);
        tourGuideService.tracker.stopTracking();

        assertEquals(user.getUserId(), visitedLocation.userId);
    }

    @Ignore // Not yet implemented
    @Test
    public void getNearbyAttractions() {
        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService(gpsApi, rewordApi, userApi);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, gpsApi, userApi);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = null;
        visitedLocation = tourGuideService.trackUserLocation(user);
        List<Attraction> attractions = tourGuideService.getNearByAttractions(visitedLocation);

        tourGuideService.tracker.stopTracking();

        assertEquals(5, attractions.size());
    }

    public void getTripDeals() {
        GpsUtil gpsUtil = new GpsUtil();
        RewardsService rewardsService = new RewardsService(gpsApi, rewordApi, userApi);
        InternalTestHelper.setInternalUserNumber(0);
        TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService, gpsApi, userApi);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        List<Provider> providers = tourGuideService.getTripDeals(user);

        tourGuideService.tracker.stopTracking();

        assertEquals(10, providers.size());
    }


}

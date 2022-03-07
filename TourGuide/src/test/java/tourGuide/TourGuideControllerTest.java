package tourGuide;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.controller.TourGuideController;
import tourGuide.exception.ValidationException;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDto.User;
import tourGuide.feign.dto.UserDto.UserPreferences;
import tourGuide.feign.dto.gpsDto.Location;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tourGuide.service.TourGuideService;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class TourGuideControllerTest {

    @Mock
    TourGuideService tourGuideServiceMocked;

    @Mock
    UserApi userApiMock;

    TourGuideController tourGuideControllerUnderTest;

    String dateTimeHelperTimeStamp = "2020-10-10";
    CurrencyUnit currency = Monetary.getCurrency("USD");

    @BeforeAll
    static void beforeAll() {
        TourGuideService.testMode = true;
    }

    @AfterAll
    static void afterAll() {
        TourGuideService.testMode = false;
    }

    @BeforeEach
    public void init() {
        tourGuideControllerUnderTest = new TourGuideController(tourGuideServiceMocked, userApiMock);
    }

    @Test
    void addUserPreferences() {
        //given
        User user = new User(UUID.randomUUID(),
                "jon",
                "000",
                "jon@tourGuide.com");
        UserPreferences userPreferences = new UserPreferences(10,
                Money.of(10, currency),
                Money.of(100, currency),
                5,
                1,
                2,
                2);
        user.setUserPreferences(userPreferences);
        when(tourGuideServiceMocked.addUserPreferences(user.getUserName(), userPreferences)).thenReturn(user);
        //when
        User result = tourGuideControllerUnderTest.addUserPreferences("jon", userPreferences);
        //then
        verify(tourGuideServiceMocked, times(1)).addUserPreferences(user.getUserName(), userPreferences);
        assertThat(result.getUserPreferences()).isEqualTo(userPreferences);
    }

    @Test
    void index() {
        String result = tourGuideControllerUnderTest.index();
        assertThat(result).isEqualTo("Greetings from TourGuide!");
    }

    @Test
    void getLocationWhenUserNotFoundThrowException() {
        //given
        String username = "notExist";
        when(userApiMock.getUserByUserName(eq(username), anyString())).thenReturn(null);
        //when
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() ->
                        tourGuideControllerUnderTest.getLocation(username)
                ).withMessageContaining(username);
    }

    @Test
    void getLocationWhenUserFoundLocationIsReturned() {
        //given
        User user = new User(UUID.randomUUID(),
                "jon",
                "000",
                "jon@tourGuide.com");
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId()
                , new Location(10.00, 10.00), new Date());
        user.getVisitedLocations().add(visitedLocation);
        when(userApiMock.getUserByUserName(eq(user.getUserName()), anyString())).thenReturn(user);
        when(tourGuideServiceMocked.getUserLocation(user)).thenReturn(visitedLocation);
        //when
        Location result = tourGuideControllerUnderTest.getLocation(user.getUserName());
        //then
        assertThat(result).isEqualTo(user.getVisitedLocations().get(0).getLocation());
    }
}
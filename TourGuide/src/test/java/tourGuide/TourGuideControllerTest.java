package tourGuide;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDto.User;
import tourGuide.feign.dto.UserDto.UserPreferences;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class TourGuideControllerTest {

    @Mock
    TourGuideService tourGuideServiceMocked;

    @Mock
    UserApi userApiMock;

    TourGuideController tourGuideControllerUnderTest;

    String dateTimeHelperTimeStamp = "2020-10-10";
    CurrencyUnit currency = Monetary.getCurrency("USD");

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
        when(userApiMock.getUserByUserName(eq("jon"), any()))
                .thenReturn(user);
        userApiMock.addUser(dateTimeHelperTimeStamp, user);
        UserPreferences userPreferences = new UserPreferences(10,
                Money.of(10, currency),
                Money.of(100, currency),
                5,
                1,
                2,
                2);
        user.setUserPreferences(userPreferences);
        when(userApiMock.addUser(any(), eq(user))).thenReturn(user);
        //when
        User result = tourGuideControllerUnderTest.addUserPreferences("jon", userPreferences);
        //then
        assertThat(result.getUserPreferences()).isEqualTo(userPreferences);
    }

    @Test
    void getTripPriceAfterChangeUserPreferences() {
        //given
        User user = new User(UUID.randomUUID(),
                "jon",
                "000",
                "jon@tourGuide.com");
        when(userApiMock.getUserByUserName(eq("jon"), any()))
                .thenReturn(user);
        userApiMock.addUser(dateTimeHelperTimeStamp, user);
        UserPreferences userPreferences = new UserPreferences(10,
                Money.of(10, currency),
                Money.of(100, currency),
                0,
                0,
                0,
                0);
        user.setUserPreferences(userPreferences);
        when(userApiMock.addUser(any(), eq(user))).thenReturn(user);
        //when
        User result = tourGuideControllerUnderTest.addUserPreferences("jon", userPreferences);
        List<Provider> tripDeals = tourGuideControllerUnderTest.getTripDeals(user.getUserName());
        //then
        assertThat(result.getUserPreferences()).isEqualTo(userPreferences);
        assertThat(tripDeals.get(0).price).isEqualTo(0.99);

    }
}
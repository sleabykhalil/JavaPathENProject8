package tourGuide.testIT;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.TourGuideController;
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

@SpringBootTest
public class TourGuideControllerITTest {

    @Autowired
    TourGuideService tourGuideService;

    @Autowired
    UserApi userApi;

    TourGuideController tourGuideControllerUnderTest;

    String dateTimeHelperTimeStamp = "2020-10-10";
    CurrencyUnit currency = Monetary.getCurrency("USD");

    @BeforeEach
    public void init() {
        tourGuideControllerUnderTest = new TourGuideController(tourGuideService, userApi);
    }

    @Test
    void getTripPriceAfterChangeUserPreferences() {
        //given
        User user = new User(UUID.randomUUID(),
                "jon",
                "000",
                "jon@tourGuide.com");

        userApi.addUser(dateTimeHelperTimeStamp, user);
        UserPreferences userPreferences = new UserPreferences(10,
                Money.of(10, currency),
                Money.of(100, currency),
                0,
                0,
                0,
                0);
        user.setUserPreferences(userPreferences);
        //when
        User result = tourGuideControllerUnderTest.addUserPreferences("jon", userPreferences);
        List<Provider> tripDeals = tourGuideControllerUnderTest.getTripDeals(user.getUserName());
        //then
        assertThat(result.getUserPreferences()).isEqualTo(userPreferences);
        assertThat(tripDeals.get(0).price).isEqualTo(0.99);

    }
}
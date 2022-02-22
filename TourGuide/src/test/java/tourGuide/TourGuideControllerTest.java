package tourGuide;

import com.jsoniter.output.JsonStream;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourGuide.feign.UserApi;
import tourGuide.feign.dto.UserDte.User;
import tourGuide.feign.dto.UserDte.UserPreferences;
import tourGuide.service.TourGuideService;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
//@ExtendWith(SpringExtension.class)
//@ExtendWith(MockitoExtension.class)
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
}
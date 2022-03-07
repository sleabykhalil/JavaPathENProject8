package tourGuide;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.feign.GpsApi;
import tourGuide.feign.RewardApi;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tourGuide.helper.DateTimeHelper;
import tourGuide.service.TourGuideService;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class FeignTest {
    @Autowired
    GpsApi gpsApi;
    @Autowired
    RewardApi rewardApi;
    private final DateTimeHelper dateTimeHelper = new DateTimeHelper();

    @BeforeAll
    static void beforeAll() {
        TourGuideService.testMode = true;
    }

    @AfterAll
    static void afterAll() {
        TourGuideService.testMode = false;
    }

    @Test
    void gpsGetAttractionForUser() {

        VisitedLocation visitedLocation = gpsApi.getUserLocation(UUID.randomUUID().toString(), dateTimeHelper.getTimeStamp());

        assertThat(visitedLocation).isNotNull();
    }

    @Test
    void rewardApi_getRewardPoints() {
        String attractionId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        int result = rewardApi.getRewardPoints(dateTimeHelper.getTimeStamp(), attractionId, userId);

        assertThat(result).isNotNull();
    }

}

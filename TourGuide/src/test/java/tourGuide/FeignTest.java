package tourGuide;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.feign.GpsApi;
import tourGuide.feign.RewordApi;
import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tourGuide.helper.DateTimeHelper;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class FeignTest {
    @Autowired
    GpsApi gpsApi;
    @Autowired
    RewordApi rewordApi;
    private final DateTimeHelper dateTimeHelper = new DateTimeHelper();

    @Test
    void gpsGetAttractionForUser() {

        VisitedLocation visitedLocation = gpsApi.getUserAttraction(UUID.randomUUID().toString(), dateTimeHelper.getTimeStamp());

        assertThat(visitedLocation).isNotNull();
    }

    @Test
    void rewordApi_getRewordPoints() {
        String attractionId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        int result = rewordApi.getRewardPoints(dateTimeHelper.getTimeStamp(), attractionId, userId);

        assertThat(result).isNotNull();
    }

}

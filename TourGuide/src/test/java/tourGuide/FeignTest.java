package tourGuide;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.feign.GpsApi;
import tourGuide.feign.RewordApi;
import tourGuide.feign.dto.gpsDto.VisitedLocation;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class FeignTest {
    @Autowired
    GpsApi gpsApi;
    @Autowired
    RewordApi rewordApi;

    @Test
    void gpsGetAttractionForUser() {

        VisitedLocation visitedLocation = gpsApi.getUserAttraction(UUID.randomUUID().toString());

        assertThat(visitedLocation).isNotNull();
    }

    @Test
    void rewordApi_getRewordPoints() {
        String attractionId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        int result = rewordApi.getRewardPoints(attractionId, userId);

        assertThat(result).isNotNull();
    }

}

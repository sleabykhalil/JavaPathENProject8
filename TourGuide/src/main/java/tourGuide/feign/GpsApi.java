package tourGuide.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.feign.dto.gpsDto.VisitedLocation;

import java.util.List;

//@FeignClient(name = "GpsApi", url = "http://localhost:8083")
@FeignClient(name = "GpsApi", url = "http://gps.localhost:81")
public interface GpsApi {

    @GetMapping("gps/attractions/{timeStamp}")
    List<Attraction> getAllAttractions(@PathVariable String timeStamp);

    @GetMapping("gps/attraction/{userId}/{timeStamp}")
    VisitedLocation getUserLocation(@PathVariable String userId, @PathVariable String timeStamp);
}

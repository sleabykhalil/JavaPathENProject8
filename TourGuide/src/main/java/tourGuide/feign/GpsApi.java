package tourGuide.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.feign.dto.gpsDto.VisitedLocation;

import java.util.List;

@FeignClient(name = "GpsApi", url = "http://localhost:8083")
public interface GpsApi {

    @GetMapping("gps/attractions/{timeStamp}")
    List<Attraction> getAllAttraction(@PathVariable String timeStamp);

    @GetMapping("gps/attraction/{userId}/{timeStamp}")
    VisitedLocation getUserAttraction(@PathVariable String userId, @PathVariable String timeStamp);
}

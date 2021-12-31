package tourGuide.feign;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "rewordApi", url = "http://localhost:8083/")
public interface GpsApi {

    @GetMapping("gps/attractions")
    public List<Attraction> getAllAttraction() ;

    @GetMapping("gps/attraction")
    public VisitedLocation getUserAttraction(UUID userId);
}

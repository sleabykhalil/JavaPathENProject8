package gpsApi.controller;

import gpsApi.service.GpsService;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GpsController {
    private Logger logger = LoggerFactory.getLogger(GpsController.class);

    private final GpsService gpsService;

    @Operation(summary = "Get all attractions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all attractions"),
    })
    @GetMapping("gps/attractions/{timeStamp}")
    public List<Attraction> getAllAttractions(@Parameter(description = "TimeStamp", required = true)
                                              @PathVariable String timeStamp) {
        logger.info("gps/attractions/timeStamp ={}", timeStamp);
        return gpsService.getAttractions();
    }

    @Operation(summary = "Get user attraction by user Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all attractions"),
    })
    @GetMapping("gps/attraction/{userId}/{timeStamp}")
    public VisitedLocation getUserLocation(@Parameter(description = "User UUID as string", required = true)
                                           @PathVariable String userId,
                                           @Parameter(description = "TimeStamp", required = true)
                                           @PathVariable String timeStamp) {
        logger.info("gps/attraction/userId{}/timeStamp ={}", userId, timeStamp);
        return gpsService.getUserLocation(userId);
    }
}

package tourGuide.feign.dto;

import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.feign.dto.gpsDto.VisitedLocation;

public class AttractionVisitedLocationPair {
    Attraction attraction;
    VisitedLocation visitedLocation;

    public AttractionVisitedLocationPair(Attraction attraction, VisitedLocation visitedLocation) {
        this.attraction = attraction;
        this.visitedLocation = visitedLocation;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public VisitedLocation getVisitedLocation() {
        return visitedLocation;
    }
}

package tourGuide.feign.dto.gpsDto;

import java.util.Date;
import java.util.UUID;

public class VisitedLocation {
    public UUID userId;
    public Location location;
    public Date timeVisited;

    public VisitedLocation(UUID userId, Location location, Date timeVisited) {
        this.userId = userId;
        this.location = location;
        this.timeVisited = timeVisited;
    }

    public VisitedLocation(UUID userId, Attraction attraction, Date timeVisited) {
        this.userId = userId;
        this.location = new Location(attraction.getLatitude(), attraction.getLongitude());
        this.timeVisited = timeVisited;
    }

    public VisitedLocation() {
    }

    public UUID getUserId() {
        return userId;
    }

    public Location getLocation() {
        return location;
    }

    public Date getTimeVisited() {
        return timeVisited;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setTimeVisited(Date timeVisited) {
        this.timeVisited = timeVisited;
    }

    @Override
    public String toString() {
        return "VisitedLocation{" +
                "userId=" + userId +
                ", location=" + location +
                ", timeVisited=" + timeVisited +
                '}';
    }

}

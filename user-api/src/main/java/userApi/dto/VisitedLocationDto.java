package userApi.dto;


import java.util.Date;
import java.util.UUID;

public class VisitedLocationDto {
    public  UUID userId;
    public  LocationDto location;
    public  Date timeVisited;

    public VisitedLocationDto() {
    }

    public VisitedLocationDto(UUID userId, LocationDto location, Date timeVisited) {
        this.userId = userId;
        this.location = location;
        this.timeVisited = timeVisited;
    }

    @Override
    public String toString() {
        return "VisitedLocationDto{" +
                "userId=" + userId +
                ", location=" + location +
                ", timeVisited=" + timeVisited +
                '}';
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public Date getTimeVisited() {
        return timeVisited;
    }

    public void setTimeVisited(Date timeVisited) {
        this.timeVisited = timeVisited;
    }
}

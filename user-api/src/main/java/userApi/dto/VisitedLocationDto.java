package userApi.dto;

import gpsUtil.location.Location;

import java.util.Date;
import java.util.UUID;

public class VisitedLocationDto {
    public  UUID userId;
    public  Location location;
    public  Date timeVisited;

    public VisitedLocationDto() {
    }

    public VisitedLocationDto(UUID userId, Location location, Date timeVisited) {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getTimeVisited() {
        return timeVisited;
    }

    public void setTimeVisited(Date timeVisited) {
        this.timeVisited = timeVisited;
    }
}

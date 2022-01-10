package userApi.dto;

public class LocationDto {
    public double longitude;
    public double latitude;

    public LocationDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationDto() {
    }

    @Override
    public String toString() {
        return "LocationDto{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}

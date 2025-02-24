package tourGuide.feign.dto.gpsDto;

import java.util.UUID;

public class Attraction extends Location {
    public String attractionName;
    public String city;
    public String state;
    public UUID attractionId;

    public Attraction(String attractionName, String city, String state, double latitude, double longitude) {
        super(latitude, longitude);
        this.attractionName = attractionName;
        this.city = city;
        this.state = state;
        this.attractionId = UUID.randomUUID();
    }

    public Attraction() {
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UUID getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(UUID attractionId) {
        this.attractionId = attractionId;
    }

    @Override
    public String toString() {
        return "Attraction{" +
                "attractionName='" + attractionName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", attractionId=" + attractionId +
                '}';
    }

}

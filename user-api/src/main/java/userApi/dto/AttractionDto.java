package userApi.dto;

import java.util.UUID;

public class AttractionDto extends LocationDto {
    public String attractionName;
    public String city;
    public String state;
    public UUID attractionId;

    public AttractionDto(String attractionName, String city, String state, UUID attractionId) {
        this.attractionName = attractionName;
        this.city = city;
        this.state = state;
        this.attractionId = attractionId;
    }

    public AttractionDto() {
    }

    @Override
    public String toString() {
        return "AttractionDto{" +
                "attractionName='" + attractionName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", attractionId=" + attractionId +
                '}';
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
}

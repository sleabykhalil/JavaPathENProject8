package tourGuide.dto;

import java.util.ArrayList;
import java.util.List;

public class NearByAttractionDto {

    double userLongitude;
    double userLatitude;
    List<PotentialAttraction> potentialAttractions = new ArrayList<>();

    public NearByAttractionDto(double userLongitude, double userLatitude, List<PotentialAttraction> potentialAttractions) {
        this.userLongitude = userLongitude;
        this.userLatitude = userLatitude;
        this.potentialAttractions = potentialAttractions;
    }

    public double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.userLongitude = userLongitude;
    }

    public double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public List<PotentialAttraction> getPotentialAttractions() {
        return potentialAttractions;
    }

    public void setPotentialAttractions(List<PotentialAttraction> potentialAttractions) {
        this.potentialAttractions = potentialAttractions;
    }
}

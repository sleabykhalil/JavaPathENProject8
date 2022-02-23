package tourGuide.dto;

public class PotentialAttraction {
    String attractionName;
    double attractionLongitude;
    double attractionLatitude;
    double distanceFromUserLocation;
    int potentialRewardPoints;

    public PotentialAttraction(String attractionName,
                               double attractionLongitude,
                               double attractionLatitude,
                               double distanceFromUserLocation,
                               int potentialRewardPoints) {
        this.attractionName = attractionName;
        this.attractionLongitude = attractionLongitude;
        this.attractionLatitude = attractionLatitude;
        this.distanceFromUserLocation = distanceFromUserLocation;
        this.potentialRewardPoints = potentialRewardPoints;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public double getAttractionLongitude() {
        return attractionLongitude;
    }

    public void setAttractionLongitude(double attractionLongitude) {
        this.attractionLongitude = attractionLongitude;
    }

    public double getAttractionLatitude() {
        return attractionLatitude;
    }

    public void setAttractionLatitude(double attractionLatitude) {
        this.attractionLatitude = attractionLatitude;
    }

    public double getDistanceFromUserLocation() {
        return distanceFromUserLocation;
    }

    public void setDistanceFromUserLocation(double distanceFromUserLocation) {
        this.distanceFromUserLocation = distanceFromUserLocation;
    }

    public int getPotentialRewardPoints() {
        return potentialRewardPoints;
    }

    public void setPotentialRewardPoints(int potentialRewardPoints) {
        this.potentialRewardPoints = potentialRewardPoints;
    }
}

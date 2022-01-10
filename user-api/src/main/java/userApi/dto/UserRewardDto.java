package userApi.dto;



public class UserRewardDto {
    public  VisitedLocationDto visitedLocation;
    public  AttractionDto attraction;
    private int rewardPoints;

    public UserRewardDto() {
    }

    public UserRewardDto(VisitedLocationDto visitedLocation, AttractionDto attraction, int rewardPoints) {
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
        this.rewardPoints = rewardPoints;
    }

    @Override
    public String toString() {
        return "UserRewardDto{" +
                "visitedLocation=" + visitedLocation +
                ", attraction=" + attraction +
                ", rewardPoints=" + rewardPoints +
                '}';
    }

    public VisitedLocationDto getVisitedLocation() {
        return visitedLocation;
    }

    public void setVisitedLocation(VisitedLocationDto visitedLocation) {
        this.visitedLocation = visitedLocation;
    }

    public AttractionDto getAttraction() {
        return attraction;
    }

    public void setAttraction(AttractionDto attraction) {
        this.attraction = attraction;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
}

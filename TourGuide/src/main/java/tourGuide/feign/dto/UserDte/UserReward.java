package tourGuide.feign.dto.UserDte;

import tourGuide.feign.dto.gpsDto.Attraction;
import tourGuide.feign.dto.gpsDto.VisitedLocation;

public class UserReward {
    public VisitedLocation visitedLocation;
    public Attraction attraction;
    private int rewardPoints;

    public UserReward(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
        this.rewardPoints = rewardPoints;
    }

    public UserReward(VisitedLocation visitedLocation, Attraction attraction) {
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
    }

    public UserReward() {
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public VisitedLocation getVisitedLocation() {
        return visitedLocation;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setVisitedLocation(VisitedLocation visitedLocation) {
        this.visitedLocation = visitedLocation;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserReward)) return false;

        UserReward that = (UserReward) o;

        if (getRewardPoints() != that.getRewardPoints()) return false;
        if (getVisitedLocation() != null ? !getVisitedLocation().equals(that.getVisitedLocation()) : that.getVisitedLocation() != null)
            return false;
        return getAttraction() != null ? getAttraction().equals(that.getAttraction()) : that.getAttraction() == null;
    }

    @Override
    public int hashCode() {
        int result = getVisitedLocation() != null ? getVisitedLocation().hashCode() : 0;
        result = 31 * result + (getAttraction() != null ? getAttraction().hashCode() : 0);
        result = 31 * result + getRewardPoints();
        return result;
    }

    @Override
    public String toString() {
        return "UserReward{" +
                "visitedLocation=" + visitedLocation +
                ", attraction=" + attraction +
                ", rewardPoints=" + rewardPoints +
                '}';
    }
}

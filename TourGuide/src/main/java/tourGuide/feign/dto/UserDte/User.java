package tourGuide.feign.dto.UserDte;

import tourGuide.feign.dto.gpsDto.VisitedLocation;
import tripPricer.Provider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID userId;
    private String userName;
    private String phoneNumber;
    private String emailAddress;
    private Date latestLocationTimestamp;
    private List<VisitedLocation> visitedLocations = new ArrayList<>();
    private List<UserReward> userRewards = new ArrayList<>();
    private UserPreferences userPreferences = new UserPreferences();
    private List<Provider> tripDeals = new ArrayList<>();

    public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (getUserId() != null ? !getUserId().equals(user.getUserId()) : user.getUserId() != null) return false;
        if (getUserName() != null ? !getUserName().equals(user.getUserName()) : user.getUserName() != null)
            return false;
        if (getPhoneNumber() != null ? !getPhoneNumber().equals(user.getPhoneNumber()) : user.getPhoneNumber() != null)
            return false;
        if (getEmailAddress() != null ? !getEmailAddress().equals(user.getEmailAddress()) : user.getEmailAddress() != null)
            return false;
        if (getLatestLocationTimestamp() != null ? !getLatestLocationTimestamp().equals(user.getLatestLocationTimestamp()) : user.getLatestLocationTimestamp() != null)
            return false;
        if (getVisitedLocations() != null ? !getVisitedLocations().equals(user.getVisitedLocations()) : user.getVisitedLocations() != null)
            return false;
        if (getUserRewards() != null ? !getUserRewards().equals(user.getUserRewards()) : user.getUserRewards() != null)
            return false;
        if (getUserPreferences() != null ? !getUserPreferences().equals(user.getUserPreferences()) : user.getUserPreferences() != null)
            return false;
        return getTripDeals() != null ? getTripDeals().equals(user.getTripDeals()) : user.getTripDeals() == null;
    }

    @Override
    public int hashCode() {
        int result = getUserId() != null ? getUserId().hashCode() : 0;
        result = 31 * result + (getUserName() != null ? getUserName().hashCode() : 0);
        result = 31 * result + (getPhoneNumber() != null ? getPhoneNumber().hashCode() : 0);
        result = 31 * result + (getEmailAddress() != null ? getEmailAddress().hashCode() : 0);
        result = 31 * result + (getLatestLocationTimestamp() != null ? getLatestLocationTimestamp().hashCode() : 0);
        result = 31 * result + (getVisitedLocations() != null ? getVisitedLocations().hashCode() : 0);
        result = 31 * result + (getUserRewards() != null ? getUserRewards().hashCode() : 0);
        result = 31 * result + (getUserPreferences() != null ? getUserPreferences().hashCode() : 0);
        result = 31 * result + (getTripDeals() != null ? getTripDeals().hashCode() : 0);
        return result;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
        this.latestLocationTimestamp = latestLocationTimestamp;
    }

    public Date getLatestLocationTimestamp() {
        return latestLocationTimestamp;
    }

    public void addToVisitedLocations(VisitedLocation visitedLocation) {
        visitedLocations.add(visitedLocation);
    }

    public List<VisitedLocation> getVisitedLocations() {
        return visitedLocations;
    }

    public void clearVisitedLocations() {
        visitedLocations.clear();
    }

    public void addUserReward(UserReward userReward) {
        if (userRewards.stream().filter(r -> !r.attraction.attractionName.equals(userReward.attraction)).count() == 0) {
            userRewards.add(userReward);
        }
    }

    public List<UserReward> getUserRewards() {
        return userRewards;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }

    public void setTripDeals(List<Provider> tripDeals) {
        this.tripDeals = tripDeals;
    }

    public List<Provider> getTripDeals() {
        return tripDeals;
    }

    public User(String userName, String phoneNumber, String emailAddress, Date latestLocationTimestamp, List<VisitedLocation> visitedLocations, List<UserReward> userRewards, UserPreferences userPreferences, List<Provider> tripDeals) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.latestLocationTimestamp = latestLocationTimestamp;
        this.visitedLocations = visitedLocations;
        this.userRewards = userRewards;
        this.userPreferences = userPreferences;
        this.tripDeals = tripDeals;
    }

    public User() {
    }


}

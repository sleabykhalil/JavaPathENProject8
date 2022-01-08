package userApi.dto;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserDto {
    private UUID userId;
    private String userName;
    private String phoneNumber;
    private String emailAddress;
    private Date latestLocationTimestamp;
    private List<VisitedLocationDto> visitedLocations = new ArrayList<>();
    private List<UserRewardDto> userRewards = new ArrayList<>();
    private UserPreferencesDto userPreferences ;
    private List<ProviderDto> tripDeals = new ArrayList<>();

    public UserDto() {
    }

    public UserDto(UUID userId, String userName, String phoneNumber, String emailAddress, Date latestLocationTimestamp, List<VisitedLocationDto> visitedLocations, List<UserRewardDto> userRewards, UserPreferencesDto userPreferences, List<ProviderDto> tripDeals) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.latestLocationTimestamp = latestLocationTimestamp;
        this.visitedLocations = visitedLocations;
        this.userRewards = userRewards;
        this.userPreferences = userPreferences;
        this.tripDeals = tripDeals;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", latestLocationTimestamp=" + latestLocationTimestamp +
                ", visitedLocations=" + visitedLocations +
                ", userRewards=" + userRewards +
                ", userPreferences=" + userPreferences +
                ", tripDeals=" + tripDeals +
                '}';
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Date getLatestLocationTimestamp() {
        return latestLocationTimestamp;
    }

    public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
        this.latestLocationTimestamp = latestLocationTimestamp;
    }

    public List<VisitedLocationDto> getVisitedLocations() {
        return visitedLocations;
    }

    public void setVisitedLocations(List<VisitedLocationDto> visitedLocations) {
        this.visitedLocations = visitedLocations;
    }

    public List<UserRewardDto> getUserRewards() {
        return userRewards;
    }

    public void setUserRewards(List<UserRewardDto> userRewards) {
        this.userRewards = userRewards;
    }

    public UserPreferencesDto getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreferencesDto userPreferences) {
        this.userPreferences = userPreferences;
    }

    public List<ProviderDto> getTripDeals() {
        return tripDeals;
    }

    public void setTripDeals(List<ProviderDto> tripDeals) {
        this.tripDeals = tripDeals;
    }
}
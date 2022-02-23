package userApi.dto;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;
import userApi.model.User;
import userApi.model.UserPreferences;
import userApi.model.UserReward;

import java.util.ArrayList;
import java.util.List;

public class Mapper {
    public User userDtoToUser(UserDto userDto) {
        User user = new User(userDto.getUserId(), userDto.getUserName(), userDto.getPhoneNumber(), userDto.getEmailAddress(),
                userDto.getLatestLocationTimestamp(),
                toVisitedLocations(userDto.getVisitedLocations()),
                toUserRewardList(userDto.getUserRewards()),
                toUserPreferences(userDto.getUserPreferences()),
                toTripDeals(userDto.getTripDeals()));
/*        user.setLatestLocationTimestamp(userDto.getLatestLocationTimestamp());
        user.setVisitedLocations(toVisitedLocations(userDto.getVisitedLocations()));
        user.setUserRewards(toUserReward(userDto.getUserRewards()));
        user.setUserPreferences(toUserPreferences(userDto.getUserPreferences()));
        user.setTripDeals(toTripDeals(userDto.getTripDeals()));*/
        return user;
    }

    public List<Provider> toTripDeals(List<ProviderDto> tripDeals) {
        List<Provider> providerList = new ArrayList<>();
        for (ProviderDto td : tripDeals) {
            providerList.add(new Provider(td.getTripId(), td.getName(), td.getPrice()));
        }
        return providerList;
    }

    private UserPreferences toUserPreferences(UserPreferencesDto userPreferencesDto) {
        return new UserPreferences(userPreferencesDto.getAttractionProximity(),
                userPreferencesDto.getCurrency(),
                userPreferencesDto.getLowerPricePoint(),
                userPreferencesDto.getHighPricePoint(),
                userPreferencesDto.getTripDuration(),
                userPreferencesDto.getTicketQuantity(),
                userPreferencesDto.getNumberOfAdults(),
                userPreferencesDto.getNumberOfChildren());
    }

    public List<UserReward> toUserRewardList(List<UserRewardDto> userRewards) {
        List<UserReward> userRewardList = new ArrayList<>();
        for (UserRewardDto ur : userRewards) {
            userRewardList.add(new UserReward(toVisitedLocation(ur.getVisitedLocation()), toAttraction(ur.getAttraction()), ur.getRewardPoints()));
        }
        return userRewardList;
    }

    public Attraction toAttraction(AttractionDto attractionDto) {
        return new Attraction(attractionDto.attractionName, attractionDto.city, attractionDto.state, attractionDto.latitude, attractionDto.longitude);
    }

    public VisitedLocation toVisitedLocation(VisitedLocationDto vl) {
        return new VisitedLocation(vl.getUserId(), toLocation(vl.getLocation()), vl.getTimeVisited());
    }

    private List<VisitedLocation> toVisitedLocations(List<VisitedLocationDto> visitedLocations) {
        List<VisitedLocation> visitedLocationList = new ArrayList<>();
        for (VisitedLocationDto vl : visitedLocations) {
            visitedLocationList.add(new VisitedLocation(vl.getUserId(), toLocation(vl.getLocation()), vl.getTimeVisited()));
        }
        return visitedLocationList;
    }

    private Location toLocation(LocationDto location) {
        return new Location(location.latitude, location.longitude);
    }

    public UserReward toUserReward(UserRewardDto userRewardDto) {
        return new UserReward(toVisitedLocation(userRewardDto.getVisitedLocation()),
                toAttraction(userRewardDto.getAttraction()), userRewardDto.getRewardPoints());
    }

    public List<UserRewardDto> toUserRewardListDto(List<UserReward> UserRewardList) {
        List<UserRewardDto> userRewardDtoList = new ArrayList<>();
        for (UserReward ur : UserRewardList) {
            userRewardDtoList.add(new UserRewardDto(toVisitedLocationDto(ur.getVisitedLocation()), toAttractionDto(ur.getAttraction()), ur.getRewardPoints()));
        }
        return userRewardDtoList;
    }

    private AttractionDto toAttractionDto(Attraction attraction) {
        return new AttractionDto(attraction.attractionName, attraction.city, attraction.state, attraction.attractionId);
    }

    private VisitedLocationDto toVisitedLocationDto(VisitedLocation visitedLocation) {
        return new VisitedLocationDto(visitedLocation.userId, toLocationDto(visitedLocation.location), visitedLocation.timeVisited);
    }

    private LocationDto toLocationDto(Location location) {
        return new LocationDto(location.latitude, location.longitude);
    }
}

package userApi.service;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tripPricer.Provider;
import userApi.dto.UserRewardDto;
import userApi.dto.VisitedLocationDto;
import userApi.model.User;
import userApi.model.UserReward;
import userApi.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<UserReward> getUserRewards(String userName) {
        User user = userRepository.getUserByUserName(userName);
        return user.getUserRewards();
    }

    public List<UserReward> getUserRewards(User user) {
        User userToFind = userRepository.getUserByUserName(user.getUserName());
        return userToFind.getUserRewards();
    }

    public User getUser(String userName) {
        return userRepository.getUserByUserName(userName);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUser();
    }

    public List<VisitedLocation> getVisitedLocation(User user) {
        User userToFind = userRepository.getUserByUserName(user.getUserName());
        return userToFind.getVisitedLocations();
    }

    public void setTripDeals(String userName, List<Provider> providers) {
        User userToFind = userRepository.getUserByUserName(userName);
        userToFind.setTripDeals(providers);
        userRepository.save(userToFind);
    }

    public void addToVisitedLocations(String userName, VisitedLocation visitedLocation) {
        User userToFind = userRepository.getUserByUserName(userName);
        if (userToFind.getVisitedLocations().stream().filter(vl -> vl.equals(visitedLocation)).count() == 0) {
            userToFind.getVisitedLocations().add(visitedLocation);
            userRepository.save(userToFind);
        }
    }

    public void addUserReward(String userName, UserReward userReward) {
        User userToFind = userRepository.getUserByUserName(userName);
        userToFind.getUserRewards().add(userReward);
        userRepository.save(userToFind);
    }

    public List<UserReward> addUserRewardList(String userName, List<UserReward> userRewards) {
        User userToFind = userRepository.getUserByUserName(userName);
        for (UserReward userReward : userRewards) {
            userToFind.getUserRewards().add(userReward);
        }
        userRepository.save(userToFind);
        return userToFind.getUserRewards();
    }

    /**********************************************************************************
     *
     * Methods Below: For Internal Testing
     *
     **********************************************************************************/
    public void initializeInternalUsers(int internalUserNumber) {
        userRepository.deleteAll();
        IntStream.range(0, internalUserNumber).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            userRepository.addUser(userName, user);
            generateUserLocationHistory(user);
        });
        logger.debug("Created " + internalUserNumber + " internal test users.");
    }

    private void generateUserLocationHistory(User user) {
        IntStream.range(0, 3).forEach(i -> {
            addToVisitedLocations(user.getUserName(), new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
        });
    }

    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

}

package userApi.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tripPricer.Provider;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    /**
     * Get user rewards
     * @param userName username
     * @return List of user reward
     */
    public List<UserReward> getUserRewards(String userName) {
        User user = userRepository.getUserByUserName(userName);
        return user.getUserRewards();
    }

    /**
     * Get user by username
     * @param userName username as string
     * @return user object
     */
    public User getUser(String userName) {
        return userRepository.getUserByUserName(userName);
    }

    /**
     * Add or update user if exist
     * @param user user to add or update
     * @return user after adding or update
     */
    public User addUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Get all users
     * @return  List of users
     */
    public List<User> getAllUsers() {
        return userRepository.getAllUser();
    }

    /**
     * Add or Update trip deals to user
     * @param userName user to add trip deals
     * @param providers List of trip deals
     */
    public void setTripDeals(String userName, List<Provider> providers) {
        User userToFind = userRepository.getUserByUserName(userName);
        userToFind.setTripDeals(providers);
        userRepository.save(userToFind);
    }

    /**
     * Add new location to user visited location
     * @param userName user to add
     * @param visitedLocation new location
     */
    public void addToVisitedLocations(String userName, VisitedLocation visitedLocation) {
        User userToFind = userRepository.getUserByUserName(userName);
        if (userToFind.getVisitedLocations().stream().filter(vl -> vl.equals(visitedLocation)).count() == 0) {
            userToFind.getVisitedLocations().add(visitedLocation);
            userRepository.save(userToFind);
        }
    }

    /**
     * Add reward list to user
     * @param userName user to add
     * @param userRewards List of user reward to add
     * @return all user rewards
     */
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

    /**
     * For test, initialize users
     * @param internalUserNumber number of users
     */
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

    /**
     * For test, add first attraction to all users
     * @param attraction
     */
    public List<User> addVisitedLocationForTest(Attraction attraction) {
        List<User> allUser = userRepository.getAllUser();
        for (User user : allUser) {
            user.getVisitedLocations().add(new VisitedLocation(user.getUserId(), new Location(attraction.latitude, attraction.longitude), getRandomTime()));
            userRepository.save(user);
        }
      return  userRepository.getAllUser();
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

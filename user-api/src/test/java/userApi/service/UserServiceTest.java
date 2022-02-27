package userApi.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tripPricer.Provider;
import userApi.model.User;
import userApi.model.UserReward;
import userApi.repository.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepositoryMock;

    UserService userServiceUnderTest;

    @BeforeEach
    void setUp() {
        userServiceUnderTest = new UserService(userRepositoryMock);
    }

    @Test
    void getUserRewards() {
        //given
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        when(userRepositoryMock.getUserByUserName(user.getUserName())).thenReturn(user);
        //when
        userServiceUnderTest.getUserRewards(user.getUserName());
        //then
        verify(userRepositoryMock, times(1)).getUserByUserName(user.getUserName());
    }

    @Test
    void getUser() {
        //given
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        when(userRepositoryMock.getUserByUserName(user.getUserName())).thenReturn(user);
        //when
        userServiceUnderTest.getUser(user.getUserName());
        //then
        verify(userRepositoryMock, times(1)).getUserByUserName(user.getUserName());
    }

    @Test
    void addUser() {
        //given
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        when(userRepositoryMock.save(user)).thenReturn(user);
        //when
        userServiceUnderTest.addUser(user);
        //then
        verify(userRepositoryMock, times(1)).save(user);
    }

    @Test
    void getAllUsers() {
        //given
        List<User> allUsers = new ArrayList<>();
        allUsers.add(new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com"));
        when(userRepositoryMock.getAllUser()).thenReturn(allUsers);
        //when
        userServiceUnderTest.getAllUsers();
        //then
        verify(userRepositoryMock, times(1)).getAllUser();
    }

    @Test
    void setTripDeals() {
        //given
        User user = new User(UUID.randomUUID(), "username", "000", "jon@tourGuide.com");
        Provider provider = new Provider(UUID.randomUUID(), "providerName", 100.00);
        List<Provider> providerList = new ArrayList<>();
        providerList.add(provider);
        user.setTripDeals(providerList);
        when(userRepositoryMock.getUserByUserName("username")).thenReturn(user);
        when(userRepositoryMock.save(user)).thenReturn(user);
        //when
        userServiceUnderTest.setTripDeals(user.getUserName(), providerList);
        //then
        verify(userRepositoryMock, times(1)).save(user);
    }

    @Test
    void addToVisitedLocations() {
        //given
        User user = new User(UUID.randomUUID(), "username", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(),
                new Location(-44.437913, -34.185441), new Date());
        when(userRepositoryMock.getUserByUserName("username")).thenReturn(user);
        when(userRepositoryMock.save(user)).thenReturn(user);
        //when
        userServiceUnderTest.addToVisitedLocations("username", visitedLocation);
        //then
        verify(userRepositoryMock, times(1)).getUserByUserName("username");
        verify(userRepositoryMock, times(1)).save(user);
    }

    @Test
    void addUserRewardList() {
        //given
        User user = new User(UUID.randomUUID(), "username", "000", "jon@tourGuide.com");
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(),
                new Location(-44.437913, -34.185441), new Date());
        Attraction attraction = new Attraction("Disneyland", "Anaheim", "CA", 33.817595, -117.922008);
        UserReward userReward = new UserReward(visitedLocation, attraction, 100);
        List<UserReward> userRewards = new ArrayList<>();
        userRewards.add(userReward);
        when(userRepositoryMock.getUserByUserName("username")).thenReturn(user);
        when(userRepositoryMock.save(user)).thenReturn(user);
        //when
        userServiceUnderTest.addUserRewardList("username", userRewards);
        //then
        verify(userRepositoryMock, times(1)).getUserByUserName("username");
        verify(userRepositoryMock, times(1)).save(user);

    }

}
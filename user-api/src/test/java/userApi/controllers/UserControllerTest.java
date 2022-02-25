package userApi.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import userApi.dto.AttractionDto;
import userApi.dto.UserDto;
import userApi.dto.VisitedLocationDto;
import userApi.model.User;
import userApi.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    UserService userServiceMock;

    UserController userControllerUnderTest;

    @BeforeEach
    void setUp() {
        userControllerUnderTest = new UserController(userServiceMock);
    }

    @Test
    void getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        when(userServiceMock.getAllUsers()).thenReturn(allUsers);
        userControllerUnderTest.getAllUsers("timeStamp");
        verify(userServiceMock, times(1)).getAllUsers();
    }

    @Test
    void getUserByUserName() {
        when(userServiceMock.getUser("userId")).thenReturn(new User());
        userControllerUnderTest.getUserByUserName("userId", "timeStamp");
        verify(userServiceMock, times(1)).getUser("userId");
    }

    @Test
    void addUser() {
        when(userServiceMock.addUser(any())).thenReturn(new User());
        userControllerUnderTest.addUser("timeStamp", new UserDto());
        verify(userServiceMock, times(1)).addUser(any());
    }

    @Test
    void getUserRewardsById() {
        when(userServiceMock.getUserRewards(any())).thenReturn(new ArrayList<>());
        userControllerUnderTest.getUserRewardsById("username", "timeStamp");
        verify(userServiceMock, times(1)).getUserRewards(any());
    }

    @Test
    void setTripDeals() {
        doNothing().when(userServiceMock).setTripDeals(eq("username"), any());
        userControllerUnderTest.setTripDeals("username", "timeStamp", new ArrayList<>());
        verify(userServiceMock, times(1)).setTripDeals(eq("username"), any());
    }

    @Test
    void addToVisitedLocations() {
        doNothing().when(userServiceMock).addToVisitedLocations(eq("username"), any());
        userControllerUnderTest.addToVisitedLocations("timeStamp", "username", "timeStamp", new VisitedLocationDto());
        verify(userServiceMock, times(1)).addToVisitedLocations(eq("username"), any());
    }

    @Test
    void addUserRewardList() {
        when(userServiceMock.addUserRewardList("username", new ArrayList<>())).thenReturn(new ArrayList<>());
        userControllerUnderTest.addUserRewardList("timeStamp", "username", new ArrayList<>());
        verify(userServiceMock, times(1)).addUserRewardList("username", new ArrayList<>());
    }

    @Test
    void initUser() {
        doNothing().when(userServiceMock).initializeInternalUsers(100);
        userControllerUnderTest.initUser("timeStamp", 100);
        verify(userServiceMock, times(1)).initializeInternalUsers(100);

    }

    @Test
    void addFirstAttractionForAllUser() {
        doNothing().when(userServiceMock).addVisitedLocationForTest(any());
        userControllerUnderTest.addFirstAttractionForAllUser("timeStamp", new AttractionDto());
        verify(userServiceMock, times(1)).addVisitedLocationForTest(any());
    }
}
package gpsApi.controller;

import gpsApi.service.GpsService;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GpsControllerTest {

    @Mock
    GpsService gpsServiceMock;

    GpsController gpsControllerUnderTest;

    @BeforeEach
    void setUp() {
        gpsControllerUnderTest = new GpsController(gpsServiceMock);
    }

    @Test
    void getAllAttractions() {
        when(gpsServiceMock.getAttractions()).thenReturn(new ArrayList<>());
        gpsControllerUnderTest.getAllAttractions("TimeStamp");
        verify(gpsServiceMock, times(1)).getAttractions();
    }

    @Test
    void getUserLocation() {
        when(gpsServiceMock.getUserLocation(anyString())).thenReturn(null);
        gpsControllerUnderTest.getUserLocation("TimeStump", "userId");
        verify(gpsServiceMock, times(1)).getUserLocation(anyString());
    }
}
package gpsApi.service;

import gpsUtil.GpsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GpsServiceTest {

    @Mock
    GpsUtil gpsUtilMock;

    GpsService gpsServiceUnderTest;

    @BeforeEach
    void setUp() {
        gpsServiceUnderTest = new GpsService(gpsUtilMock);
    }

    @Test
    void getAttractions() {
        when(gpsUtilMock.getAttractions()).thenReturn(new ArrayList<>());
        gpsServiceUnderTest.getAttractions();
        verify(gpsUtilMock, times(1)).getAttractions();
    }

    @Test
    void getUserLocation() {
        when(gpsUtilMock.getUserLocation(any())).thenReturn(null);
        gpsServiceUnderTest.getUserLocation("76b77115-7e4d-4bdc-ae9f-b370fc35b9e9");
        verify(gpsUtilMock, times(1)).getUserLocation(any());
    }
}
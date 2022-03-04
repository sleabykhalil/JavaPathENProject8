package rewardApi.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rewardApi.service.RewardService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardControllerTest {
    @Mock
    RewardService rewardServiceMock;

    RewardController rewardControllerUnderTest;

    @BeforeEach
    void setUp() {
        rewardControllerUnderTest = new RewardController(rewardServiceMock);
    }

    @Test
    void getRewardPoints() {
        when(rewardServiceMock.getRewardPoints(anyString(), anyString())).thenReturn(100);
        rewardControllerUnderTest.getRewardPoints("timeStamp", "userId", "attractionId");
        verify(rewardServiceMock, times(1)).getRewardPoints("userId", "attractionId");
    }
}
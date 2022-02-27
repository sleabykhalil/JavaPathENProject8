package rewardApi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rewardCentral.RewardCentral;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {
@Mock
RewardCentral rewardsCentralMock;

RewardService rewardServiceUnderTest;
    @BeforeEach
    void setUp() {
        rewardServiceUnderTest = new RewardService(rewardsCentralMock);
    }

    @Test
    void getRewardPoints() {
        when(rewardsCentralMock.getAttractionRewardPoints(any(),any())).thenReturn(100);
        rewardServiceUnderTest.getRewardPoints("76b77115-7e4d-4bdc-ae9f-b370fc35b9e9","76b77115-7e4d-4bdc-ae9f-b370fc35b9e9");
        verify(rewardsCentralMock,times(1)).getAttractionRewardPoints(any(),any());
    }
}
package gpsApi.model;

import gpsUtil.GpsUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rewardCentral.RewardCentral;

@Configuration
public class BeanModule {
    @Bean
    public GpsUtil getGpsUtil() {
        return new GpsUtil();
    }

    @Bean
    public RewardCentral getRewardCentral() {
        return new RewardCentral();
    }
}

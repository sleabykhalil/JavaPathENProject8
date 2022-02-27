package rewardApi.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rewardCentral.RewardCentral;

@Configuration
public class BeanModule {

    @Bean
    public RewardCentral getRewardCentral() {
        return new RewardCentral();
    }
}

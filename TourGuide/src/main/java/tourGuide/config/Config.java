package tourGuide.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.jackson.datatype.money.MoneyModule;

@Configuration
public class Config {

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new MoneyModule());
        //     .withMonetaryAmount(FastMoney::of));
        return mapper;
    }
}

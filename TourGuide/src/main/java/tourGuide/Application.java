package tourGuide;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.zalando.jackson.datatype.money.MoneyModule;

import java.util.Locale;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"tourGuide", "tourGuide.feign"})
public class Application {

    public static void main(String[] args) {
        Locale.setDefault(new Locale("en", "US"));
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//
//        ObjectMapper mapper = new ObjectMapper()
//                .registerModule(new MoneyModule());
//        //     .withMonetaryAmount(FastMoney::of));
//        return mapper;
//    }

//    @Bean
//    public MoneyModule moneyModule() {
//        return new MoneyModule();
//    }
}

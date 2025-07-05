package ir.yasinzadeh.logprompt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Mahdi Yasinzadeh
 * @since 7/3/25
 */
@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient webClient(){
        return WebClient.builder().build();
    }
}

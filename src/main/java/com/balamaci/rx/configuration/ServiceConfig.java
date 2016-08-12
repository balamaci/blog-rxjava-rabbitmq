package com.balamaci.rx.configuration;

import com.balamaci.rx.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sbalamaci
 */
@Configuration
public class ServiceConfig {

    @Bean
    public UserService userService() {
        return new UserService();
    }


}

package com.balamaci.rx.observable.login;

import com.balamaci.rx.service.UserService;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import rx.Observable;
import static com.balamaci.rx.util.Json.*;

/**
 * @author sbalamaci
 */
public class LoginObservables {

    @Autowired
    @Qualifier("events")
    protected Observable<JsonObject> events;

    @Autowired
    protected UserService userService;


    @Bean
    Observable<JsonObject> loginEvents() {
        return events
                .filter(checkProperty("logger_name", val -> val.contains("LoginEvent")));
    }

    @Bean
    Observable<JsonObject> failedLogins() {
        return loginEvents()
                .filter(checkProperty("message", val -> val.startsWith("FAILED login")));
    }

    @Bean
    Observable<JsonObject> succesfullLogins() {
        return loginEvents()
                .filter(checkProperty("message", val -> val.startsWith("SUCCESS login")));
    }


}

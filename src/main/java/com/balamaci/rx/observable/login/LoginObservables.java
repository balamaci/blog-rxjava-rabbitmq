package com.balamaci.rx.observable.login;

import com.balamaci.rx.service.UserService;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import rx.Observable;
import rx.schedulers.Schedulers;

import static com.balamaci.rx.util.Json.checkPropertyFunc;

/**
 * @author sbalamaci
 */
public class LoginObservables {

    private static final Logger log = LoggerFactory.getLogger(LoginObservables.class);

    @Autowired
    @Qualifier("events")
    protected Observable<JsonObject> events;

    @Autowired
    protected UserService userService;


    @Bean
    Observable<JsonObject> loginEvents() {
        return events
                .observeOn(Schedulers.io())
                .filter(checkPropertyFunc("logger_name", val -> val.contains("LoginEvent")));
    }

    @Bean
    Observable<JsonObject> failedLogins() {
        return loginEvents()
                .filter(checkPropertyFunc("message", val -> val.startsWith("FAILED login")));
    }

    @Bean
    Observable<JsonObject> succesfullLogins() {
        return loginEvents()
                .filter(checkPropertyFunc("message", val -> val.startsWith("SUCCESS login")));
    }


}

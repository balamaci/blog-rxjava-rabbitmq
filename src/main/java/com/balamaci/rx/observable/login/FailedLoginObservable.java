package com.balamaci.rx.observable.login;

import com.balamaci.rx.util.Json;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rx.Observable;
import rx.Subscription;

import java.util.concurrent.TimeUnit;


/**
 * @author sbalamaci
 */
@Configuration
public class FailedLoginObservable extends LoginObservables {

    private static final Logger log = LoggerFactory.getLogger(FailedLoginObservable.class);

    @Value("${min.failed.login.attempts}")
    private Integer minFailedLoginAttempts;

    Observable<Pair<String, Integer>> failedLoginsPerWindow(int windowSecs) {
        return failedLogins()
//                .doOnNext(jsonObject -> System.out.println(jsonObject.toString()))
                .window(windowSecs, TimeUnit.SECONDS)
                .flatMap(window -> window
                            .groupBy(Json.propertyStringValueFunc("remoteIP"))
                            .flatMap(groupEventForIP -> groupEventForIP
                                .count()
                                .map(failedLoginsCount -> {
                                    final String remoteIp = groupEventForIP.getKey();
                                    return new Pair<>(remoteIp, failedLoginsCount);
                                }))
                );
    }

    @Bean
    public Subscription failedLoginsPerIpSubscription() {
        return failedLoginsPerWindow(10)
                .filter(failedLoginPair -> failedLoginPair.getValue() > minFailedLoginAttempts)
                .subscribe((failedLoginPair) ->log.info("Possible brute force login: {}", failedLoginPair));
    }
}

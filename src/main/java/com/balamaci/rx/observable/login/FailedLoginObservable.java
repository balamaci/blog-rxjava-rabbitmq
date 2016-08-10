package com.balamaci.rx.observable.login;

import javafx.util.Pair;
import org.springframework.context.annotation.Configuration;
import rx.Observable;

import java.util.concurrent.TimeUnit;

import static com.balamaci.rx.util.Json.propertyStringValue;

/**
 * @author sbalamaci
 */
@Configuration
public class FailedLoginObservable extends LoginObservables {

    Observable<Pair<String, Integer>> failedLoginsPerWindow(int timespan) {
        return failedLogins()
//                .doOnNext(jsonObject -> System.out.println(jsonObject.toString()))
                .window(timespan, 5, TimeUnit.SECONDS)
                .flatMap(window -> window
                        .groupBy(propertyStringValue("remoteIP"))
                        .flatMap(grouped -> grouped
                                .count()
                                .map(failedLoginsCount -> {
                                    final String remoteIp = grouped.getKey();
                                    return new Pair<>(remoteIp, failedLoginsCount);
                                }))
                );
    }


}

package com.balamaci.rx.observable.login;

import com.balamaci.rx.domain.UserLocationRating;
import com.balamaci.rx.domain.UserScoring;
import com.balamaci.rx.util.Json;
import javafx.util.Pair;
import javaslang.Tuple3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;


/**
 * @author sbalamaci
 */
@Configuration
public class SuccessfulLoginObservable extends LoginObservables {

    private static final Logger log = LoggerFactory.getLogger(SuccessfulLoginObservable.class);

    private Observable<Tuple3<String, UserScoring, UserLocationRating>> userScoringAndLocationRatingObservable(String username, String ip) {
        Observable<UserScoring> scoringObservable = userService.retrieveScoring(username);
        Observable<UserLocationRating> locationObservable = userService.retrieveUserLocationBasedOnIp(username, ip);

        Observable<Tuple3<String, UserScoring, UserLocationRating>> scoringAndLocationObservable =
                scoringObservable
                        .zipWith(locationObservable, (scoring, location) -> new Tuple3<>(username, scoring, location));

        return scoringAndLocationObservable;
    }

    private Observable<Tuple3<String, UserScoring, UserLocationRating>> userScoringAndLocationForSuccessfulLogins() {
        return succesfullLogins()
                .map(jsonObject ->
                        new Pair<>(new Json(jsonObject).propertyStringValue("userName"),
                                new Json(jsonObject).propertyStringValue("remoteIP")))
                .flatMap(pair -> userScoringAndLocationRatingObservable(pair.getKey(), pair.getValue()))

                .doOnNext(scoringUserLocationPair -> System.out.println("UserLocation " + scoringUserLocationPair._1()));
    }

    @Bean
    Subscription userScoringAndLocationSubscription() {
        return userScoringAndLocationForSuccessfulLogins()
                .subscribeOn(Schedulers.newThread())
                .subscribe(userScoringAndLocation -> log.info("***** Got {} ****", userScoringAndLocation));
    }


}
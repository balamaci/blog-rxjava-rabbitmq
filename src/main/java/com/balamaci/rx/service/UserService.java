package com.balamaci.rx.service;

import com.balamaci.rx.domain.UserLocationRating;
import com.balamaci.rx.domain.UserCreditScoring;
import com.balamaci.rx.util.Json;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.util.Pair;
import rx.Observable;

/**
 * @author Serban Balamaci
 */
public class UserService {

    private Observable<Pair<String, UserCreditScoring>> userScoringObservable;

    public UserService() {
        userScoringObservable = loadHardcodedUserObservableFromFile()
                                    .cache();
    }

    public Observable<UserCreditScoring> retrieveScoring(String username) {
/*
        CompletableFuture<UserCreditScoring> result = CompletableFuture.supplyAsync(()-> {
            periodicSleep(200);
        });
        return CompletableFutures.<UserCreditScoring>observe(result);
*/

        return userScoringObservable
                .filter(userScorePair -> userScorePair.getKey().equals(username))
//                .delay(1, TimeUnit.SECONDS)
                .map(Pair::getValue)
                .singleOrDefault(UserCreditScoring.NONE);
    }

    public Observable<UserLocationRating> retrieveUserLocationBasedOnIp(String username, String ip) {
        return Observable.create(subscriber -> {

            subscriber.onNext(UserLocationRating.SAFE);
            subscriber.onCompleted();
        });
    }

    private Observable<Pair<String, UserCreditScoring>> loadHardcodedUserObservableFromFile() {
        return Observable.<Pair<String, UserCreditScoring>>create(subscriber -> {
            JsonArray userScoreArray = Json.readJsonArrayFromFile("scoring.json");
            userScoreArray.forEach((jsonElement) -> {
                JsonObject scoringJson = (JsonObject) jsonElement;
                String username = scoringJson.get("username").getAsString();
                String scoring = scoringJson.get("scoring").getAsString();

                subscriber.onNext(new Pair<>(username, UserCreditScoring.fromString(scoring)));
            });
            subscriber.onCompleted();
        });
    }

}

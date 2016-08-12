package com.balamaci.rx.service;

import com.balamaci.rx.domain.UserLocationRating;
import com.balamaci.rx.domain.UserScoring;
import com.balamaci.rx.util.Json;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.util.Pair;
import rx.Observable;

/**
 * @author Serban Balamaci
 */
public class UserService {

    private Observable<Pair<String, UserScoring>> userScoringObservable;

    public UserService() {
        userScoringObservable = loadHardcodedUserObservableFromFile()
                                    .cache();
    }

    public Observable<UserScoring> retrieveScoring(String username) {
/*
        CompletableFuture<UserScoring> result = CompletableFuture.supplyAsync(()-> {
            periodicSleep(200);
        });
        return CompletableFutures.<UserScoring>observe(result);
*/

        return userScoringObservable
                .filter(userScorePair -> userScorePair.getKey().equals(username))
//                .delay(1, TimeUnit.SECONDS)
                .map(Pair::getValue)
                .singleOrDefault(UserScoring.LOWEST);
    }

    public Observable<UserLocationRating> retrieveUserLocationBasedOnIp(String username, String ip) {
        return Observable.create(subscriber -> {

            subscriber.onNext(UserLocationRating.SAFE);
            subscriber.onCompleted();
        });
    }

    private Observable<Pair<String, UserScoring>> loadHardcodedUserObservableFromFile() {
        return Observable.<Pair<String, UserScoring>>create(subscriber -> {
            JsonArray userScoreArray = Json.readJsonArrayFromFile("scoring.json");
            userScoreArray.forEach((jsonElement) -> {
                JsonObject scoringJson = (JsonObject) jsonElement;
                String username = scoringJson.get("username").getAsString();
                String scoring = scoringJson.get("scoring").getAsString();

                subscriber.onNext(new Pair<>(username, UserScoring.fromString(scoring)));
            });
            subscriber.onCompleted();
        });
    }

}

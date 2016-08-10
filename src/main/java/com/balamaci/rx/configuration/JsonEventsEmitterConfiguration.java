package com.balamaci.rx.configuration;

import com.balamaci.rx.Receiver;
import com.balamaci.rx.util.Json;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import rx.Observable;
import rx.Observer;
import rx.subjects.PublishSubject;

import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * @author sbalamaci
 */
@Configuration
@Profile("hardcoded-events")
public class JsonEventsEmitterConfiguration {

    private static final Logger log = LoggerFactory.getLogger(JsonEventsEmitterConfiguration.class);

    @Bean
    Receiver receiver() {
        return new Receiver();
    }


    @Bean(name = "events")
    public Observable<JsonObject> emitEvents() {
        startEmitting();

        return receiver().getPublishSubject();
    }

    private void startEmitting() {
        PublishSubject<JsonObject> publishSubject = receiver().getPublishSubject();

        Executors.newSingleThreadExecutor().submit(() -> produceEventsFromJson(publishSubject, () -> 200));
    }

/*
    @Bean(name = "events")
    public Observable<JsonObject> emitEventsColdSubscriber() {
        return Observable.<JsonObject>create(this::produceEventsFromJson);
    }
*/

    private void produceEventsFromJson(Observer<JsonObject> subscriber, Supplier<Integer> waitTimeMillis) {
        JsonArray events = Json.readJsonArray("events.json");
        events.forEach(ev -> {
            sleepMillis(waitTimeMillis.get());

            JsonObject jsonObject = (JsonObject) ev;
            log.info("Emitting {}", Json.propertyStringValue("message").call(jsonObject));

            subscriber.onNext(jsonObject);
        });
    }

    private void sleepMillis(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("Interrupted Thread");
            throw new RuntimeException("Interrupted thread");
        }
    }

}

package com.balamaci.rx.observable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author sbalamaci
 */
@Configuration
@Profile("write-events-to-file")
public class EventsFileWritter {

    @Autowired
    @Qualifier("events")
    protected Observable<JsonObject> events;

    @Bean
    Subscription writeEvents() {
        return events
                .buffer(1000)
                .subscribeOn(Schedulers.newThread())
                .subscribe(events -> {
                    try {
                        FileWriter file = new FileWriter("events.json");
                        Gson gson = new GsonBuilder()
                                .setPrettyPrinting()
                                .create();
                        JsonArray jsonArray = new JsonArray();
                        for(JsonObject jsonObject : events) {
                            jsonArray.add(jsonObject);
                        }

                        gson.toJson(jsonArray, file);
                        file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

}

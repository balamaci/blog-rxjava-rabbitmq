package com.balamaci.rx;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.subjects.PublishSubject;

/**
 * @author Serban Balamaci
 */
public class Receiver {

    private static final Logger log = LoggerFactory.getLogger(Receiver.class);

    private PublishSubject<JsonObject> publishSubject = PublishSubject.create();

    private Gson gson = new Gson();

    public Receiver() {
    }

    /**
     * Method invoked by Spring whenever a new message arrives
     * @param message amqp message
     */
    public void receive(Object message) {
        log.info("Received remote message {}", message);
        JsonElement remoteJsonElement = gson.fromJson ((String) message, JsonElement.class);
        JsonObject jsonObj = remoteJsonElement.getAsJsonObject();

        publishSubject.onNext(jsonObj);
    }

    public PublishSubject<JsonObject> getPublishSubject() {
        return publishSubject;
    }
}

package com.balamaci.rx;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * @author Serban Balamaci
 */
public class Receiver implements ApplicationListener<ContextClosedEvent> {

    private static final Logger log = LoggerFactory.getLogger(Receiver.class);

    /**
     * Serialized since the Receiver can be shared by multiple threads in the ListenerContainer
     * and also the main thread when Spring context closes
     */
    private final SerializedSubject<JsonObject, JsonObject> serializedSubjectPublishSubject = PublishSubject
            .<JsonObject>create()
            .toSerialized();

    private final Gson gson = new Gson();

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
        serializedSubjectPublishSubject.onNext(jsonObj);
    }

    public SerializedSubject<JsonObject, JsonObject> getSubject() {
        return serializedSubjectPublishSubject;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        serializedSubjectPublishSubject.onCompleted();
    }
}

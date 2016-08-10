package com.balamaci.rx;

import com.balamaci.rx.configuration.AmqpSourceConfiguration;
import com.balamaci.rx.configuration.AppConfig;
import com.balamaci.rx.configuration.JsonEventsEmitterConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Serban Balamaci
 */
public class Start {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setDisplayName("RxJava");


        context.getEnvironment().setActiveProfiles("hardcoded-events");
//        context.getEnvironment().addActiveProfile("write-events-to-file");

        context.register(AppConfig.class);
        context.register(AmqpSourceConfiguration.class);
        context.register(JsonEventsEmitterConfiguration.class);


        context.scan("com.balamaci.rx.observable");
        context.refresh();
        context.start();

//        Thread.sleep(1000000);
    }

}

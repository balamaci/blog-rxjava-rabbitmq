package com.balamaci.rx;

import com.balamaci.rx.configuration.AmqpSourceEmitterConfiguration;
import com.balamaci.rx.configuration.AppConfig;
import com.balamaci.rx.configuration.JsonFileEmitterSourceConfiguration;
import com.balamaci.rx.configuration.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sun.security.tools.keytool.Main;

/**
 * @author Serban Balamaci
 */
public class Start {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setDisplayName("RxJava");


        context.getEnvironment().setActiveProfiles("hardcoded-events");
//        context.getEnvironment().addActiveProfile("write-events-to-file");

        context.register(AppConfig.class);
        context.register(AmqpSourceEmitterConfiguration.class);
        context.register(JsonFileEmitterSourceConfiguration.class);
        context.register(ServiceConfig.class);


        context.scan("com.balamaci.rx.observable");
        context.refresh();

        log.info("Starting up...");
        context.start();
    }

}

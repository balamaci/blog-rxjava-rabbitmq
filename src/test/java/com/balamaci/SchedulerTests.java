package com.balamaci;

import com.balamaci.rx.util.SleepUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by sbalamaci on 04.09.2016.
 */
public class SchedulerTests {

    private static final Logger log = LoggerFactory.getLogger(SchedulerTests.class);

    @Test
    public void testSubscribeOn() {
        log.info("Starting");
        Observable<Integer> observable = Observable
                .create(subscriber -> {
                    log.info("Started emitting");
                    subscriber.onNext(1);

//                    log.info("Emitting 2nd");
//                    subscriber.onNext(2);

                    subscriber.onCompleted();
                });

        observable
                .subscribeOn(Schedulers.computation())
                .map(val -> {
                    int newValue = val * 2;
                    log.info("Mapping new val {}", newValue);
                    return newValue;
                })
//                .toBlocking()
                .subscribe(val -> log.info("Subscribe received " + val));

        SleepUtil.sleepMillis(3000);

    }

    @Test
    public void testObserveOn() {

    }
}

package com.balamaci;

import com.balamaci.rx.util.SleepUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by sbalamaci
 */
public class SchedulerTests {

    private static final Logger log = LoggerFactory.getLogger(SchedulerTests.class);

    @Test
    public void testSubscribeOn() {
        log.info("Starting");

        simpleObservable()
                .subscribeOn(Schedulers.computation())
                .map(val -> {
                    int newValue = val * 2;
                    log.info("Mapping new val {}", newValue);
                    return newValue;
                })
//                .toBlocking()
                .subscribe(val -> log.info("Subscribe received " + val));

        SleepUtil.sleepMillis(2000);
    }

    @Test
    public void testObserveOn() {
        simpleObservable()
                .observeOn(Schedulers.computation())
                .map(val -> {
                    int newValue = val * 2;
                    log.info("First mapping new val {}", newValue);
                    return newValue;
                })
                .observeOn(Schedulers.io())
                .map(val -> {
                    String newValue = "*" + val + "*";
                    log.info("Second mapping new val {}", newValue);
                    return newValue;
                })
                .subscribe(val -> log.info("Subscribe received " + val));
        SleepUtil.sleepMillis(2000);
    }

    private Observable<Integer> simpleObservable() {
        return Observable
                .create(subscriber -> {
                    log.info("Started emitting");

                    log.info("Emitting 1st");
                    subscriber.onNext(1);

                    log.info("Emitting 2nd");
                    subscriber.onNext(2);

                    subscriber.onCompleted();
                });
    }

}

package com.balamaci.rx.util;

import rx.Observable;

import java.util.concurrent.CompletableFuture;

/**
 * @author Serban Balamaci
 */
public class RxUtil {

    /**
     * Transforms a CompletableFuture into an Observable
     * @param future CompletableFuture
     * @param <T>
     * @return Observable to type T
     */
    public static <T> Observable<T> observeCompletableFuture(CompletableFuture<T> future) {
        return Observable.create(subscriber -> {
            future.whenComplete((value, exception) -> {
                if (exception != null) {
                    subscriber.onError(exception);
                } else {
                    subscriber.onNext(value);
                    subscriber.onCompleted();
                }
            });
        });
    }
}

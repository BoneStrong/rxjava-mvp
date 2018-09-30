package com.dzz.operator;

import com.dzz.model.Observable;
import com.dzz.model.Subscriber;

/**
 * @author zoufeng
 * @date 2018/9/25
 */
public class MergeSubscriber<T> extends Subscriber<Observable<? extends T>> {

    private Subscriber<Observable<? super T>> actual;

    private Subscriber<? super T> subscriber;

    public MergeSubscriber(Subscriber<? super T> subscriber) {
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onNext(Observable<? extends T> var1) {
//        subscriber.onNext(var1.getValue());
    }

}

package com.dzz.operator;

import com.dzz.model.Observable;
import com.dzz.model.Observer;
import com.dzz.model.OnSubscribe;
import com.dzz.model.Subscriber;

/**
 * @author zoufeng
 * @date 2018/9/22
 */
public class OperatorWindow<T> extends Subscriber<T> {

    private Subscriber<Observable<T>> subscriber;
    private int index = 0;
    private int size;
    private WindownSubject<T> windown;

    public OperatorWindow(Subscriber<Observable<T>> subscriber, int size) {
        this.subscriber = subscriber;
        this.size = size;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onNext(T var1) {
        if (index == 0) {
            windown = new WindownSubject<T>(new OnSubscribe<T>() {
                @Override
                public void call(Subscriber<? super T> subscriber) {
                    //do nothing
                }
            });
            subscriber.onNext(windown);
        }
        index++;
        if (index == size) {
            index = 0;
        }
        windown.onNext(var1);
    }

    class WindownSubject<T> extends Observable<T> implements Observer<T> {

        Subscriber subscriber;

        @Override
        public void subscribe(Subscriber subscriber) {
            this.subscriber = subscriber;
        }

        public WindownSubject(OnSubscribe<T> onSubscribe) {
            super(onSubscribe);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onNext(T var1) {
            subscriber.onNext(var1);
        }
    }
}


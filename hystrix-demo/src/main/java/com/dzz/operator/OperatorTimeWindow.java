package com.dzz.operator;

import com.dzz.model.Observable;
import com.dzz.model.Subscriber;

import java.util.concurrent.TimeUnit;

/**
 * @author zoufeng
 * @date 2018/9/22
 */
public class OperatorTimeWindow <T> extends Subscriber<T>{

    public  OperatorTimeWindow(Subscriber<? super Observable<T>> subscriber, int time, TimeUnit timeUnit) {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onNext(T var1) {

    }
}

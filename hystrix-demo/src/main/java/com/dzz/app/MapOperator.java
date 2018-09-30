package com.dzz.app;

import com.dzz.model.Observable;
import com.dzz.model.OnSubscribe;
import com.dzz.model.Subscriber;
import com.dzz.model.Transformer;
import org.junit.jupiter.api.Test;

/**
 * create by zoufeng on 2018/4/27
 */
public class MapOperator {

    public static void main(String[] args) {

        Observable.create(new OnSubscribe<Integer>() {
            //原始订阅者
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                }
            }
        }).map(new Transformer<Integer, String>() {
            @Override
            public String call(Integer from) {
                return from + "--map";
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onNext(String var1) {
                System.out.println(var1);
            }
        });

    }

    @Test
    public void map2() {
        Observable<Integer> integerObservable = Observable.create(new OnSubscribe<Integer>() {
            //原始订阅者
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                }
            }
        });
        Observable<String> map = integerObservable.map(new Transformer<Integer, String>() {
            @Override
            public String call(Integer from) {
                return from + "--map";
            }
        });
        map.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onNext(String var1) {
                System.out.println(var1);
            }
        });

    }
}

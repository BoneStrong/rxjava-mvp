package com.dzz.app;

import com.dzz.model.Observable;
import com.dzz.model.OnSubscribe;
import com.dzz.model.Subscriber;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * create by zoufeng on 2018/4/27
 */
public class RxjavaApp {

    public static void main(String[] args) {

        Observable.create(new OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                }
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onNext(Integer var1) {
                System.out.println(var1);
            }
        });
    }

    @Test
    public void range() {
        Observable.range(1, 10).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onNext(Integer var1) {
                System.out.println(var1);
            }
        });
    }
    @Test
    public void windown(){
        Observable.range(1,10).windown(5).subscribe(new Subscriber<Observable<Integer>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onNext(Observable<Integer> var1) {
                System.out.println("---------");
                var1.subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onNext(Integer var1) {
                        System.out.println(var1);
                    }
                });
            }
        });
    }

    @Test
    public void bool(){
        System.out.println(Boolean.getBoolean("true"));
        System.out.println(Boolean.parseBoolean("true"));
        System.out.println(Boolean.parseBoolean("hehe"));
    }




}

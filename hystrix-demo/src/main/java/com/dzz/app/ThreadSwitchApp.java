package com.dzz.app;

import com.dzz.model.Observable;
import com.dzz.model.OnSubscribe;
import com.dzz.model.Subscriber;
import com.dzz.task.ScheduleUtils;
import com.dzz.task.Schedulers;

/**
 * create by zoufeng on 2018/4/28
 */
public class ThreadSwitchApp {

    public static void main(String[] args) {
//        subscribeOn();
//        subscribeOns();
//        observaberOn();
//        observaberOns();
        doOnSubscribe();

    }

    /**
     * 一次调用
     */
    public static void subscribeOn() {

        Observable.create(new OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                System.out.println(Thread.currentThread().getName() + "==============");
                subscriber.onNext(666);
            }
            //设置中间层，将订阅源任务发送至线程池
        }).subscribeOn(Schedulers.scheduler())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onNext(Integer var1) {
                        System.out.println(Thread.currentThread().getName() + "==========" + var1);
                    }
                });
    }

    /**
     * 多次调用subscribeOn
     */
    public static void subscribeOns() {
        //main Thread
        System.out.println(Thread.currentThread().getName());
        Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hehe");
            }
        }).subscribeOn(ScheduleUtils.newInstance("A"))
                .subscribeOn(ScheduleUtils.newInstance("B"))
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onNext(String var1) {
                        System.out.println(Thread.currentThread().getName() + var1);
                    }

                });
    }

    /**
     * 一次调用observaberOn
     */
    private static void observaberOn() {
        Observable.create(new OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                System.out.println(Thread.currentThread().getName());
                subscriber.onNext(777);
            }
            //设置中间层，将下层订阅任务交给线程池
        }).observeOn(ScheduleUtils.newInstance("A"))
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onNext(Integer var1) {
                        System.out.println(Thread.currentThread().getName() + "==========" + var1);
                    }
                });
    }

    /**
     * 多次调用observaberOn
     */
    private static void observaberOns() {

        Observable.create(new OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                System.out.println(Thread.currentThread().getName());
                subscriber.onNext(777);
            }
            //设置中间层，将下层订阅任务交给线程池
        }).observeOn(ScheduleUtils.newInstance("A"))
                .observeOn(ScheduleUtils.newInstance("B"))
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onNext(Integer var1) {
                        System.out.println(Thread.currentThread().getName() + "==========" + var1);
                    }
                });
    }


    private static void doOnSubscribe() {
        Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.out.println("源call : " + Thread.currentThread().getName());
                subscriber.onNext("hehe");
            }
        }).subscribeOn(ScheduleUtils.newInstance("A"))
                .doOnSubscribe(new Observable.Action0() {
                    @Override
                    public void call() {
                        System.out.println("action is : " + Thread.currentThread().getName());
                    }
                })
                .subscribeOn(ScheduleUtils.newInstance("B")).observeOn(ScheduleUtils.newInstance("C"))
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onNext(String var1) {
                        System.out.println("subscrible invoke is " + var1 + " : " + Thread.currentThread().getName());
                    }
                });
    }

}

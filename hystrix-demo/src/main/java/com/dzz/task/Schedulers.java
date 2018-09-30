package com.dzz.task;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * create by zoufeng on 2018/4/28
 * <p>
 * 任务工厂
 */
public class Schedulers {

    private Schedulers() {
    }

    //单线程池的任务调度
    private static Scheduler scheduler = new Scheduler(new ThreadPoolExecutor(1, 1, 20
            , TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10)));

    public static Scheduler scheduler() {
        return scheduler;
    }

    public static Scheduler newThread() {
        return new Scheduler(new ThreadPoolExecutor(1, 1, 20
                , TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10)));
    }
}

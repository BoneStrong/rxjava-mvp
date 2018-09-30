package com.dzz.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zoufeng
 * @date 2018/9/11
 */
public class ScheduleUtils {

    private ScheduleUtils() {
    }

    public static Scheduler newInstance(String threadPoolName) {
        return new Scheduler(new ThreadPoolExecutor(1, 1, 20
                , TimeUnit.SECONDS
                , new ArrayBlockingQueue<Runnable>(10)
                , new ThreadFactoryBuilder().setNameFormat(threadPoolName + "-pool-%d").build()
                , new ThreadPoolExecutor.AbortPolicy()));
    }
}

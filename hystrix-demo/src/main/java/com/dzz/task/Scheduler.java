package com.dzz.task;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * create by zoufeng on 2018/4/28
 * <p>
 * 任务调度，创建任务执行者
 */
public class Scheduler {

    final Executor executor;

    public Scheduler(Executor executor) {
        this.executor = executor;
    }

    public Worker createWorker() {
        return new Worker(executor);
    }

    public static class Worker {
        final Executor executor;

        public Worker(Executor executor) {
            this.executor = executor;
        }

        public void schedule(Runnable runnable) {
            executor.execute(runnable);
        }

        public void schedule(Runnable runnable, long delay, TimeUnit unit) {
            executor.execute(runnable);
        }


    }

}

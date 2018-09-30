package com.dzz.model;

import com.dzz.operator.OperatorTimeWindow;
import com.dzz.operator.OperatorWindow;
import com.dzz.task.Scheduler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Iterator;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * create by zoufeng on 2018/4/27
 * <p>
 * 订阅源
 * 内部的onsubscribe相当于 订阅源--->订阅者的管道
 * 管道里面可以持有订阅者的引用
 *
 * @author zoufeng02
 */
public class Observable<T> {

    final OnSubscribe<T> onSubscribe;

    public Observable(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }

    public OnSubscribe<T> getOnSubscribe() {
        return onSubscribe;
    }


    public static <T> Observable<T> create(OnSubscribe<T> onSubscribe) {
        return new Observable<T>(onSubscribe);
    }

    public void subscribe(Subscriber<? super T> subscriber) {
        subscriber.onStart();
        try {
            onSubscribe.call(subscriber);
            subscriber.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            subscriber.onError(e);
        }
    }

    public interface Func1<T, R> {
        R call(T t);
    }

    public interface Operator<R, T> extends Func1<Subscriber<? super R>, Subscriber<? super T>> {
    }

    /**
     * 这里的泛型看起来好混乱，其实仔细想想就理清了
     * 生产的Observable是代理层，订阅的是目标类型的数据R
     * <p>
     * <p>
     * lift()是Observable一切操作的基础，原理其实很简单
     * 做了一层subscriber的代理而已，operator是生成这个代理的工具
     *
     * @param operator
     * @param <R>
     * @return
     */
    public <R> Observable<R> lift(Operator<? extends R, ? super T> operator) {
        return create(subscriber -> {
            //这里的精髓就是operator.call(),
            //subscriber --->最终订阅的对象
            // 表面看起来是将订阅的subscriber代理成观察源所需要订阅对象，
            // 其实内部这代理对象是讲源代理对象的值转化成了代理的值，内部隐藏了这一层操作
            //具体可以看map的实现，很典型的案例demo
            Observable.this.subscribe(operator.call(subscriber));
        });
    }

    public <R> Observable<R> map(final Transformer<? super T, ? extends R> transformer) {

        return create(new OnSubscribe<R>() {

            @Override
            public void call(final Subscriber<? super R> subscriber) {
                //代理层代理了订阅者，对输入源的值做了转换
                Observable.this.subscribe(new Subscriber<T>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onNext(T var1) {

                        subscriber.onNext(transformer.call(var1));
                    }
                });
            }
        });
    }

    public <R> Observable<R> map2(final Transformer<? super T, ? extends R> transformer) {

        return lift(new Operator<R, T>() {
            @Override
            public Subscriber<? super T> call(Subscriber<? super R> subscriber) {
                return new Subscriber<T>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onNext(T var1) {
                        subscriber.onNext(transformer.call(var1));
                    }
                };
            }
        });
    }

    public final <R> Observable<R> flatMap(Transformer<? super T, ? extends Observable<? extends R>> transformer) {
        return merge(map(transformer));
    }

    public <T> Observable<T> merge(Observable<? extends Observable<? extends T>> source) {

        return source.lift(new Operator<T, Observable<? extends T>>() {

            @Override
            public Subscriber<? super Observable<? extends T>> call(Subscriber<? super T> subscriber) {
                return new Subscriber<Observable<? extends T>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onNext(Observable<? extends T> var1) {
                        //这里是简单的顺序实现，其实算是concatMap实现了
                        // 因为flatMap是不保证顺序的，看源码是用队列来解耦生产消费，实现无阻塞的异步生产消费
                        //我们这里是简单的描述flatMap执行过程，就不引入这些了
                        var1.subscribe(subscriber);
                    }
                };
            }
        });
    }


    public final <R> Observable<R> flatMap2(Transformer<? super T, ? extends Observable<? extends R>> transformer) {
        return map(transformer).lift(new Operator<R, Observable<? extends R>>() {
            @Override
            public Subscriber<? super Observable<? extends R>> call(Subscriber<? super R> subscriber) {
                return new Subscriber<Observable<? extends R>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onNext(Observable<? extends R> var1) {
                        var1.subscribe(subscriber);
                    }
                };
            }
        });
    }


    /**
     * <p>
     * 下层通知上层，最终执行是上层
     * 上层的线程是固定的，多次调用也没辙
     *
     * @param scheduler
     * @return 将【订阅者管道】切换执行线程，
     * 【call过程（包含订阅者执行过程）】
     */
    public Observable<T> subscribeOn(final Scheduler scheduler) {
        return Observable.create(new OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                scheduler.createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        Observable.this.onSubscribe.call(subscriber);
                    }
                });
            }
        });
    }

    /**
     * 虽然超过一个的 subscribeOn() 对事件处理的流程没有影响，但在流程之前却是可以利用的。
     * doOnSubscribe()本质上是代理OnSubscrible.subscribe() ----> subscribe.call()中间这段过程的
     *
     * @param action0
     * @return s
     */
    public Observable<T> doOnSubscribe(Action0 action0) {
        return Observable.create(subscriber -> {
            action0.call();
            Observable.this.onSubscribe.call(subscriber);
        });
    }

    public interface Action0 {
        void call();
    }

    /**
     * 代理Observerable拿到原Observerable的引用，并让原Observerable订阅代理订阅者,
     * 代理订阅者将原订阅者的执行过程放入新的线程池
     * <p>
     * 将【订阅者】切换执行线程
     * <p>
     * 上层通知下层，最终执行是下层 ，下层线程可以随意切换
     * <p>
     * 最下层订阅subscribe,通知上一层的订阅代理Subscribe,
     * 直到顶层的Observable订阅第二层的代理Subscribe,
     * subscribe的执行顺序是上层的subscribe 调用下层的subscribe
     * 这样最下层的subscribe在最后设置的线程里执行
     */
    public Observable<T> observeOn(final Scheduler scheduler) {

        return Observable.create(new OnSubscribe<T>() {

            @Override
            public void call(final Subscriber<? super T> subscriber) {

                final Scheduler.Worker worker = scheduler.createWorker();

                Observable.this.subscribe(new Subscriber<T>("proxySubscriber" + System.nanoTime()) {

                    @Override
                    public void onCompleted() {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onCompleted();
                            }
                        });
                    }

                    @Override
                    public void onError(final Throwable t) {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onError(t);
                            }
                        });
                    }

                    @Override
                    public void onNext(final T var1) {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onNext(var1);
                            }
                        });
                    }
                });
            }
        });
    }

    public static Observable<Integer> range(int start, int end) {
        return create(new OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber subscriber) {
                for (int i = start; i <= end; i++) {
                    subscriber.onNext(i);
                }
            }
        });
    }

    public Observable<Observable<T>> windown(int size) {
        return lift(new Operator<Observable<T>, T>() {
            //两层包装，将subscriber转换成代理Observable WindownSubject,然后用代理Observaber再次订阅新的subscribe
            // （这里的关键就是将Observable的subscribe()和Subscribe的call()、OnNext()两步分开）
            @Override
            public Subscriber<? super T> call(Subscriber<? super Observable<T>> subscriber) {
                return new OperatorWindow(subscriber, size);
            }
        });
    }

    public Observable<Observable<T>> windownTime(int time, TimeUnit timeUnit) {
        return lift(new Operator<Observable<T>, T>() {
            //两层包装，将subscriber转换成代理Observable WindownSubject,然后用代理Observaber再次订阅新的subscribe
            // （这里的关键就是将Observable的subscribe()和Subscribe的call()、OnNext()两步分开）
            @Override
            public Subscriber<? super T> call(Subscriber<? super Observable<T>> subscriber) {
                return new OperatorTimeWindow<>(subscriber, time, timeUnit);
            }
        });
    }

    public static <T> Observable<T> from(Iterable<T> iterable) {
        return create(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                Iterator<T> iterator = iterable.iterator();
                while (iterator.hasNext()) {
                    subscriber.onNext(iterator.next());
                }
            }
        });
    }

    public final Observable<T> delay(long delay, TimeUnit unit) {
        return lift(new Operator<T, T>() {
            @Override
            public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
                ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10, new ThreadFactoryBuilder().setNameFormat("delay-pool-%d").build());
                return new Subscriber<T>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onNext(T var1) {
                        executor.schedule(() -> subscriber.onNext(var1), delay, unit);
                    }
                };
            }
        });
    }

    public interface Func2<T1, T2, R> {
        R call(T1 t1, T2 t2);
    }

    public Observable<T> reduce(Func2<T, T, T> reducer) {
        return create(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                Observable.this.subscribe(new Subscriber<T>() {

                    private T value;

                    @Override
                    public void onCompleted() {
                        subscriber.onNext(value);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onNext(T var1) {
                        if (value == null) {
                            value = var1;
                        } else {
                            value = reducer.call(value, var1);
                        }
                    }
                });
            }
        });
    }

    public Observable<T> reduce2(Func2<T, T, T> reducer) {
        return lift(new Operator<T, T>() {
            @Override
            public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
                return new Subscriber<T>() {

                    private T value;

                    @Override
                    public void onCompleted() {
                        subscriber.onNext(value);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onNext(T var1) {
                        if (value == null) {
                            value = var1;
                        } else {
                            value = reducer.call(value, var1);
                        }
                    }
                };
            }
        });
    }

    public final Observable<T> retry() {
        return create(new OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                Observable<T> tObservable = Observable.this;
                tObservable.subscribe(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {
                        //如果是repeat()方法，执行的retry动作在这里
                    }

                    @Override
                    public void onError(Throwable t) {
                        tObservable.subscribe(subscriber);
                    }

                    @Override
                    public void onNext(T var1) {
                        subscriber.onNext(var1);
                    }
                });
            }
        });
    }

    //retryWhen()主要是加了失败判断条件
    /*public final Observable<T> retry(Func2<Integer, Throwable, Boolean> predicate) {
        return lift(new OperatorRetryWithPredicate<T>(predicate));
    }

    public final Observable<T> retryWhen(final Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> notificationHandler) {
        return OnSubscribeRedo.<T>retry(this, InternalObservableUtils.createRetryDematerializer(notificationHandler));
    }*/
}

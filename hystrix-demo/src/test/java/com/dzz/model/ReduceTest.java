package com.dzz.model;

import org.junit.Test;

/**
 * @author zoufeng
 * @date 2018/9/26
 */
public class ReduceTest {

    @Test
    public void reduce() {
        Observable.range(1, 10).reduce((x, y) -> x + y).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }
        });

    }

    @Test
    public void reduce2() {
        Observable.range(1, 10).reduce2((x, y) -> x + y).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }
        });

    }
}

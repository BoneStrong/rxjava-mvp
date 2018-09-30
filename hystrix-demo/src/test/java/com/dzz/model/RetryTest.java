package com.dzz.model;

import org.junit.Test;

/**
 * @author zoufeng
 * @date 2018/9/27
 */
public class RetryTest {

    @Test
    public void retry() {
        Observable.range(1, 10).retry().subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {
//                System.out.println(t.getCause());
            }

            @Override
            public void onNext(Integer var1) {
                if (var1 > 5) throw new RuntimeException("hehhe > 5");
                System.out.println(var1);
            }
        });
    }
}

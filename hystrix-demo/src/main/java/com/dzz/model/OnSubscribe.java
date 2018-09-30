package com.dzz.model;

/**
 * @author zoufeng
 * @date 2018/9/11
 */
public interface OnSubscribe<T> {

    /**
     * 像订阅者发送事件
     *
     * @param subscriber 订阅者
     */
    void call(Subscriber<? super T> subscriber);
}

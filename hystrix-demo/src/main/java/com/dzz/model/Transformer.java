package com.dzz.model;

/**
 * @author zoufeng
 * @date 2018/9/11
 */
public interface Transformer<T, R> {
    R call(T from);
}

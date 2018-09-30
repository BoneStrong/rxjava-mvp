package com.dzz.model;

/*
 * 订阅者
 *
 * */
public abstract class Subscriber<T> implements Observer<T> {

    private String name;

    public Subscriber(String name) {
        this.name = name;
    }

    public Subscriber() {
    }

    public void onStart() {
        //dosomething
        System.out.println(this.name == null ? this : this.name + "  onstart");
    }

    public String getName() {
        return name;
    }

    public Subscriber<T> setName(String name) {
        this.name = name;
        return this;
    }
}

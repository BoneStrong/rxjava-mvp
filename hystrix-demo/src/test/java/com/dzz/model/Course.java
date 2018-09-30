package com.dzz.model;

/**
 * @author zoufeng
 * @date 2018/9/11
 */
public class Course {

    private String name;

    private int id;

    public String getName() {
        return name;
    }

    public Course setName(String name) {
        this.name = name;
        return this;
    }

    public int getId() {
        return id;
    }

    public Course setId(int id) {
        this.id = id;
        return this;
    }
}

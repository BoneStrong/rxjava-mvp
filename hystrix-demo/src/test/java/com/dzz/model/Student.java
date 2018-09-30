package com.dzz.model;

import java.util.List;

/**
 * @author zoufeng
 * @date 2018/9/11
 */
public class Student {

    private String name;

    List<Course>courses;

    public String getName() {
        return name;
    }

    public Student setName(String name) {
        this.name = name;
        return this;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public Student setCourses(List<Course> courses) {
        this.courses = courses;
        return this;
    }
}

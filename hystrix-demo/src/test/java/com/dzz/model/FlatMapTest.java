package com.dzz.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author zoufeng
 * @date 2018/9/22
 */
public class FlatMapTest {

    private List<Student> students = new ArrayList<>();

    @Before
    public void student() {
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(new Course().setName("兰陵一笑").setId(1));
        courses.add(new Course().setName("海陵金莲").setId(2));
        Student hehe = new Student().setName("hehe").setCourses(courses);

        ArrayList<Course> courses2 = new ArrayList<>();
        courses.add(new Course().setName("爆裂鼓手").setId(1));
        courses.add(new Course().setName("动次打次").setId(2));
        Student haha = new Student().setName("haha").setCourses(courses2);

        students.add(hehe);
        students.add(haha);
    }

    @Test
    public void flatmap() {
        Observable.from(students).flatMap(new Transformer<Student, Observable<Course>>() {
            @Override
            public Observable<Course> call(Student student) {
                return Observable.from(student.getCourses()).delay(new Random().nextInt(900), TimeUnit.MILLISECONDS);
            }
        }).subscribe(new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onNext(Course var1) {
                System.out.println(var1.getName());
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

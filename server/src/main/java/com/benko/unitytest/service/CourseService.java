package com.benko.unitytest.service;

import com.benko.unitytest.entity.Course;

import java.util.List;

public interface CourseService {
    Course createClass(Course course);

    Course updateClass(Course course);

    List<Course> getAllClasses();

    Course getClassById(int id);

    void deleteProduct(int id);
}

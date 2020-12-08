package com.unityTest.courseManagement.service;

import com.unityTest.courseManagement.entity.Course;
import com.unityTest.courseManagement.entity.CourseAttribute;

import java.util.List;

public interface CourseService {
    Course createCourse(Course course);

    List<Course> getCourses(Integer id, String code, Integer level, String term, Integer academicYear);

    Course getCourseById(int id);

    void deleteCourse(int id);

    CourseAttribute createCourseAttr(CourseAttribute attribute);

    List<CourseAttribute> getCourseAttributes(Integer id, Integer courseId, String name);

    void deleteCourseAttr(int id);
}

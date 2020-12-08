package com.unityTest.courseManagement.restImpl;

import com.unityTest.courseManagement.entity.Course;
import com.unityTest.courseManagement.entity.CourseAttribute;
import com.unityTest.courseManagement.restApi.CourseApi;
import com.unityTest.courseManagement.service.CourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for the /course endpoint
 */
@RestController
public class CourseController implements CourseApi {

    @Autowired
    private CourseService courseService;

    @Override
    public ResponseEntity<Course> createCourse(Course course) {
        return ResponseEntity.ok().body(courseService.createCourse(course));
    }

    @Override
    public ResponseEntity<List<Course>> getCourses(Integer id, String code, Integer level, String term, Integer year) {
        return ResponseEntity.ok().body(courseService.getCourses(id, code, level, term, year));
    }

    @Override
    public void deleteCourse(Integer courseId) {
        courseService.deleteCourse(courseId);
    }

    @Override
    public ResponseEntity<CourseAttribute> createCourseAttr(Integer courseId, CourseAttribute courseAttr) {
        // Find and and set course id for attribute
        Course course = courseService.getCourseById(courseId);
        courseAttr.setCourseId(course.getId());
        return ResponseEntity.ok().body(courseService.createCourseAttr(courseAttr));
    }

    @Override
    public ResponseEntity<List<CourseAttribute>> getCourseAttrs(Integer courseId, Integer id, String name) {
        return ResponseEntity.ok().body(courseService.getCourseAttributes(id, courseId, name));
    }

    @Override
    public void deleteCourseAttr(Integer attributeId) {
        courseService.deleteCourseAttr(attributeId);
    }
}
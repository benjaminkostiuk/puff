package com.unityTest.courseManagement.restImpl;

import com.unityTest.courseManagement.entity.Course;
import com.unityTest.courseManagement.entity.CourseAttribute;
import com.unityTest.courseManagement.models.CourseAttributePage;
import com.unityTest.courseManagement.models.CoursePage;
import com.unityTest.courseManagement.models.Term;
import com.unityTest.courseManagement.restApi.CourseApi;
import com.unityTest.courseManagement.service.CourseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

/**
 * Rest Controller for the /course endpoint
 */
@RestController
public class CourseController implements CourseApi {

    @Autowired
    private CourseService courseService;

    @Override
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<Course> createCourse(Course course) {
        return new ResponseEntity<>(courseService.createCourse(course), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CoursePage> getCourses(Pageable pageable, Integer id, String code, Integer level, String term, Integer academicYear) {
        // Convert term to Enum before searching
        Term termEnum = null;
        if(term != null) {
            try { termEnum = Term.valueOf(term); }
            catch(IllegalArgumentException e) { throw new HttpMessageNotReadableException("Not one of accepted values"); }
        }
        // Retrieve results using service
        CoursePage page = new CoursePage(courseService.getCourses(pageable, id, code, level, termEnum, academicYear));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Override
    @RolesAllowed("ROLE_ADMIN")
    public void deleteCourse(Integer courseId) {
        courseService.deleteCourse(courseId);
    }

    @Override
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<CourseAttribute> createCourseAttr(Integer courseId, CourseAttribute courseAttr) {
        // Find and and set course id for attribute
        Course course = courseService.getCourseById(courseId);
        courseAttr.setCourseId(course.getId());
        return new ResponseEntity<>(courseService.createCourseAttr(courseAttr), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CourseAttributePage> getCourseAttrs(Pageable pageable, Integer courseId, Integer id, String name) {
        CourseAttributePage page = new CourseAttributePage(courseService.getCourseAttributes(pageable, id, courseId, name));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Override
    @RolesAllowed("ROLE_ADMIN")
    public void deleteCourseAttr(Integer attributeId) {
        courseService.deleteCourseAttr(attributeId);
    }
}
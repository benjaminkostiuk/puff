package com.unityTest.courseManagement.service;

import com.unityTest.courseManagement.entity.Course;
import com.unityTest.courseManagement.entity.CourseAttribute;
import com.unityTest.courseManagement.exception.ElementNotFoundException;
import com.unityTest.courseManagement.models.Term;
import com.unityTest.courseManagement.repository.CourseAttrRepository;
import com.unityTest.courseManagement.repository.CourseRepository;
import com.unityTest.courseManagement.serviceImpl.CourseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Implements unit tests for CourseService
 */
@ExtendWith(MockitoExtension.class)
class CourseServiceTests {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseAttrRepository courseAttrRepository;

    @InjectMocks
    private CourseService courseService;

    // Mock course
    private Course course;

    // Mock course list
    private List<Course> courseList;

    // Mock course attribute
    private CourseAttribute courseAttribute;

    // Mock course attribute list
    private List<CourseAttribute> courseAttributeList;

    @BeforeEach
    void setup() {
        // Setup course
        course = new Course(1, "TEST COURSE CODE", 1, Term.WINTER, 2000);
        // Setup course list
        courseList = new ArrayList<>();
        courseList.add(course);

        // Setup course attribute
        courseAttribute = new CourseAttribute(1, 1, "TEST_NAME", "TEST VALUE");
        // Setup course attribute list
        courseAttributeList = new ArrayList<>();
        courseAttributeList.add(courseAttribute);
    }

    @Test
    void createCourse_Valid_ReturnCreatedCourse() {
        when(this.courseRepository.save(any(Course.class))).then(returnsFirstArg());
        assertEquals(course, courseService.createCourse(course));
    }

    @Test
    void getCourses_Valid_ReturnCourseList() {
        Page<Course> page = new PageImpl<>(courseList);
        when(this.courseRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        assertEquals(courseList, courseService.getCourses(1, "COMPSCI 1JC3", 1, Term.WINTER, 2000));
    }

    /**
     * Test element not found for GetCourseById
     */
    @Test
    void getCourseById_CourseNotFound_ElementNotFoundException() {
        assertThrows(ElementNotFoundException.class, () -> {
            when(this.courseRepository.findById(1)).thenReturn(Optional.empty());  // Set repository to not find element
            courseService.getCourseById(1);
        });
    }

    @Test
    void getCourseById_CourseFound_ReturnCourse() {
        when(this.courseRepository.findById(1)).thenReturn(Optional.of(course));
        assertEquals(course, courseService.getCourseById(1));
    }

    @Test
    void deleteCourse_Valid_DeleteCourse() {
        courseService.deleteCourse(1);
    }

    @Test
    void createCourseAttr_Valid_ReturnCourseAttributeCreated() {
        when(this.courseAttrRepository.save(any(CourseAttribute.class))).then(returnsFirstArg());
        assertEquals(courseAttribute, courseService.createCourseAttr(courseAttribute));
    }

    @Test
    void getCourseAttributes_Valid_ReturnCourseAttributeList() {
        Page<CourseAttribute> page = new PageImpl<>(courseAttributeList);
        when(this.courseAttrRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        assertEquals(courseAttributeList, courseService.getCourseAttributes(1, 1, "TEST_NAME"));
    }

    @Test
    void deleteCourseAttr_Valid_DeleteCourseAttribute() {
        courseService.deleteCourseAttr(1);
    }
}


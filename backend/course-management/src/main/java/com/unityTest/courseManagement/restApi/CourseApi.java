package com.unityTest.courseManagement.restApi;

import com.unityTest.courseManagement.entity.Course;
import com.unityTest.courseManagement.entity.CourseAttribute;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "Class Management API", tags = "Class API", description = "Manage Class resources")
@Validated
@RequestMapping(value = "/course")
public interface CourseApi {
    /**
     * POST endpoint to create a Course
     * @return Created course
     */
    @ApiOperation(value = "Create or update a course", nickname = "createCourse", response = Course.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "",  produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Course> createCourse(@ApiParam(value = "Course to create") @Valid @RequestBody Course course);

    /**
     * GET endpoint to retrieve courses
     * Filter by id, code, level, term and academic year
     * @return List of Course objects matching criterion
     */
    @ApiOperation(value = "Retrieve a list of courses", nickname = "getCourses", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<Course>> getCourses(
            Pageable pageable,
            @ApiParam("Course id") @RequestParam(value = "id", required = false) Integer id,
            @ApiParam("Course code") @RequestParam(value = "code", required = false) String code,
            @ApiParam("Course level") @RequestParam(value = "level", required = false) Integer level,
            @ApiParam("Course term") @RequestParam(value = "term", required = false) String term,
            @ApiParam("Course academic year") @RequestParam(value = "academicYear", required = false) Integer academicYear
    );

    /**
     * DELETE endpoint to delete a course
     * @param courseId Id of course to delete
     */
    @ApiOperation(value = "Delete a course", nickname = "deleteCourse")
    @DeleteMapping(value = "/{courseId}")
    void deleteCourse(@ApiParam(value = "Course id", required = true) @PathVariable(value = "courseId") Integer courseId);

    /**
     * POST endpoint to create an attribute for a course
     * @return Create course attribute
     */
    @ApiOperation(value = "Create or update a course attribute", nickname = "createCourseAttr", response = CourseAttribute.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/{courseId}/attr", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CourseAttribute> createCourseAttr(
            @ApiParam(value = "Course id", required = true) @PathVariable(value = "courseId") Integer courseId,
            @ApiParam("Course attribute to create") @Valid @RequestBody CourseAttribute courseAttr
    );

    /**
     * GET endpoint to retrieve attributes for a course
     * @return List of attributes for a course
     */
    @ApiOperation(value = "Get attributes for a course", nickname = "getCourseAttrs", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{courseId}/attr", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<CourseAttribute>> getCourseAttrs(
            Pageable pageable,
            @ApiParam(value = "course id", required = true) @PathVariable(value = "courseId") Integer courseId,
            @ApiParam("Attribute id") @RequestParam(value = "id", required = false) Integer id,
            @ApiParam("Attribute name") @RequestParam(value = "name", required = false) String name
    );

    /**
     * DELETE endpoint to delete a course attribute
     * @param attributeId Id of course attribute to delete
     */
    @ApiOperation(value = "Delete a course attribute", nickname = "deleteCourseAttr")
    @DeleteMapping(value = "/attr/{attributeId}")
    void deleteCourseAttr(@ApiParam(value = "Course attribute id", required = true) @PathVariable(value = "attributeId") Integer attributeId);
}

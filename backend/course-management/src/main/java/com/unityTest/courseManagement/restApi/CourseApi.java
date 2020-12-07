package com.unityTest.courseManagement.restApi;

import com.unityTest.courseManagement.entity.Course;
import com.unityTest.courseManagement.entity.CourseAttribute;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    @ApiOperation(value = "Create a course", nickname = "createCourse", response = Course.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "",  produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Course> createCourse(@ApiParam(value = "Course to create") @Valid @RequestBody Course course);

    /**
     * GET endpoint to retrieve courses
     * Filter by id, code, level, term and academic year
     * @return List of Course objects matching criterion
     */
    @ApiOperation(value = "Retrieve a list of courses", nickname = "getCourses", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Course>> getCourses(
            @ApiParam("course id") @RequestParam(value = "id", required = false) Integer id,
            @ApiParam("course code") @RequestParam(value = "code", required = false) String code,
            @ApiParam("course level") @RequestParam(value = "level", required = false) Integer level,
            @ApiParam("course term") @RequestParam(value = "term", required = false) String term,
            @ApiParam("course academic year") @RequestParam(value = "year", required = false) Integer year
    );

    /**
     * DELETE endpoint to delete a course
     * @param courseId Id of course to delete
     */
    @ApiOperation(value = "Delete a course", nickname = "deleteCourse")
    @DeleteMapping(value = "/{courseId}")
    void deleteCourse(@ApiParam(value = "course id", required = true) @PathVariable(value = "courseId") Integer courseId);

    /**
     * POST endpoint to create an attribute for a course
     * @return Create course attribute
     */
    @ApiOperation(value = "Create a course attribute", nickname = "createCourseAttr", response = CourseAttribute.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/{courseId}/attr", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CourseAttribute> createCourseAttr(
            @ApiParam(value = "course id", required = true) @PathVariable(value = "courseId") Integer courseId,
            @ApiParam("course attribute to create") @Valid @RequestBody CourseAttribute courseAttr
    );

    /**
     * GET endpoint to retrieve attributes for a course
     * @return List of attributes for a course
     */
    @ApiOperation(value = "Get attributes for a course", nickname = "getCourseAttrs", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{courseId}/attr", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<CourseAttribute>> getCourseAttrs(
            @ApiParam(value = "course id", required = true) @PathVariable(value = "courseId") Integer courseId,
            @ApiParam("attribute id") @RequestParam(value = "id", required = false) Integer id,
            @ApiParam("attribute name") @RequestParam(value = "name", required = false) String name
    );

    /**
     * DELETE endpoint to delete a course attribute
     * @param attributeId Id of course attribute to delete
     */
    @ApiOperation(value = "Delete a course attribute", nickname = "deleteCourseAttr")
    @DeleteMapping(value = "/attr/{attributeId}")
    void deleteCourseAttr(@ApiParam(value = "course attribute id", required = true) @PathVariable(value = "attributeId") Integer attributeId);
}

package com.benko.unitytest.rest;

import com.benko.unitytest.entity.Course;
import com.benko.unitytest.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value = "Class Management Api", tags = "Class API", description = "Manage Class resources")
public class CourseController {

    // Notes on swagger annotations
    //    @Api	Marks a class as a Swagger resource.
    //    @ApiModel	Provides additional information about Swagger models.
    //    @ApiModelProperty	Adds and manipulates data of a model property.
    //    @ApiOperation	Describes an operation or typically an HTTP method against a specific path.
    //    @ApiParam	Adds additional meta-data for operation parameters.
    //    @ApiResponse	Describes a possible response of an operation.
    //    @ApiResponses	A wrapper to allow a list of multiple ApiResponse objects.
    // For more informaton https://github.com/swagger-api/swagger-core/wiki/Annotations-1.5.X

    @Autowired
    private CourseService courseService;

    @ApiOperation(value = "Get a list of all classes", response = List.class, httpMethod = "GET")
    @GetMapping("/class")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok().body(courseService.getAllClasses());
    }

    @ApiOperation(value = "Create a new class", response = Course.class)
    @PostMapping("/class")
    public ResponseEntity<Course> createCourse(
            @ApiParam(value = "Class to create", required = true) @Valid @RequestBody Course course
    ) {
        return ResponseEntity.ok().body(courseService.createClass(course));
    }
}

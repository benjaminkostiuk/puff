package com.unityTest.courseManagement.rest;

import com.unityTest.courseManagement.entity.Course;
import com.unityTest.courseManagement.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Validated
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
    @RolesAllowed("ROLE_LIBRARIAN")
    @PostMapping("/class")
    public ResponseEntity<Course> createCourse(
            Principal principal,
            @ApiParam(value = "Class to create", required = true) @Valid @RequestBody Course course
    ) {
        // How to get keycloak user id token
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
        AccessToken accessToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();
        System.out.println(accessToken.getId());
        System.out.println(accessToken.getPreferredUsername());
        return ResponseEntity.ok().body(courseService.createClass(course));
    }

    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public Map<String, String> handleValidationExceptions(
    //         MethodArgumentNotValidException ex) {
    //     Map<String, String> errors = new HashMap<>();
    //     ex.getBindingResult().getAllErrors().forEach((error) -> {
    //         String fieldName = ((FieldError) error).getField();
    //         String errorMessage = error.getDefaultMessage();
    //         errors.put(fieldName, errorMessage);
    //     });
    //     return errors;
    // }
}

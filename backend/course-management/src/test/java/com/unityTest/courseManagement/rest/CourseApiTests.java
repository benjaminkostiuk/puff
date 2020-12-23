package com.unityTest.courseManagement.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.unityTest.courseManagement.constants.ExceptionMsg;
import com.unityTest.courseManagement.entity.Course;
import com.unityTest.courseManagement.entity.CourseAttribute;
import com.unityTest.courseManagement.models.Term;
import com.unityTest.courseManagement.repository.CourseAttrRepository;
import com.unityTest.courseManagement.repository.CourseRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.unityTest.courseManagement.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Implements Integration tests for Course API endpoints
 */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CourseApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseAttrRepository courseAttrRepository;

    private final String baseUri = "/course";

    @AfterEach
    void cleanupDB() {
        courseRepository.deleteAll();
        courseAttrRepository.deleteAll();
    }

    @Test
    void createCourse_ValidArg_SaveCourseToRepo() throws Exception {
        final Course courseToCreate = new Course(0, "CREATE_TEST", 1, Term.FALL, 2020);

        // Perform POST request
        MvcResult result = mockMvc.perform(post(this.baseUri, courseToCreate))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.code").value(courseToCreate.getCode()))
                .andExpect(jsonPath("$.level").value(courseToCreate.getLevel()))
                .andExpect(jsonPath("$.term").value(courseToCreate.getTerm().toString()))
                .andExpect(jsonPath("$.academicYear").value(courseToCreate.getAcademicYear()))
                .andReturn();
        // Extract id of created course
        int courseId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        courseToCreate.setId(courseId);
        // Verify in repository
        assertThat(courseRepository.findAll()).hasSize(1);
        assertEquals(courseRepository.findAll().get(0), courseToCreate);
    }

    @Test
    void createCourse_MissingRequiredArgs_ValidationError() throws Exception {
        final Course courseToCreate = new Course(0, null, 6, null, null);

        // Perform POST request
        mockMvc.perform(post(this.baseUri, courseToCreate))
                .andExpect(status().isBadRequest())     // Expect a 400 error
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(ExceptionMsg.METHOD_ARGUMENT_NOT_VALID))
                // Check that the validation errors are returned
                .andExpect(jsonPath("$.subErrors[*].field").value(containsInAnyOrder("code", "academicYear", "term", "level")));
    }

    @Test
    void createCourse_UniqueConstraintViolation_DataConflictError() throws Exception {
        // Create mock course to save to database
        Course course = new Course(0, "CONSTRAINT_CREATE_TEST", 2, Term.FALL, 2020);

        // Perform first POST request
        mockMvc.perform(post(this.baseUri, course))
                .andExpect(status().isCreated());       // Verify creation
        // Perform second request to violate unique constraint
        mockMvc.perform(post(this.baseUri, course))
                .andExpect(status().isConflict())       // Expect a 409 status
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.name()))
                .andExpect(jsonPath("$.message").value(ExceptionMsg.DATABASE_CONFLICT))
                .andExpect(jsonPath("$.path").value(baseUri));
        // Verify only one course was created in database
        assertThat(courseRepository.findAll()).hasSize(1);
    }

    @Test
    void getCourses_ValidQuery_GetListOfCourses() throws Exception {
        // Create mock courses
        final Course course1 = courseRepository.save(new Course(0, "GET_TEST_1", 1, Term.FALL, 2019));
        final Course course2 = courseRepository.save(new Course(0, "GET_TEST_2", 2, Term.FALL, 2020));
        // Perform GET request
        mockMvc.perform(get(this.baseUri))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                // Check first course
                .andExpect(jsonPath("$.data[0].id").value(course1.getId()))
                .andExpect(jsonPath("$.data[0].code").value(course1.getCode()))
                .andExpect(jsonPath("$.data[0].level").value(course1.getLevel()))
                .andExpect(jsonPath("$.data[0].term").value(course1.getTerm().toString()))
                .andExpect(jsonPath("$.data[0].academicYear").value(course1.getAcademicYear()))
                // Check second course
                .andExpect(jsonPath("$.data[1].id").value(course2.getId()))
                .andExpect(jsonPath("$.data[1].code").value(course2.getCode()))
                .andExpect(jsonPath("$.data[1].level").value(course2.getLevel()))
                .andExpect(jsonPath("$.data[1].term").value(course2.getTerm().toString()))
                .andExpect(jsonPath("$.data[1].academicYear").value(course2.getAcademicYear()));
    }

    @Test
    void deleteCourse_CourseExists_DeleteCourseInRepo() throws Exception {
        // Create mock course in repository to delete
        Course course = courseRepository.save(new Course(0, "DELETE_TEST", 1, Term.WINTER, 2000));
        final String url = String.format("%s/%d", this.baseUri, course.getId());

        // Perform DELETE request
        mockMvc.perform(delete(url))
                .andExpect(status().isNoContent());
        // Verify to see if course was deleted
        assertThat(courseRepository.findAll()).hasSize(0);
    }

    @Test
    void deleteCourse_CourseDoesNotExist_ElementNotFoundError() throws Exception {
        final String url = String.format("%s/%d", this.baseUri, 1);

        // Call delete endpoint, however no courses exist in the database
        mockMvc.perform(delete(url))
                .andExpect(status().isNotFound())       // Status should be a 404
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(ExceptionMsg.ELEMENT_DOES_NOT_EXIST))
                .andExpect(jsonPath("$.path").value(url));
    }

    @Test
    void createCourseAttr_ValidArg_SaveCourseAttrInRepo () throws Exception {
        // Create course to attach attribute to in repository
        Course course = courseRepository.save(new Course(0, "TEST_CODE", 1, Term.SUMMER, 2000));
        final String url = String.format("%s/%d/attr", this.baseUri, course.getId());
        CourseAttribute courseAttrToCreate = new CourseAttribute(0, null, "TEST_NAME", "TEST_VAL");

        // Perform POST request
        MvcResult result = mockMvc.perform(post(url, courseAttrToCreate))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(courseAttrToCreate.getName()))
                .andExpect(jsonPath("$.value").value(courseAttrToCreate.getValue()))
                .andReturn();
        // Extract id of created course attribute
        int courseAttributeId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        // Set id and courseId to check
        courseAttrToCreate.setId(courseAttributeId);
        courseAttrToCreate.setCourseId(course.getId());
        // Verify in repository
        assertThat(courseAttrRepository.findAll()).hasSize(1);
        assertEquals(courseAttrRepository.findAll().get(0), courseAttrToCreate);
    }

    @Test
    void createCourseAttr_CourseDoesNotExist_ElementNotFoundError() throws Exception {
        final String url = String.format("%s/%d/attr", this.baseUri, 1);

        // Create CourseAttribute to save to repo
        CourseAttribute courseAttribute = new CourseAttribute(0, null, "TEST_NAME", "TEST_VAL");

        // Perform post request
        mockMvc.perform(post(url, courseAttribute))
                .andExpect(status().isNotFound())       // Expect a 404 error
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(ExceptionMsg.ELEMENT_NOT_FOUND))
                .andExpect(jsonPath("$.path").value(url));
    }

    @Test
    void createCourseAttr_MissingRequiredArgs_ValidationError() throws Exception {
        // Create course to attach attribute to in repository
        Course course = courseRepository.save(new Course(0, "TEST_CODE", 1, Term.SUMMER, 2000));
        final String url = String.format("%s/%d/attr", this.baseUri, course.getId());

        // Create CourseAttribute missing name and value
        CourseAttribute courseAttribute = new CourseAttribute(0, null, null, null);

        // Perform POST request
        mockMvc.perform(post(url, courseAttribute))
                .andExpect(status().isBadRequest())     // Expect a 400 error
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(ExceptionMsg.METHOD_ARGUMENT_NOT_VALID))
                // Check that the validation errors are returned
                .andExpect(jsonPath("$.subErrors[*].field").value(containsInAnyOrder("name", "value")));
    }

    @Test
    void createCourseAttr_UniqueConstraintViolation_DataConflictError() throws Exception {
        // Create course to attach attribute to in repository
        Course course = courseRepository.save(new Course(0, "TEST_CODE", 1, Term.SUMMER, 2000));
        final String url = String.format("%s/%d/attr", this.baseUri, course.getId());

        // Create CourseAttribute to save (twice) to repo
        CourseAttribute courseAttribute = new CourseAttribute(0, null, "TEST_NAME", "TEST_VAL");

        // Perform first POST request
        mockMvc.perform(post(url, courseAttribute))
                .andExpect(status().isCreated());       // Verify 201 on first request

        // Perform second POST request to violate unique constraint
        mockMvc.perform(post(url, courseAttribute))
                .andExpect(status().isConflict())       // Expect 409 error
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.name()))
                .andExpect(jsonPath("$.message").value(ExceptionMsg.DATABASE_CONFLICT))
                .andExpect(jsonPath("$.path").value(url));
        // Verify only one course attribute was created in database
        assertThat(courseAttrRepository.findAll()).hasSize(1);
    }

    @Test
    void getCourseAttrs_ValidQuery_ReturnListOfCourseAttributes() throws Exception {
        Course course = courseRepository.save(new Course(0, "TEST_GET_COURSE_ATTR", 1, Term.WINTER, 2020));
        String url = String.format("%s/%d/attr", this.baseUri, course.getId());
        // Create mock entities
        CourseAttribute attr1 = courseAttrRepository.save(new CourseAttribute(0, course.getId(), "TEST_NAME_1", "TEST_VAL_1"));
        CourseAttribute attr2 = courseAttrRepository.save(new CourseAttribute(0, course.getId(), "TEST_NAME_2", "TEST_VAL_2"));

        // Perform GET request
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                // Check first course attribute
                .andExpect(jsonPath("$.data[0].id").value(attr1.getId()))
                .andExpect(jsonPath("$.data[0].name").value(attr1.getName()))
                .andExpect(jsonPath("$.data[0].value").value(attr1.getValue()))
                // Check second course attribute
                .andExpect(jsonPath("$.data[1].id").value(attr2.getId()))
                .andExpect(jsonPath("$.data[1].name").value(attr2.getName()))
                .andExpect(jsonPath("$.data[1].value").value(attr2.getValue()));
    }

    @Test
    void deleteCourseAttr_CourseAttributeExists_DeleteCourseAttributeInRepo() throws Exception {
        // Create mock courseAttribute in repository to delete
        Course course = courseRepository.save(new Course(0, "TEST_CODE", 1, Term.WINTER, 2000));
        CourseAttribute courseAttribute = courseAttrRepository.save(new CourseAttribute(0, course.getId(), "TEST_NAME", "TEST_VALUE"));
        final String url = String.format("%s/attr/%d", this.baseUri, courseAttribute.getId());

        // Perform DELETE request
        mockMvc.perform(delete(url))
                .andExpect(status().isNoContent());
        // Check to see if course attribute was deleted
        assertThat(courseAttrRepository.findAll()).hasSize(0);
    }

    @Test
    void deleteCourseAttr_CourseAttributeDoesNotExist_ElementNotFoundError() throws Exception {
        final String url = String.format("%s/attr/%d", this.baseUri, 1);

        // Call delete endpoint, no CourseAttributes will exist in database
        mockMvc.perform(delete(url))
                .andExpect(status().isNotFound())       // Status should be a 404
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(ExceptionMsg.ELEMENT_DOES_NOT_EXIST))
                .andExpect(jsonPath("$.path").value(url));
    }
}

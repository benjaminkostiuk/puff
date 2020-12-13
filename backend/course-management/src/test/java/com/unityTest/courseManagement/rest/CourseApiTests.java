package com.unityTest.courseManagement.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.unityTest.courseManagement.entity.Course;
import com.unityTest.courseManagement.entity.CourseAttribute;
import com.unityTest.courseManagement.models.Term;
import com.unityTest.courseManagement.repository.CourseAttrRepository;
import com.unityTest.courseManagement.repository.CourseRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.unityTest.courseManagement.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Implements Integration tests for Course API endpoints
 */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
    void createCourse() throws Exception {
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
    void getCourses() throws Exception {
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
    void deleteCourse() throws Exception {
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
    void createCourseAttr() throws Exception {
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
    void getCourseAttrs() throws Exception {
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
    void deleteCourseAttr() throws Exception {
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
}

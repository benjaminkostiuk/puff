package com.unityTest.courseManagement.rest;

import com.unityTest.courseManagement.constants.ExceptionMsg;
import com.unityTest.courseManagement.entity.Course;
import com.unityTest.courseManagement.models.Term;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.unityTest.courseManagement.TestUtils.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Implements testing of global exception handling for external REST APIs
 */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class RestExceptionHandlerTests {

    @Autowired
    private MockMvc mockMvc;

    // Course endpoint
    private String courseURI = "/course";

    /**
     * Test MissingServletRequestParameterException handler when request is missing required parameter.
     */
    @Test
    void handleMissingServletRequestParameter_missingRequiredParam_BadRequestError() throws Exception {
        // TODO Implement MissingServletRequestParameter test once an endpoint that can this error exists
    }

    /*
        Test HttpMediaTypeNotSupported handler when request header Content-Type is set to include unsupported media type.
     */
    @Test
    void handleHttpMediaTypeNotSupported_BadMediaType_UnsupportedMediaTypeError() throws Exception {
        final Course course = new Course(0, "TEST", 1, Term.FALL, 2020);
        final String uri = this.courseURI;

        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .content(asJsonString(course))
                .contentType(MediaType.IMAGE_PNG)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType())       // Expect a 415 status to be returned
                .andExpect(jsonPath("$.code").value(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.UNSUPPORTED_MEDIA_TYPE.name()))
                .andExpect(jsonPath("$.path").value(uri));
    }

    /**
     * Test HttpRequestMethodNotSupportException
     */
    @Test
    void handleHttpRequestMethodNotSupported_InvalidHttpVerb_MethodNotAllowedError() throws Exception {
        final String uri = this.courseURI;

        // Call DELETE on /course endpoint. /course only supports POST and GET Http verbs
        mockMvc.perform(delete(uri))
                .andExpect(status().isMethodNotAllowed())       // Expect a 405 status to be returned
                .andExpect(jsonPath("$.code").value(HttpStatus.METHOD_NOT_ALLOWED.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.METHOD_NOT_ALLOWED.name()))
                .andExpect(jsonPath("$.path").value(uri));
    }

    /**
     * Test NoHandlerFoundException handler for request to unsupported path.
     * TODO Add test once NoHandlerFoundException added to RestExceptionHanlder
     */
    void handleNotHandlerFoundException_InvalidPath_NotFoundError() throws Exception {
        final String uri = "/invalidPath";      // Create some invalid path that does not exist

        // Call DELETE on invalid path
        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isNotFound())       // Expect a 404 status code
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.path").value(uri));
    }

    /**
     * Test HttpMessageNotReadableException handler for request with malformed JSON body.
     */
    @Test
    void handleHttpMessageNotReadable_MalformedJson_BadRequestError() throws Exception {
        final String uri = this.courseURI;
        final String malformedJsonBody = "{\n\t\"code\":\"Test\",";     // Create JSON missing closing bracket

        // Call /course endpoint with malformed JSON body
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .content(malformedJsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())     // Expect a 400 status to be returned
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").value(ExceptionMsg.MALFORMED_JSON_REQUEST))
                .andExpect(jsonPath("$.path").value(uri));
    }

    /**
     * Test PropertyReferenceException handler for request with pagination sorting referencing
     * field not in object.
     */
    @Test
    void handlePropertyReferenceException_BadPaginationSorting_NotFoundError() throws Exception {
        final String uri = String.format("%s?sort=%s,asc", this.courseURI, "xx");

        // Call GET endpoint and try to sort by property 'xx' which doesn't exist in course object
        mockMvc.perform(get(uri))
                .andExpect(status().isNotFound())       // Expect a 404 status to be returned
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(ExceptionMsg.PROPERTY_REFERENCE))
                .andExpect(jsonPath("$.path").value(this.courseURI));
    }
}

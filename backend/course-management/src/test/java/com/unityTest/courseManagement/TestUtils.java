package com.unityTest.courseManagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class TestUtils {

    public static MockHttpServletRequestBuilder get(String url) {
        return MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder post(String url, Object body) {
        return MockMvcRequestBuilders.post(url)
                .content(asJsonString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder put(String url, Object body) {
        return MockMvcRequestBuilders.put(url)
                .content(asJsonString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder delete(String url) {
        return MockMvcRequestBuilders.delete(url)
                .accept(MediaType.APPLICATION_JSON);
    }

    /**
     * Return a JSON string representation of an object
     * @param obj Object to serialize as JSON
     * @throws RuntimeException if object cannot be serialized
     * @return JSON string representation of object
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.unityTest.courseManagement.constants;

import org.springframework.http.MediaType;

import java.util.List;

/**
 * Contains all exception messages for RestExceptionHandler
 */
public class ExceptionMsg {
    public static final String NO_HANDLER_FOUND = "No handler found for path";
    public static final String MALFORMED_JSON_REQUEST = "Malformed JSON request";
    public static final String METHOD_ARGUMENT_NOT_VALID = "Validation error";
    public static final String CONSTRAINT_VALIDATION = "Validation error";
    public static final String ELEMENT_NOT_FOUND = "Element not found";
    public static final String DATABASE_CONFLICT = "Database conflict";
    public static final String PROPERTY_REFERENCE = "Property does not exist";
    public static final String ELEMENT_DOES_NOT_EXIST = "Element does not exist";

    public static String MISSING_REQUEST_PARAMETER(String name) {
        return String.format("%s parameter is missing", name);
    }

    public static String HTTP_MEDIA_TYPE_NOT_SUPPORTED(MediaType type, List<MediaType> supportedTypes) {
        StringBuilder builder = new StringBuilder();
        builder.append(type);
        builder.append(" media type is not supported. Supported media types are ");
        supportedTypes.forEach(t -> builder.append(t).append(", "));
        return builder.substring(0, builder.length() - 2);
    }

    public static String HTTP_REQUEST_METHOD_NOT_SUPPORTED(String method) {
        return String.format("Request method '%s' not supported", method);
    }


}

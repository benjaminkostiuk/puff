package com.unityTest.courseManagement;

import com.unityTest.courseManagement.constants.ExceptionMsg;
import com.unityTest.courseManagement.exception.ElementNotFoundException;
import com.unityTest.courseManagement.models.error.ApiError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Global REST API Exception Handler
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle MissingServletRequestParameterException. Thrown when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object as response with 400 status code
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        String message = ExceptionMsg.MISSING_REQUEST_PARAMETER(ex.getParameterName());
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message, ex, request));
    }

    /**
     * Handle HttpMediaTypeNotSupportedException. Thrown when MediaType is not supported.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object as response with 415 status code
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        String message = ExceptionMsg.HTTP_MEDIA_TYPE_NOT_SUPPORTED(ex.getContentType(), ex.getSupportedMediaTypes());
        ApiError error = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(), ex, request);
        error.setDebugMessage(message);
        return buildResponseEntity(error);
    }

    /**
     * Handle HttpRequestMethodNotSupportedException. Thrown when endpoint is called with unsupported HTTP verb.
     *
     * @param ex        HttpRequestMethodNotSupportedException
     * @param headers   HttpHeaders
     * @param status    HttpStatus
     * @param request   WebRequest
     * @return ApiError object as response with 405 status code
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        // Create debug message string
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for path. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(m -> builder.append(m.toString()).append(", "));
        // Create ApiError object to return as response
        ApiError error = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ExceptionMsg.HTTP_REQUEST_METHOD_NOT_SUPPORTED(ex.getMethod()), ex, request);
        error.setDebugMessage(builder.substring(0, builder.length() - 2));
        return buildResponseEntity(error);
    }

    /**
     * Handle NoHandlerFoundException. Thrown when no handler exists for an endpoint.
     * NOTE: NOT CURRENTLY IMPLEMENTED
     *
     * @param ex        NoHandlerFoundException
     * @param headers   HttpHeaders
     * @param status    HttpStatus
     * @param request   WebRequest
     * @return ApiError object as response with 404 status code
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ExceptionMsg.NO_HANDLER_FOUND, ex, request));
    }

    /**
     * Handle HttpMessageNotReadableException. Thrown when request JSON is malformed.
     *
     * @param ex        HttpMessageNotReadableException
     * @param headers   HttpHeaders
     * @param status    HttpStatus
     * @param request   WebRequest
     * @return ApiError object as response with 400 status code
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ExceptionMsg.MALFORMED_JSON_REQUEST, ex, request));
    }

    /**
     * Handle MethodArgumentNotValidException. Thrown when an object with @Valid fails validation.
     *
     * @param ex        MethodArgumentNotValidException that is thrown
     * @param headers   HttpHeaders
     * @param status    HttpStatus
     * @param request   WebRequest
     * @return ApiError object as response with 400 status code
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ExceptionMsg.METHOD_ARGUMENT_NOT_VALID, ex, request);
        error.setDebugMessage("Validation failed for argument");
        // Add all field errors
        ex.getBindingResult().getFieldErrors().forEach(error::addValidationError);
        // Add all global validation errors
        ex.getBindingResult().getGlobalErrors().forEach(error::addValidationError);
        return buildResponseEntity(error);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex        ConstraintViolationException
     * @param headers   HttpHeaders
     * @param status    HttpStatus
     * @param request   WebRequest
     * @return ApiError object as response with 400 status code
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            javax.validation.ConstraintViolationException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ExceptionMsg.CONSTRAINT_VALIDATION, ex, request);
        error.setDebugMessage("Validation failed for argument");
        // Add all constraint violation errors
        ex.getConstraintViolations().forEach(error::addValidationError);
        return buildResponseEntity(error);
    }

    /**
     * Handle ElementNotFoundException. Thrown when queried element cannot be found in database.
     *
     * @param ex        ElementNotFoundException that is thrown
     * @param request   HttpServletRequest web request
     * @return ApiError object as response with 404 status code
     */
    @ExceptionHandler(ElementNotFoundException.class)
    protected ResponseEntity<Object> handleElementNotFound(
            ElementNotFoundException ex,
            HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ExceptionMsg.ELEMENT_NOT_FOUND, ex, request);
        return buildResponseEntity(error);
    }

    /**
     * Handle DataIntegrityViolationException. Thrown when database attempts to perform an action that would violate
     * it data integrity. Root causes are generally ConstraintViolationExceptions.
     *
     * @param ex        DataIntegrityViolationException
     * @param request   HttpServletRequest
     * @return ApiError object as response with 409 status code
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        if(ex.getCause() instanceof javax.validation.ConstraintViolationException
            || ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, ExceptionMsg.DATABASE_CONFLICT, ex, request));
        }
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), ex.getCause(), request));
    }

    /**
     * Handle PropertyReferenceException. Thrown when property referenced in param does not exist.
     *
     * @param ex        PropertyReferenceException
     * @param request   HttpServletRequest
     * @return ApiError object as response with 400 status code
     */
    @ExceptionHandler(org.springframework.data.mapping.PropertyReferenceException.class)
    protected ResponseEntity<Object> handlePropertyReferenceException(
            PropertyReferenceException ex,
            HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ExceptionMsg.PROPERTY_REFERENCE, ex, request);
        return buildResponseEntity(error);
    }

    /**
     * Handle EmptyResultDataAccessException. Thrown when a result was expected to have at least one element but zero
     * elements were actually returned.
     *
     * @param ex        EmptyResultDataAccessException
     * @param request   HttpServletRequest
     * @return ApiError object as response with 404 status code
     */
    @ExceptionHandler(org.springframework.dao.EmptyResultDataAccessException.class)
    protected ResponseEntity<Object> handleEmptyResultDataAccess(
            EmptyResultDataAccessException ex,
            HttpServletRequest request) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ExceptionMsg.ELEMENT_DOES_NOT_EXIST, ex, request);
        return buildResponseEntity(error);
    }


    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}

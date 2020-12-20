package com.unityTest.courseManagement.models.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Models an API error. Returned with a non 2xx response code.
 */
@Data
@ApiModel(value = "ApiError", description = "REST Api Error")
public class ApiError {

    @ApiModelProperty(example = "2020-12-19T01:00:26")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private LocalDateTime timestamp;

    @ApiModelProperty
    private Integer code;

    @ApiModelProperty
    private HttpStatus status;

    @ApiModelProperty
    private String message;

    @ApiModelProperty
    private String debugMessage;

    @ApiModelProperty
    private String path;

    @ApiModelProperty
    private List<ApiSubError> subErrors = new ArrayList<>();

    private ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.code = status.value();
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    private ApiError(HttpStatus status, String message, Throwable ex, String path) {
        this(status, message, ex);
        this.path = path;
    }

    public ApiError(HttpStatus status, String message, Throwable ex, WebRequest request) {
        this(status, message, ex, ((ServletWebRequest) request).getRequest().getRequestURI());
    }

    public ApiError(HttpStatus status, String message, Throwable ex, HttpServletRequest request) {
        this(status, message, ex, request.getRequestURI());
    }

    private void addSubError(ApiSubError subError) {
        this.subErrors.add(subError);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addSubError(new ApiValidationError(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message) {
        addSubError(new ApiValidationError(object, message));
    }

    /**
     * Add a ObjectError validation error as a sub error. Usually when a @Valid validation fails.
     *
     * @param objectError ObjectError to be added as a validation sub error.
     */
    public void addValidationError(ObjectError objectError) {
        if(objectError instanceof FieldError) {
            FieldError fieldError = (FieldError) objectError;
            this.addValidationError(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
        } else {
            this.addValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
        }
    }

    /**
     * Add a ConstraintViolation as a sub error. Usually when a @Validated validation fails.
     *
     * @param cv ConstraintViolation error to be added as a validation sub error.
     */
    public void addValidationError(ConstraintViolation<?> cv) {
        this.addValidationError(
                cv.getRootBeanClass().getSimpleName(),
                ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                cv.getInvalidValue(),
                cv.getMessage());
    }
}

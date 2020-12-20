package com.unityTest.courseManagement.models.error;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Models a validation sub error
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class ApiValidationError extends ApiSubError {

    @ApiModelProperty
    private String object;

    @ApiModelProperty
    private String field;

    @ApiModelProperty
    private Object rejectedValue;

    @ApiModelProperty
    private String message;

    public ApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }

    public ApiValidationError(String object, String field, Object rejectedValue, String message) {
        this(object,  message);
        this.field = field;
        this.rejectedValue = rejectedValue;
    }

}

package com.unityTest.testrunner.models.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Models an author's information
 */
@AllArgsConstructor
@Data
@ApiModel(value = "Author")
public class Author {
    @ApiModelProperty(value = "First name", example = "John")
    private String firstname;

    @ApiModelProperty(value = "Last name", example = "Deer")
    private String lastname;
}

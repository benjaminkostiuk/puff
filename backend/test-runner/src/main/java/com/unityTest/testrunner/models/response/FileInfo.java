package com.unityTest.testrunner.models.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Models the information of a stored file
 */
@AllArgsConstructor
@Data
@ApiModel(value = "File", description = "Models a source code file from a submission")
class FileInfo {

    @NotBlank
    @ApiModelProperty(value = "File name", required = true, example = "Test.hs")
    private String name;

    @ApiModelProperty(value = "File size", example = "1245")
    private long size;
}

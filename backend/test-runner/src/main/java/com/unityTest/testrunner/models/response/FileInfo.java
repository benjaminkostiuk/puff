package com.unityTest.testrunner.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unityTest.testrunner.entity.SourceFile;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileInfo {

    @NotBlank
    @ApiModelProperty(value = "File name", required = true, example = "Test.hs")
    private String name;

    @ApiModelProperty(value = "File size", example = "1245")
    private long size;

    @ApiModelProperty(value = "File contents")
    private String content;

    public FileInfo(String name, long size) {
        this.name = name;
        this.size = size;
    }
}

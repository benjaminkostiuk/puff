package com.unityTest.testrunner.models.response;

import com.unityTest.testrunner.models.ResultStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@ApiModel(value = "TestResult", description = "Models the result of running a test case")
@Data
public class TestResult {

    @ApiModelProperty(value = "Id of case run")
    private int caseId;

    @ApiModelProperty(value = "Status of running case")
    private ResultStatus status;

    @ApiModelProperty(value = "Result message")
    private String message;
}

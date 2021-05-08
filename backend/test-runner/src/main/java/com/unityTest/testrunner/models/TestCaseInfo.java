package com.unityTest.testrunner.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Models the core information needed to create or modify a test case for a test suite
 * Used as the body of the request for the POST and PUT /case endpoints
 */
@AllArgsConstructor
@ApiModel(value = "TestCaseInfo", description = "Models the information required to creating or modifying a test case")
@Data
public class TestCaseInfo {

    // Id of suite that the test case belongs to
    @ApiModelProperty(value = "Test suite id", required = true)
    private int suiteId;

    // Programming language of test case
    @ApiModelProperty(value = "Language", required = true, example = "JAVA")
    @NotNull
    private PLanguage language;

    // Optional description of test case
    @ApiModelProperty(value = "Test case description", example = "Tests boundary case if x = 0")
    private String description;

    // Function name of test case, used in conjunction with the body to create a working code snippet
    // that will run the test case
    @ApiModelProperty(value = "Test case function name", required = true, example = "test_FuncA")
    @NotBlank
    @NotNull
    private String functionName;

    // Code body of test case, used in conjunction with the name to create a working code snippet representing
    // the function that will run the test
    @ApiModelProperty(value = "Test case function body code", required = true)
    @NotNull
    private String body;
}

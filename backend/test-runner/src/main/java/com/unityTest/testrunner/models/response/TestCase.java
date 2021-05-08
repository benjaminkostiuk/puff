package com.unityTest.testrunner.models.response;

import com.unityTest.testrunner.entity.Suite;
import com.unityTest.testrunner.models.PLanguage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Models a test case returned in response to a request
 */
@AllArgsConstructor
@ApiModel(value = "TestCase", description = "Models a test case in a test suite")
@Data
public class TestCase {

    @ApiModelProperty(value = "Id", required = true)
    private int id;

    // Test suite that the test case belongs to
    @ApiModelProperty(value = "Test suite", required = true)
    private Suite suite;

    // Description of test case
    @ApiModelProperty(value = "Test case description", example = "Tests boundary case if x = 0")
    private String description;

    // Programming language of test case
    @ApiModelProperty(value = "Language", required = true, example = "JAVA")
    private PLanguage language;

    // Author information of test case
    @ApiModelProperty(value = "Author name", required = true)
    private Author author;

    // Engagement stats for the test case, upvotes, comment count etc.
    @ApiModelProperty(value = "Engagement statistics", required = true)
    // TODO Change this to engagement using course-management endpoints
    private int upvotes;

    // Statistics for the test case, run count, pass count etc.
    @ApiModelProperty(value = "Test case statistics", required = true)
    private CaseStats stats;

    // Code snippet that runs with the test case
    @ApiModelProperty(value = "Test case code", required = true)
    private String code;
}

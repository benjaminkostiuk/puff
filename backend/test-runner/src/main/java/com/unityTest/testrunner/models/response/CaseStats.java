package com.unityTest.testrunner.models.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Statistics for a test case
 */
@AllArgsConstructor
@ApiModel(value = "CaseStats")
@Data
public class CaseStats {

    private int runCount;

    private int passCount;

    // TODO add something for last failed repeats here
}

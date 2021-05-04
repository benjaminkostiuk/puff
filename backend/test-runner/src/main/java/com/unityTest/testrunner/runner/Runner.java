package com.unityTest.testrunner.runner;

import com.unityTest.testrunner.entity.Case;
import com.unityTest.testrunner.entity.SuiteFile;
import com.unityTest.testrunner.models.response.TestResult;

import java.io.File;
import java.io.IOException;

/**
 * Code runner for a language that handles running test cases
 */
public interface Runner {

    /**
     * Run a test case
     * @param caze Test case to run
     * @param suiteFileName Test suite file name
     * @param path Path of working directory where suite file is located
     * @return TestResult of running the test case
     */
    TestResult runTestCase(Case caze, String suiteFileName, String path);

    /**
     * Write the suite file & test file content to the test suite file
     * @param testFile Test suite file to write to
     * @param suiteFile Suite file that contains content
     * @param caze Test case with code to write
     * @throws IOException if file does not exist, or other io-related operations
     */
    void writeToTestFile(File testFile, SuiteFile suiteFile, Case caze) throws IOException;

}

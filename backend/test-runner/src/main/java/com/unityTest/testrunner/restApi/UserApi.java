package com.unityTest.testrunner.restApi;

import com.unityTest.testrunner.models.page.SuitePage;
import com.unityTest.testrunner.models.page.TestCasePage;
import com.unityTest.testrunner.models.page.SubmissionEventPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Pageable;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@Api(value = "User API", tags = "User API", description = "Retrieve user information and perform user-specific actions")
@RequestMapping(value = "/user")
public interface UserApi extends BaseApi {

    /**
     * GET endpoint to retrieve test suites created by the user
     * Filter by id, assignment id, name and programming language
     * @return Pageable view of test suites matching query criteria created by the user
     */
    @ApiOperation(value = "Retrieve a pageable view of test suites created by the user", nickname = "getUserTestSuites", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/suites", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SuitePage> getUserTestSuites(
            @ApiIgnore Principal principal,
            Pageable pageable,
            @ApiParam("Suite id") @RequestParam(value = "id", required = false) Integer id,
            @ApiParam("Assignment id") @RequestParam(value = "assignmentId", required = false) Integer assignmentId,
            @ApiParam("Name") @RequestParam(value = "name", required = false) String name,
            @ApiParam("Programming language") @RequestParam(value = "lang", required = false) String lang
    );

    /**
     * GET endpoint to retrieve test cases written by the user
     * Filter by id, suite id, function name and programming language
     * @return Pageable view of test cases matching query criteria created by the user
     */
    @ApiOperation(value = "Retrieve a pageable view of test cases written by the user", nickname = "getUserTestCases", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/cases", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TestCasePage> getUserTestCases(
            @ApiIgnore Principal principal,
            Pageable pageable,
            @ApiParam("Test case id") @RequestParam(value = "id", required = false) Integer id,
            @ApiParam("Suite id") @RequestParam(value = "suiteId", required = false) Integer suiteId,
            @ApiParam("Function name") @RequestParam(value = "name", required = false) String functionName,
            @ApiParam("Programming language") @RequestParam(value = "lang", required = false) String lang
    );

    /**
     * GET endpoint to retrieve recent user code uploads
     * Filter by submission id
     * @return Pageable view of user code uploads that match query criteria
     */
    @ApiOperation(value = "Get a pageable view of user's recent code uploads", nickname = "getRecentUserCodeUploads", response = SubmissionEventPage.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/recentUploads", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SubmissionEventPage> getRecentUserCodeUploads(
            @ApiIgnore Principal principal,
            Pageable pageable,
            @ApiParam("Submission id") @RequestParam(value = "submissionId", required = false) Integer submissionId,
            @ApiParam("Assignment id") @RequestParam(value = "assignmentId", required = false) Integer assignmentId
    );
}

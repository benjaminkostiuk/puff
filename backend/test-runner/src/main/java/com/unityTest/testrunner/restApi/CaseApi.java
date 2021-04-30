package com.unityTest.testrunner.restApi;

import com.unityTest.testrunner.entity.Case;
import com.unityTest.testrunner.models.TestCaseInfo;
import com.unityTest.testrunner.models.page.TestCasePage;
import com.unityTest.testrunner.models.response.TestCase;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@Api(value = "Test case API", tags = "Test case API", description = "Manage and run test cases")
@Validated
@RequestMapping(value = "/case")
public interface CaseApi extends BaseApi {

    /**
     * POST endpoint to create a test case for a test suite
     * @return Test case created
     */
    @ApiOperation(value = "Create a test case", nickname = "createTestCase", response = TestCase.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({ @ApiResponse(code = 201, message = "Created", response = TestCase.class) })
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TestCase> createTestCase(
            @ApiIgnore Principal principal,
            @ApiParam(value = "Test case to create", required = true) @Valid @RequestBody TestCaseInfo testCaseInfo
    );

    /**
     * GET endpoint to retrieve test cases
     * Filter by id, suite id, function name and programming language
     * @return Pageable view of test cases that match query criteria
     */
    @ApiOperation(value = "Retrieve a pageable view of test cases", nickname = "getTestCases", response = TestCasePage.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TestCasePage> getTestCases(
            Pageable pageable,
            @ApiParam("Case id") @RequestParam(value = "id", required = false) Integer id,
            @ApiParam("Suite id") @RequestParam(value = "suiteId", required = false) Integer suiteId,
            @ApiParam("Function name") @RequestParam(value = "name", required = false) String functionName,
            @ApiParam("Programming language") @RequestParam(value = "lang", required = false) String lang
    );

    /**
     * PUT endpoint to update a test case
     * Updatable fields are *functionName*, *description* and *body*
     * @param caseId Id of case to update
     * @return Updated Case
     */
    @ApiOperation(value = "Update a test case", nickname = "updateTestCase", response = TestCase.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{caseId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<TestCase> updateTestCase(
            @ApiIgnore Principal principal,
            @ApiParam(value = "Test case id", required = true) @PathVariable(value = "caseId") Integer caseId,
            @ApiParam(value = "Test case to update", required = true) @Valid @RequestBody TestCaseInfo testCaseInfo
    );

    /**
     * DELETE endpoint to delete a test case
     * @param caseId Id of case to delete
     */
    @ApiOperation(value = "Delete a test case", nickname = "deleteTestCase")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{caseId}")
    void deleteTestCase(
            @ApiIgnore Principal principal,
            @ApiParam(value = "Test case id", required = true) @PathVariable(value = "caseId") Integer caseId
    );

    /**
     * POST endpoint to update test case vote count
     * SYSTEM LEVEL endpoint only accessible with ROLE_SYS
     * @param caseId Id of case
     * @param action Vote action
     */
    @ApiOperation(value = "Vote on a test case", nickname = "voteTestCase")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PostMapping(value = "/{caseId}/vote")
    void voteOnTestCase(
            @ApiParam(value = "Test case id", required = true) @PathVariable(value = "caseId") Integer caseId,
            @ApiParam(value = "Vote action", required = true) @RequestParam(value = "action") String action
    );
}

package com.unityTest.testrunner.restApi;

import com.unityTest.testrunner.entity.Suite;
import com.unityTest.testrunner.models.SuitePage;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.validation.Valid;
import org.springframework.data.domain.Pageable;

@Api(value = "Test suite API", tags = "Test suite API", description = "Manage and run test suites")
@Validated
@RequestMapping(value = "/suite")
public interface SuiteApi extends BaseApi {

    /**
     * POST endpoint to create a test suite
     * @return Suite created
     */
    @ApiOperation(value = "Create a test suite", nickname = "createTestSuite", response = Suite.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({ @ApiResponse(code = 201, message = "Created", response = Suite.class) })
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Suite> createTestSuite(@ApiParam(value = "Test suite to create", required = true) @Valid @RequestBody Suite suite);

    /**
     * GET endpoint to retrieve test suites
     * Filter by assignment id, name and programming language
     * @return Pageable view of test suites matching query criteria
     */
    @ApiOperation(value = "Retrieve a pageable view of test suites", nickname = "getTestSuites", response = SuitePage.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SuitePage> getTestSuites(
            Pageable pageable,
            @ApiParam("Suite id") @RequestParam(value = "id", required = false) Integer id,
            @ApiParam("Assignment id") @RequestParam(value = "assignmentId", required = false) Integer assignmentId,
            @ApiParam("Name") @RequestParam(value = "name", required = false) String name,
            @ApiParam("Programming language") @RequestParam(value = "lang", required = false) String lang
    );

    /**
     * DELETE endpoint to delete a test suite
     * @param suiteId Id of suite to delete
     */
    @ApiOperation(value = "Delete a test suite", nickname = "deleteTestSuite")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{suiteId}")
    void deleteTestSuite(@ApiParam(value = "Test suite id", required = true) @PathVariable(value = "suiteId") Integer suiteId);

    /**
     * POST endpoint to run a test suite using source files from a submission
     * @return
     */
    // TODO Update this to use different response
    @ApiOperation(value = "Run a test suite", nickname = "runTestSuite", response = Suite.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/run", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseBodyEmitter> runTestSuite(
            @ApiParam(value = "Test suite id", required = true) @RequestParam(value = "id") Integer suiteId,
            @ApiParam(value = "Submission id") @RequestParam(value = "submissionId", required = false) Integer submissionId
    );
}

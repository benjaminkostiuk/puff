package com.unityTest.testrunner.restApi;

import com.unityTest.testrunner.entity.Suite;
import com.unityTest.testrunner.models.response.FileInfo;
import com.unityTest.testrunner.models.page.SuitePage;
import com.unityTest.testrunner.models.response.FileUploadEvent;
import com.unityTest.testrunner.models.response.TestResult;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

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
    ResponseEntity<Suite> createTestSuite(
            @ApiIgnore Principal principal,
            @ApiParam(value = "Test suite to create", required = true) @Valid @RequestBody Suite suite
    );

    /**
     * POST endpoint to set the test file for a test suite
     * @return FileUploadEvent on successful file upload
     */
    @ApiOperation(value = "Upload the testing file for a test suite", nickname = "setTestSuiteFile", response = FileUploadEvent.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/{suiteId}/setFile", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<FileUploadEvent> setTestSuiteFile(
            @ApiIgnore Principal principal,
            @ApiParam(value = "Test suite id", required = true) @PathVariable(value = "suiteId") Integer suiteId,
            @ApiParam(value = "File to set", required = true) @RequestParam("file") MultipartFile file) throws IOException;

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
     * GET endpoint to retrieve contents of the test file set for a test suite
     * @return FileInfo about the file set of test suite
     */
    @ApiOperation(value = "Get the contents of a test file for a test suite", nickname = "getTestSuiteFile", response = FileInfo.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{suiteId}/getFile", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<FileInfo> getTestSuiteFile(@ApiParam(value = "Test suite id", required = true) @PathVariable(value = "suiteId") Integer suiteId);

    /**
     * DELETE endpoint to delete a test suite
     * @param suiteId Id of suite to delete
     */
    @ApiOperation(value = "Delete a test suite", nickname = "deleteTestSuite")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{suiteId}")
    void deleteTestSuite(
            @ApiIgnore Principal principal,
            @ApiParam(value = "Test suite id", required = true) @PathVariable(value = "suiteId") Integer suiteId
    );

    /**
     * POST endpoint to update test suite vote count
     * SYSTEM LEVEL endpoint only accessible with ROLE_SYS
     * @param suiteId Id of case
     * @param action Vote action
     */
    @ApiOperation(value = "Vote on a test case", nickname = "voteTestSuite")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PostMapping(value = "/{suiteId}/vote")
    void voteOnTestSuite(
            @ApiParam(value = "Test suite id", required = true) @PathVariable(value = "suiteId") Integer suiteId,
            @ApiParam(value = "Vote action", required = true) @RequestParam(value = "action") String action
    );

    /**
     * POST endpoint to run test cases in test suite using source files from a submission
     * @return Stream of TestResults from running the test cases
     */
    @ApiOperation(value = "Run test cases in a test suite", nickname = "runTestCases", response = TestResult.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/{suiteId}/run", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseBodyEmitter> runTestSuite(
            @ApiIgnore Principal principal,
            @ApiParam(value = "Test suite id", required = true) @PathVariable(value = "suiteId") Integer suiteId,
            @ApiParam(value = "Test case ids") @RequestParam(value = "ids", required = false) List<Integer> ids,
            @ApiParam(value = "Limit on number of cases run", defaultValue = "20") @RequestParam(value = "limit", required = false) Integer limit,
            @ApiParam(value = "Submission id") @RequestParam(value = "submissionId", required = false) Integer submissionId
    );
}

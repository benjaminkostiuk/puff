package com.unityTest.testrunner.restApi;

import com.unityTest.testrunner.entity.Case;
import com.unityTest.testrunner.models.CasePage;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Api(value = "Test case API", tags = "Test case API", description = "Manage and run test cases")
@Validated
@RequestMapping(value = "/case")
public interface CaseApi extends BaseApi {

    /**
     * POST endpoint to create a test case for a test suite
     * @return Case created
     */
    @ApiOperation(value = "Create a test case", nickname = "createTestCase", response = Case.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({ @ApiResponse(code = 201, message = "Created", response = Case.class) })
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Case> createTestCase(@ApiParam(value = "Test case to create", required = true) @Valid @RequestBody Case testCase);

    /**
     * GET endpoint to retrieve test cases
     * Filter by suite id and programming language
     * @return Pageable view of test cases that match query criteria
     */
    @ApiOperation(value = "Retrieve a pageable view of test cases", nickname = "getTestCases", response = CasePage.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CasePage> getTestCases(
            Pageable pageable,
            @ApiParam("Suite id") @RequestParam(value = "suiteId", required = false) Integer suiteId,
            @ApiParam("Programming language") @RequestParam(value = "lang", required = false) String language
    );

    /**
     * PUT endpoint to update a test case
     * Updatable fields are description and code
     * @return Updated Case
     */
    @ApiOperation(value = "Update a test case", nickname = "updateTestCase", response = Case.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Case> updateTestCase(@ApiParam(value = "Test case to update", required = true) @Valid @RequestBody Case testCase);

    /**
     * DELETE endpoint to delete a test case
     * @param caseId Id of case to delete
     */
    @ApiOperation(value = "Delete a test case", nickname = "deleteTestCase")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{caseId}")
    void deleteTestCase(@ApiParam(value = "Test case id", required = true) @PathVariable(value = "caseId") Integer caseId);

    /**
     * POST endpoint to compile a test case code in its programming language
     * @return TODO figure this out
     */
    @ApiOperation(value = "Compile a test case", nickname = "compileTestCase", response = Case.class, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/compile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseBodyEmitter> compileTestCase(@ApiParam(value = "Test case to compile", required = true) @Valid @RequestBody Case testCase);

    /**
     * POST endpoint to run test cases
     * Pass case ids to run with the ids query. Optionally specify the source file submission id to use.
     * @return TODO figure this out
     */
    @ApiOperation(value = "Run test cases", nickname = "runTestCases", response = Case.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/run", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResponseBodyEmitter> runTestCases(
            @ApiParam(value = "Test case ids", required = true) @RequestParam(value = "ids") List<Integer> ids,
            @ApiParam(value = "Submission id") @RequestParam(value = "submissionId", required = false) Integer submissionId
    );
}

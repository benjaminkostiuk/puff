package com.unityTest.testrunner.restApi;

import com.unityTest.testrunner.models.response.Submission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

@Api(value = "Submissions API", tags = "Submissions API", description = "Upload source files to run against test cases")
public interface SubmissionApi extends BaseApi {

    /**
     * POST endpoint to upload source code files
     * @return TODO figure this out
     */
    @ApiOperation(value = "Upload source code files", nickname = "uploadSourceFiles", response = Submission.class, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Submission> uploadSourceFiles(
            @ApiParam(name = "files", required = true) @RequestParam("files") MultipartFile[] files,
            @ApiParam(name = "assignmentId", required = true) @RequestParam("assignmentId") Integer assignmentId) throws IOException;

    @ApiOperation(value = "Download source code upload", nickname = "downloadSourceFiles")
    @GetMapping(value = "/download", produces = "application/zip")
    void downloadSourceFiles(
            OutputStream out
            //@ApiParam(name = "Submission id", required = true) @PathVariable(value = "submissionId") Integer submissionId
    ) throws FileNotFoundException;
}

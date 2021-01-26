package com.unityTest.testrunner.restApi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(value = "Submissions API", tags = "Submissions API", description = "Upload source files to run against test cases")
public interface SubmissionApi extends BaseApi {

    /**
     * POST endpoint to upload source code files
     * @return TODO figure this out
     */
    @ApiOperation(value = "Upload source code files", nickname = "uploadSourceFiles", response = String.class, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> uploadSourceFiles(@ApiParam(name = "files", required = true) @RequestParam("files") MultipartFile[] files) throws IOException;
}

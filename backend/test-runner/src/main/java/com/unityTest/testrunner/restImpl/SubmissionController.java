package com.unityTest.testrunner.restImpl;

import com.unityTest.testrunner.restApi.SubmissionApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class SubmissionController implements SubmissionApi {
    @Override
    public ResponseEntity<String> uploadSourceFiles(MultipartFile[] files) {
        return null;
    }
}

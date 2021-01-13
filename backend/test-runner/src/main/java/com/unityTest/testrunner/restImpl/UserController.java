package com.unityTest.testrunner.restImpl;

import com.unityTest.testrunner.models.CasePage;
import com.unityTest.testrunner.restApi.UserApi;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {
    @Override
    public ResponseEntity<CasePage> getUserTestCases(Pageable pageable, Integer id, Integer suiteId) {
        return null;
    }

    @Override
    public ResponseEntity<String> getUserCodeUploads(Pageable pageable, Integer submissionId) {
        return null;
    }
}

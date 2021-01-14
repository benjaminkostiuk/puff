package com.unityTest.testrunner.restImpl;

import com.unityTest.testrunner.entity.Case;
import com.unityTest.testrunner.models.CasePage;
import com.unityTest.testrunner.restApi.CaseApi;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
public class CaseController implements CaseApi {
    @Override
    public ResponseEntity<Case> createTestCase(@Valid Case testCase) {
        return null;
    }

    @Override
    public ResponseEntity<CasePage> getTestCases(Pageable pageable, Integer suiteId, String language) {
        CasePage page = null;
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Case> updateTestCase(@Valid Case testCase) {
        return null;
    }

    @Override
    public void deleteTestCase(Integer caseId) {

    }

    @Override
    public ResponseEntity<ResponseBodyEmitter> compileTestCase(@Valid Case testCase) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBodyEmitter> runTestCases(List<Integer> ids, Integer submissionId) {
        return null;
    }
}

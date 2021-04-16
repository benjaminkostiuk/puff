package com.unityTest.testrunner.restImpl;

import com.unityTest.testrunner.entity.Suite;
import com.unityTest.testrunner.models.PLanguage;
import com.unityTest.testrunner.models.page.SuitePage;
import com.unityTest.testrunner.restApi.SuiteApi;
import com.unityTest.testrunner.serviceImpl.SuiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import org.springframework.data.domain.Pageable;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

/**
 * Rest Controller for the /suite endpoint
 */
@RestController
public class SuiteController implements SuiteApi {

    @Autowired
    private SuiteService suiteService;

    @Override
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<Suite> createTestSuite(@Valid Suite suite) {
        return new ResponseEntity<>(suiteService.createSuite(suite), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<SuitePage> getTestSuites(Pageable pageable, Integer id, Integer assignmentId, String name, String lang) {
        // Convert lang to PLanguage
        PLanguage pLanguage = null;
        if(lang != null) {
            try { pLanguage = PLanguage.valueOf(lang); }
            catch(IllegalArgumentException e) { throw new HttpMessageNotReadableException("Not of of accepted values"); }
        }
        // Retrieve results using service
        SuitePage page = new SuitePage(suiteService.getSuites(pageable, id, assignmentId, name, pLanguage));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Override
    @RolesAllowed("ROLE_ADMIN")
    public void deleteTestSuite(Integer suiteId) {
        suiteService.deleteSuite(suiteId);
    }

    @Override
    public ResponseEntity<ResponseBodyEmitter> runTestSuite(Integer suiteId, Integer submissionId) {
        return null;
    }
}

package com.unityTest.testrunner.restImpl;

import com.unityTest.testrunner.entity.Suite;
import com.unityTest.testrunner.entity.SuiteFile;
import com.unityTest.testrunner.models.PLanguage;
import com.unityTest.testrunner.models.page.SuitePage;
import com.unityTest.testrunner.models.response.FileInfo;
import com.unityTest.testrunner.models.response.FileUploadEvent;
import com.unityTest.testrunner.restApi.SuiteApi;
import com.unityTest.testrunner.service.SuiteService;
import com.unityTest.testrunner.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import org.springframework.data.domain.Pageable;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

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

    // TODO ADD ACCESS CONTROL TO ENDPOINT
    @Override
    public ResponseEntity<FileUploadEvent> setTestSuiteFile(Principal principal, Integer suiteId, MultipartFile file) throws IOException {
        // Get author id
        String authorId = Utils.getAuthToken(principal).getSubject();
        // Save file uploaded to repository
        SuiteFile suiteFile = suiteService.setSuiteTestFile(suiteId, authorId, file);
        // Create confirmation upload event
        FileUploadEvent resp = new FileUploadEvent();
        resp.addFile(suiteFile.getFileName(), suiteFile.getFileSize());
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SuitePage> getTestSuites(Pageable pageable, Integer id, Integer assignmentId, String name, String lang) {
        // Convert lang to PLanguage
        PLanguage pLanguage = null;
        if(lang != null) {
            try { pLanguage = PLanguage.valueOf(lang); }
            catch(IllegalArgumentException e) { throw new HttpMessageNotReadableException("Not one of accepted values"); }
        }
        // Retrieve results using service
        SuitePage page = new SuitePage(suiteService.getSuites(pageable, id, assignmentId, name, pLanguage));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<FileInfo> getTestSuiteFile(Integer suiteId) {
        SuiteFile suiteFile = suiteService.getSuiteTestFile(suiteId);
        return new ResponseEntity<>(new FileInfo(suiteFile.getFileName(), suiteFile.getFileSize(), new String(suiteFile.getContent(), StandardCharsets.UTF_8)), HttpStatus.OK);
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

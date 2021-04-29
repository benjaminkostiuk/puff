package com.unityTest.testrunner.restImpl;

import com.unityTest.testrunner.entity.Suite;
import com.unityTest.testrunner.entity.SuiteFile;
import com.unityTest.testrunner.models.PLanguage;
import com.unityTest.testrunner.models.VoteAction;
import com.unityTest.testrunner.models.page.SuitePage;
import com.unityTest.testrunner.models.response.FileInfo;
import com.unityTest.testrunner.models.response.FileUploadEvent;
import com.unityTest.testrunner.restApi.SuiteApi;
import com.unityTest.testrunner.service.SuiteService;
import com.unityTest.testrunner.utils.Utils;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
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
    public ResponseEntity<Suite> createTestSuite(Principal principal, @Valid Suite suite) {
        String authorId = Utils.getAuthToken(principal).getSubject();       // Get author id from token
        suite.setAuthorId(authorId);        // Set author id
        suite.setUpvotes(0);                // Set upvote count to 0

        return new ResponseEntity<>(suiteService.createSuite(suite), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<FileUploadEvent> setTestSuiteFile(Principal principal, Integer suiteId, MultipartFile file) throws IOException {
        AccessToken token = Utils.getAuthToken(principal);      // Get request token

        // Verify that the user uploading the file is the same as created the suite
        Suite suite = suiteService.getSuiteById(suiteId);
        if(!Utils.isAuthorOrAdmin(token, suite.getAuthorId())) throw new AccessDeniedException("Access Denied");

        // Save file uploaded to repository
        SuiteFile suiteFile = suiteService.setSuiteTestFile(suiteId, token.getSubject(), file);
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
    public void deleteTestSuite(Principal principal, Integer suiteId) {
        AccessToken token = Utils.getAuthToken(principal);              // Get request token
        Suite suiteToDelete = suiteService.getSuiteById(suiteId);       // Find suite to delete

        // Check that user deleting is author or admin
        if(!Utils.isAuthorOrAdmin(token, suiteToDelete.getAuthorId())) throw new AccessDeniedException("Access Denied");
        // If allowed, delete the test suite
        suiteService.deleteSuite(suiteId);
    }

    @Override
    @RolesAllowed("ROLE_SYS")
    public void voteOnTestSuite(Integer suiteId, String action) {
        // Convert action to VoteAction
        VoteAction voteAction;
        try {
            voteAction = VoteAction.valueOf(action);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Not one of accepted values for vote action");
        }
        suiteService.updateSuiteUpvotes(suiteId, voteAction);
    }

    @Override
    public ResponseEntity<ResponseBodyEmitter> runTestSuite(Integer suiteId, Integer submissionId) {
        return null;
    }
}

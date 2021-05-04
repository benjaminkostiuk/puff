package com.unityTest.testrunner.restImpl;

import com.unityTest.testrunner.entity.*;
import com.unityTest.testrunner.models.PLanguage;
import com.unityTest.testrunner.models.VoteAction;
import com.unityTest.testrunner.models.page.SuitePage;
import com.unityTest.testrunner.models.response.FileInfo;
import com.unityTest.testrunner.models.response.FileUploadEvent;
import com.unityTest.testrunner.restApi.SuiteApi;
import com.unityTest.testrunner.service.CaseService;
import com.unityTest.testrunner.service.CodeService;
import com.unityTest.testrunner.service.SubmissionService;
import com.unityTest.testrunner.service.SuiteService;
import com.unityTest.testrunner.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.List;

/**
 * Rest Controller for the /suite endpoint
 */
@Slf4j
@RestController
public class SuiteController implements SuiteApi {

    @Autowired
    private SuiteService suiteService;

    @Autowired
    private CaseService caseService;

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private CodeService codeService;

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
        PLanguage pLanguage = Utils.parsePLanguage(lang);
        // Retrieve results using service
        SuitePage page = new SuitePage(suiteService.getSuites(pageable, id, assignmentId, name, pLanguage, null));
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
        VoteAction voteAction = Utils.parseVoteAction(action);
        suiteService.updateSuiteUpvotes(suiteId, voteAction);
    }

    @Override
    public ResponseEntity<ResponseBodyEmitter> runTestSuite(Principal principal, Integer suiteId, List<Integer> ids, Integer limit, Integer submissionId) {
        Suite suite = suiteService.getSuiteById(suiteId);                   // Get suite with id
        SuiteFile suiteFile = suiteService.getSuiteTestFile(suiteId);       // Get suiteFile with suite id
        String authorId = Utils.getAuthToken(principal).getSubject();       // Get requester's authorId

        // Find submission by id if present, otherwise find the latest submission
        Submission submission = submissionId != null
                ? submissionService.getSubmissionById(submissionId)
                : submissionService.getLatestSubmissionForAssignment(authorId, suite.getAssignmentId());

        final int DEFAULT_LIMIT = 20;
        // Get the list of test cases to run. If no ids are provided take the top x ranked by upvotes, where x is limit (default 20)
        List<Case> cases;
        if(ids == null || ids.size() == 0) {
            int maxCaseCount = limit == null ? DEFAULT_LIMIT : limit;
            Pageable pageable = PageRequest.of(0, maxCaseCount, Sort.Direction.DESC, Case_.UPVOTES);
            cases = caseService.getCases(pageable, null, suiteId, null, null, null).getContent();
        } else {
            cases = caseService.getCases(ids, suiteId);
        }
        // Create emitter to send back results
        final ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        try {
            codeService.runTestCasesInSuite(emitter, submission, suite, suiteFile, cases);
        } catch (Exception e) {
            log.error(e.getMessage());
            emitter.completeWithError(e);
        }
        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }
}

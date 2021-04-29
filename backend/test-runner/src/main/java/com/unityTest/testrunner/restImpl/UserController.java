package com.unityTest.testrunner.restImpl;

import com.unityTest.testrunner.entity.Case;
import com.unityTest.testrunner.entity.Submission;
import com.unityTest.testrunner.entity.Submission_;
import com.unityTest.testrunner.models.PLanguage;
import com.unityTest.testrunner.models.page.SuitePage;
import com.unityTest.testrunner.models.page.TestCasePage;
import com.unityTest.testrunner.models.page.SubmissionEventPage;
import com.unityTest.testrunner.models.response.Author;
import com.unityTest.testrunner.repository.SubmissionRepository;
import com.unityTest.testrunner.restApi.UserApi;
import com.unityTest.testrunner.service.CaseService;
import com.unityTest.testrunner.service.SuiteService;
import com.unityTest.testrunner.utils.Utils;
import com.unityTest.testrunner.utils.specification.AndSpecification;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;

/**
 * Rest controller for the /user/* endpoints
 */
@RestController
public class UserController implements UserApi {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private CaseService caseService;

    @Autowired
    private SuiteService suiteService;

    @Override
    public ResponseEntity<SuitePage> getUserTestSuites(Principal principal, Pageable pageable, Integer id, Integer assignmentId, String name, String lang) {
        // Extract author id from access token
        String authorId = Utils.getAuthToken(principal).getSubject();
        // Convert lang to PLanguage
        PLanguage pLanguage = Utils.parsePLanguage(lang);
        // Retrieve results using service
        SuitePage page = new SuitePage(suiteService.getSuites(pageable, id, assignmentId, name, pLanguage, authorId));
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TestCasePage> getUserTestCases(Principal principal, Pageable pageable, Integer id, Integer suiteId, String functionName, String lang) {
        AccessToken token = Utils.getAuthToken(principal);                          // Extract request access token
        Author author = new Author(token.getGivenName(), token.getFamilyName());    // Constructor author obj from token

        // Convert lang to PLanguage
        PLanguage pLanguage = Utils.parsePLanguage(lang);
        // Call case service and build TestCasePage to return
        Page<Case> page = caseService.getCases(pageable, id, suiteId, functionName, pLanguage, token.getSubject());
        return new ResponseEntity<>(new TestCasePage(page, author), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SubmissionEventPage> getRecentUserCodeUploads(Principal principal, Pageable pageable, Integer submissionId, Integer assignmentId) {
        // Get the id of caller from authentication token
        String requesterId = Utils.getAuthToken(principal).getSubject();

        // Retrieve all SourceFile with matching author id, from 10 days ago
        final long DAY_IN_MS = 1000 * 60 * 60 * 24;
        final Date TEN_DAYS_AGO = new Date(System.currentTimeMillis() - (10 * DAY_IN_MS));
        // Build specification
        Specification<Submission> spec = new AndSpecification<Submission>()
                .equal(requesterId, Submission_.AUTHOR_ID)
                .equal(submissionId, Submission_.ID)
                .equal(assignmentId, Submission_.ASSIGNMENT_ID)
                .after(TEN_DAYS_AGO, Submission_.SUBMISSION_DATE).getSpec();

        Page<Submission> submissionPage = submissionRepository.findAll(spec, pageable);

        // Return submissions list as response
        return new ResponseEntity<>(new SubmissionEventPage(submissionPage), HttpStatus.OK);
    }
}

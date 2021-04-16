package com.unityTest.testrunner.restImpl;

import com.unityTest.testrunner.entity.Submission;
import com.unityTest.testrunner.entity.Submission_;
import com.unityTest.testrunner.models.page.CasePage;
import com.unityTest.testrunner.models.page.SubmissionEventPage;
import com.unityTest.testrunner.models.response.SubmissionEvent;
import com.unityTest.testrunner.repository.SourceFileRepository;
import com.unityTest.testrunner.repository.SubmissionRepository;
import com.unityTest.testrunner.restApi.UserApi;
import com.unityTest.testrunner.utils.Utils;
import com.unityTest.testrunner.utils.specification.AndSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Rest controller for the /user/* endpoints
 */
@RestController
public class UserController implements UserApi {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Override
    public ResponseEntity<CasePage> getUserTestCases(Pageable pageable, Integer id, Integer suiteId) {
        return null;
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

package com.unityTest.testrunner.service;

import com.unityTest.testrunner.entity.Submission;
import com.unityTest.testrunner.exception.ElementNotFoundException;
import com.unityTest.testrunner.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    /**
     * Save a submission to the repository
     * @param submission Submission to save
     * @return Submission saved to repository
     */
    public Submission saveSubmission(Submission submission) {
        return submissionRepository.save(submission);
    }

    /**
     * Fetch submission by id from repository
     * @param id Id of submission to fetch
     * @return Submission with matching id
     * @throws ElementNotFoundException if submission with matching id cannot be found
     */
    public Submission getSubmissionById(int id) {
        // Fetch Submission from repository
        Optional<Submission> opt = submissionRepository.findById(id);
        // Throw exception if submission with id does not exist
        if(!opt.isPresent()) throw new ElementNotFoundException(Submission.class, "id", String.valueOf(id));
        return opt.get();
    }

    public Submission getLatestSubmissionForAssignment(String authorId, int assignmentId) {
        Optional<Submission> opt = submissionRepository.findTopByAuthorIdAndAssignmentIdOrderByIdDesc(authorId, assignmentId);
        // Throw exception if a submission cannot be found
        if(!opt.isPresent()) throw new ElementNotFoundException(Submission.class, "assignmentId", String.valueOf(assignmentId));
        return opt.get();
    }
}

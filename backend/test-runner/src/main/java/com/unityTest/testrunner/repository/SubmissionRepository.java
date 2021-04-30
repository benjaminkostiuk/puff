package com.unityTest.testrunner.repository;

import com.unityTest.testrunner.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Integer>, JpaSpecificationExecutor<Submission> {
    Optional<Submission> findTopByAuthorIdAndAssignmentIdOrderByIdDesc(String authorId, int assignmentId);
}

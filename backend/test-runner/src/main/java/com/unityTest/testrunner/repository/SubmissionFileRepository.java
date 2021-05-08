package com.unityTest.testrunner.repository;

import com.unityTest.testrunner.entity.SubmissionFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionFileRepository extends JpaRepository<SubmissionFile, Integer>, JpaSpecificationExecutor<SubmissionFile> {
}

package com.unityTest.testrunner.repository;

import com.unityTest.testrunner.entity.SourceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceFileRepository extends JpaRepository<SourceFile, Integer>, JpaSpecificationExecutor<SourceFile> {

    /**
     * Obtain the next submission id to group submissions by
     * @return Next value in the submission id sequence
     */
    @Query(value = "SELECT SOURCE_FILE_SUBMISSION_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
    Integer nextSubmissionId();

    /**
     * Get list of all source files in a submission
     * @param submissionId Submission id to search by
     * @return List of source files in a submission
     */
    List<SourceFile> getSourceFilesBySubmissionId(Integer submissionId);
}

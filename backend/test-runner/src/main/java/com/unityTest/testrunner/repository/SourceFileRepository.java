package com.unityTest.testrunner.repository;

import com.unityTest.testrunner.entity.SourceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceFileRepository extends JpaRepository<SourceFile, Integer>, JpaSpecificationExecutor<SourceFile> {
}

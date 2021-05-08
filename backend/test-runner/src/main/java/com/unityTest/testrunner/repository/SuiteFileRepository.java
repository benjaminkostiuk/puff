package com.unityTest.testrunner.repository;

import com.unityTest.testrunner.entity.SuiteFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SuiteFileRepository extends JpaRepository<SuiteFile, Integer>, JpaSpecificationExecutor<SuiteFile> {
}

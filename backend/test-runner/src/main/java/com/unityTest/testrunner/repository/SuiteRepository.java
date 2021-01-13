package com.unityTest.testrunner.repository;

import com.unityTest.testrunner.entity.Suite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SuiteRepository extends JpaRepository<Suite, Integer>, JpaSpecificationExecutor<Suite> {
}

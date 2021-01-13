package com.unityTest.testrunner.repository;

import com.unityTest.testrunner.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseRepository extends JpaRepository<Case, Integer>, JpaSpecificationExecutor<Case> {
}

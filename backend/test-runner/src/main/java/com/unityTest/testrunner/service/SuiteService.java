package com.unityTest.testrunner.service;

import com.unityTest.testrunner.entity.Suite;
import com.unityTest.testrunner.models.PLanguage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SuiteService {
    Suite createSuite(Suite suite);

    List<Suite> getSuites(Integer id, Integer assignmentId, String name, PLanguage language);

    Page<Suite> getSuites(Pageable pageable, Integer id, Integer assignmentId, String name, PLanguage language);

    Suite getSuiteById(int id);

    void deleteSuite(int id);
}

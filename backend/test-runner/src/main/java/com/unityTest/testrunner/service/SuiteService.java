package com.unityTest.testrunner.service;

import com.unityTest.testrunner.entity.Suite;
import com.unityTest.testrunner.entity.SuiteFile;
import com.unityTest.testrunner.exception.ElementNotFoundException;
import com.unityTest.testrunner.models.PLanguage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SuiteService {
    Suite createSuite(Suite suite);

    SuiteFile setSuiteTestFile(int suiteId, String authorId, MultipartFile file) throws IOException;

    List<Suite> getSuites(Integer id, Integer assignmentId, String name, PLanguage language);

    Page<Suite> getSuites(Pageable pageable, Integer id, Integer assignmentId, String name, PLanguage language);

    SuiteFile getSuiteTestFile(int suiteId);

    Suite getSuiteById(int id) throws ElementNotFoundException;

    void deleteSuite(int id);
}

package com.unityTest.testrunner.service;

import com.unityTest.testrunner.entity.Case;
import com.unityTest.testrunner.exception.ElementNotFoundException;
import com.unityTest.testrunner.models.PLanguage;
import com.unityTest.testrunner.models.TestCaseInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CaseService {

    Case createCase(Case testCase);

    Case createCase(TestCaseInfo caseInfo, String authorId);

    List<Case> getCases(Integer id, Integer suiteId, String functionName, PLanguage pLanguage, String authorId);

    Page<Case> getCases(Pageable pageable, Integer id, Integer suiteId, String functionName, PLanguage pLanguage, String authorId);

    Case getCaseById(int id) throws ElementNotFoundException;

    Case updateCase(int id, TestCaseInfo testCaseInfo);

    void deleteCase(int id);
}

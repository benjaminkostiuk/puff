package com.unityTest.testrunner.service;

import com.unityTest.testrunner.entity.Case;
import com.unityTest.testrunner.entity.Case_;
import com.unityTest.testrunner.entity.Suite;
import com.unityTest.testrunner.entity.Suite_;
import com.unityTest.testrunner.exception.ElementNotFoundException;
import com.unityTest.testrunner.exception.ProgrammingLanguageMismatch;
import com.unityTest.testrunner.models.PLanguage;
import com.unityTest.testrunner.models.TestCaseInfo;
import com.unityTest.testrunner.models.VoteAction;
import com.unityTest.testrunner.repository.CaseRepository;
import com.unityTest.testrunner.utils.specification.AndSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CaseService {

    @Autowired
    private SuiteService suiteService;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private CodeService codeService;

    /**
     * Create a case
     * @param testCase Case to create
     * @return Case created
     */
    public Case createCase(Case testCase) {
        // Verify the programming languages match between the case and the suite
        if(testCase.getLanguage() != testCase.getSuite().getLanguage()) throw new ProgrammingLanguageMismatch(testCase.getSuite().getLanguage(), testCase.getLanguage());
        return caseRepository.save(testCase);
    }

    /**
     * Create a case using the information in a TestCaseInfo
     * @param caseInfo Information to create a case
     * @return Case created
     */
    public Case createCase(TestCaseInfo caseInfo, String authorId) {
        // Find test suite for case
        Suite suite = suiteService.getSuiteById(caseInfo.getSuiteId());
        // Verify the programming languages match
        if(caseInfo.getLanguage() != suite.getLanguage()) throw new ProgrammingLanguageMismatch(suite.getLanguage(), caseInfo.getLanguage());
        // Get test case code
        String code = codeService.buildTestCaseCode(caseInfo.getLanguage(), caseInfo.getFunctionName(), caseInfo.getBody());
        // Build case to save
        Case caseToCreate = new Case(suite, authorId, caseInfo.getFunctionName(), caseInfo.getDescription(), caseInfo.getLanguage(), code);
        return this.createCase(caseToCreate);
    }

    /**
     * Get a list of test cases that match the passed arguments
     * @param id Id of case to fetch
     * @param suiteId Suite id to match
     * @param functionName Function name of case to match
     * @param pLanguage Programming language of case to match
     * @param authorId Author id of case to match
     * @return List of cases with fields matching the passed arguments
     */
    public List<Case> getCases(Integer id, Integer suiteId, String functionName, PLanguage pLanguage, String authorId) {
        return getCases(Pageable.unpaged(), id, suiteId, functionName, pLanguage, authorId).getContent();
    }

    /**
     * Get a Page view of test cases that match the passed arguments
     * @param pageable Pageable object specifying page size, sort and index
     * @param id Id of case to fetch
     * @param suiteId Suite id to match
     * @param functionName Function name of case to match
     * @param pLanguage Programming language of case to match
     * @param authorId Author id of case to match
     * @return Page view of test cases from repository matching passed arguments and formatted by the pageable param
     */
    public Page<Case> getCases(Pageable pageable, Integer id, Integer suiteId, String functionName, PLanguage pLanguage, String authorId) {
        // Build specification
        Specification<Case> spec = new AndSpecification<Case>()
                .equal(id, Case_.ID)
                .equal(suiteId, Case_.SUITE, Suite_.ID)
                .equal(functionName, Case_.FUNCTION_NAME)
                .equal(pLanguage, Case_.LANGUAGE)
                .equal(authorId, Case_.AUTHOR_ID).getSpec();

        return caseRepository.findAll(spec, pageable);
    }

    /**
     * Get a list of test cases that match the given list of ids
     * Optionally specify that all cases are part of the same suite
     * @param ids List of case ids to retrieve from the repo
     * @param suiteId Suite id that cases should be part of, otherwise null
     * @return List of test cases with the given ids, constrained by the given suite id
     */
    public List<Case> getCases(List<Integer> ids, Integer suiteId) throws ElementNotFoundException {
        // Find all cases with ids
        List<Case> cases = caseRepository.findAllById(ids);

        // Check that all cases have been found, if not throw exception
        if(ids.size() != cases.size()) {
            Set<Integer> toBeFound = new HashSet<>(ids);
            for(Case caze: cases) {
                toBeFound.remove(caze.getId());
            }
            Integer notFound = toBeFound.iterator().next();
            throw new ElementNotFoundException(Case.class, "id", String.valueOf(notFound));
        }

        // If suite id is specified constrain all cases to be part of the suite
        if(suiteId != null) {
            for(Case caze: cases) {
                if(caze.getSuite().getId() != suiteId) throw new ElementNotFoundException(Case.class, "id", String.valueOf(caze.getId()), "suite id", String.valueOf(suiteId));
            }
        }
        return cases;
    }

    /**
     * Find a case by id
     * @param id Id of case to find
     * @return Case with matching id from JPA repo
     * @throws ElementNotFoundException if no case with id exists
     */
    public Case getCaseById(int id) throws ElementNotFoundException {
        Optional<Case> opt = caseRepository.findById(id);
        // Throw exception if case does not exist
        if(!opt.isPresent()) throw new ElementNotFoundException(Case.class, "id", String.valueOf(id));
        return opt.get();
    }

    /**
     * Update case, updateable fields are *functionName*, *description* and *code*
     * @param id Id of case to update
     * @param caseInfo Test case info object with information to update
     * @return Updated case
     */
    public Case updateCase(int id, TestCaseInfo caseInfo) {
        // Find the case to update
        Case caseToUpdate = getCaseById(id);

        // Update all updateable fields
        caseToUpdate.setFunctionName(caseInfo.getFunctionName());
        caseToUpdate.setDescription(caseInfo.getDescription());

        String code = codeService.buildTestCaseCode(caseToUpdate.getLanguage(), caseToUpdate.getFunctionName(), caseInfo.getBody());
        caseToUpdate.setCode(code);
        // Save and return case
        return caseRepository.save(caseToUpdate);
    }

    /**
     * Update the upvote count on a case
     * @param id Id of case to vote on
     * @param action Vote action to modify upvote count
     */
    public void updateCaseUpvotes(int id, VoteAction action) {
        // Find case to update
        Case caseToUpdate = getCaseById(id);
        // Update vote count
        int change = action == VoteAction.UPVOTE ? 1 : -1;
        caseToUpdate.setUpvotes(caseToUpdate.getUpvotes() + change);
        caseRepository.save(caseToUpdate);
    }

    /**
     * Delete a case
     * @param id Id of case to delete
     */
    public void deleteCase(int id) {
        caseRepository.deleteById(id);
    }
}

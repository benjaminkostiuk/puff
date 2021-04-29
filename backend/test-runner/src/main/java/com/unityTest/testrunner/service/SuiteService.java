package com.unityTest.testrunner.service;

import com.unityTest.testrunner.entity.Suite;
import com.unityTest.testrunner.entity.SuiteFile;
import com.unityTest.testrunner.entity.SuiteFile_;
import com.unityTest.testrunner.entity.Suite_;
import com.unityTest.testrunner.exception.ElementNotFoundException;
import com.unityTest.testrunner.exception.NoFilesUploadedException;
import com.unityTest.testrunner.exception.NoSuiteFileException;
import com.unityTest.testrunner.models.PLanguage;
import com.unityTest.testrunner.models.VoteAction;
import com.unityTest.testrunner.repository.SuiteFileRepository;
import com.unityTest.testrunner.repository.SuiteRepository;
import com.unityTest.testrunner.utils.specification.AndSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SuiteService {

    @Autowired
    private SuiteRepository suiteRepository;

    @Autowired
    private SuiteFileRepository suiteFileRepository;

    /**
     * Create or update a suite
     * @param suite Suite to create or update
     * @return Suite created
     */
    public Suite createSuite(Suite suite) {
        // TODO Implement check if assignment with id exists
        return suiteRepository.save(suite);
    }

    /**
     * Set the testing file for a suite
     * @param suiteId Id a suite
     * @param file File to set for suite
     * @return SuiteFile saved in repository
     */
    public SuiteFile setSuiteTestFile(int suiteId, String authorId, MultipartFile file) throws IOException {
        if (file == null) throw new NoFilesUploadedException();     // Check file is present
        Suite suite = this.getSuiteById(suiteId);       // Get matching test suite

        // Perform checks based on lang
        switch (suite.getLanguage()) {
            case JAVA:
                // TODO IMPLEMENT JAVA CHECKS
                break;
            case PYTHON3:
                // TODO IMPLEMENT PYTHON3 CHECKS
                break;
            case HASKELL:
                // TODO IMPLEMENT HASKELL CHECKS
                break;
            default:
                throw new IllegalArgumentException("Invalid language associated to test suite");        // TODO FIX THIS
        }

        // Check for overwrite
        Specification<SuiteFile> spec = new AndSpecification<SuiteFile>().equal(suiteId, SuiteFile_.SUITE_ID).getSpec();
        Optional<SuiteFile> opt = suiteFileRepository.findOne(spec);

        // Get id to overwrite old suite file if it exists
        int existingId = opt.map(SuiteFile::getId).orElse(0);
        // Save file to repository
        return suiteFileRepository.save(new SuiteFile(existingId, suiteId, file.getOriginalFilename(), file.getSize(), authorId, file.getBytes()));
    }

    /**
     * Get a list of test suites that match the passed arguments
     * @param id Id of suite to fetch
     * @param assignmentId Assignment id to match
     * @param name Suite name to match
     * @param language Suite programming language to match
     * @return List of test suites with fields matching the passed arguments
     */
    public List<Suite> getSuites(Integer id, Integer assignmentId, String name, PLanguage language, String authorId) {
        return getSuites(Pageable.unpaged(), id, assignmentId, name, language, authorId).getContent();
    }

    /**
     * Get a Page view of test suites that match the passed arguments
     * @param pageable Pageable object specifying page size, sort and index
     * @param id Id of suite to fetch
     * @param assignmentId Assignment id to match
     * @param name Suite name to match
     * @param language Suite programming language to match
     * @return Page view of suites from repository matching passed arguments and formatted by the pageable param
     */
    public Page<Suite> getSuites(Pageable pageable, Integer id, Integer assignmentId, String name, PLanguage language, String authorId) {
        // Build specification
        Specification<Suite> spec = new AndSpecification<Suite>()
                .equal(id, Suite_.ID)
                .equal(assignmentId, Suite_.ASSIGNMENT_ID)
                .equal(name, Suite_.NAME)
                .equal(language, Suite_.LANGUAGE)
                .equal(authorId, Suite_.AUTHOR_ID).getSpec();
        return suiteRepository.findAll(spec, pageable);
    }

    /**
     * Get the testing file for a suite
     * @param suiteId Id of suite to fetch file for
     * @return SuiteFile for the suite
     */
    public SuiteFile getSuiteTestFile(int suiteId) {
        // Find SuiteFile for suite
        Specification<SuiteFile> spec = new AndSpecification<SuiteFile>().equal(suiteId, SuiteFile_.SUITE_ID).getSpec();
        Optional<SuiteFile> opt = suiteFileRepository.findOne(spec);
        if(!opt.isPresent()) throw new NoSuiteFileException(suiteId);
        return opt.get();
    }

    /**
     * Find a test suite by id
     * @param id Id of suite to find
     * @return Suite with matching id from JPA repository
     * @throws ElementNotFoundException if no suite with matching id can be found
     */
    public Suite getSuiteById(int id) throws ElementNotFoundException {
        Optional<Suite> opt = suiteRepository.findById(id);
        // Throw exception if element for id does not exist
        if(!opt.isPresent()) throw new ElementNotFoundException(Suite.class, "id", String.valueOf(id));
        return opt.get();
    }

    /**
     * Delete a test suite
     * @param id Id of test suite to delete
     */
    public void deleteSuite(int id) {
        suiteRepository.deleteById(id);
    }

    /**
     * Update the upvote count on a suite
     * @param id Id of suite to vote on
     * @param action Vote action to modify upvote count
     */
    public void updateSuiteUpvotes(int id, VoteAction action) {
        // Find suite to update
        Suite suite = getSuiteById(id);
        // Update vote count
        int change = action == VoteAction.UPVOTE ? 1 : -1;
        suite.setUpvotes(suite.getUpvotes() + change);
        suiteRepository.save(suite);
    }
}

package com.unityTest.testrunner.serviceImpl;

import com.unityTest.testrunner.entity.Suite;
import com.unityTest.testrunner.entity.Suite_;
import com.unityTest.testrunner.exception.ElementNotFoundException;
import com.unityTest.testrunner.models.PLanguage;
import com.unityTest.testrunner.repository.SuiteRepository;
import com.unityTest.testrunner.utils.specification.AndSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuiteService implements com.unityTest.testrunner.service.SuiteService {

    @Autowired
    private SuiteRepository suiteRepository;

    /**
     * Create or update a suite
     * @param suite Suite to create or update
     * @return Suite created
     */
    @Override
    public Suite createSuite(Suite suite) {
        // TODO Implement check if assignment with id exists
        return suiteRepository.save(suite);
    }

    /**
     * Get a list of test suites that match the passed arguments
     * @param id Id of suite to fetch
     * @param assignmentId Assignment id to match
     * @param name Suite name to match
     * @param language Suite programming language to match
     * @return List of test suites with fields matching the passed arguments
     */
    @Override
    public List<Suite> getSuites(Integer id, Integer assignmentId, String name, PLanguage language) {
        return getSuites(Pageable.unpaged(), id, assignmentId, name, language).getContent();
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
    @Override
    public Page<Suite> getSuites(Pageable pageable, Integer id, Integer assignmentId, String name, PLanguage language) {
        // Build specification
        Specification<Suite> spec = new AndSpecification<Suite>()
                .equal(id, Suite_.ID)
                .equal(assignmentId, Suite_.ASSIGNMENT_ID)
                .equal(name, Suite_.NAME)
                .equal(language, Suite_.LANGUAGE).getSpec();
        return suiteRepository.findAll(spec, pageable);
    }

    /**
     * Find a test suite by id
     * @param id Id of suite to find
     * @return Suite with matching id from JPA repository
     * @throws ElementNotFoundException if no suite with matching id can be found
     */
    @Override
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
    @Override
    public void deleteSuite(int id) {
        suiteRepository.deleteById(id);
    }
}

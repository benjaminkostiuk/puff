package com.unityTest.testrunner.utils.specification;

import org.springframework.data.jpa.domain.Specification;

public class AndSpecification<T> extends SpecificationBuilder<T> {
    /**
     * Combine specifications with AND operation
     * @param spec First specification
     * @param specToAdd Specification to add on to first specification
     * @return Specification combining both specs with AND operation
     */
    @Override
    protected Specification<T> combineSpecifications(Specification<T> spec, Specification<T> specToAdd) {
        return spec.and(specToAdd);
    }
}

package com.unityTest.testrunner.utils.specification;



import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Date;

public abstract class SpecificationBuilder<T> {

    // Specification being built
    private Specification<T> specification;

    public SpecificationBuilder() {
        this.specification = Specification.where(null);
    }

    /**
     * Extract the specification built from the builder
     * @return Specification built by the builder
     */
    public Specification<T> getSpec() {
        return this.specification;
    }

    /**
     * Specify the behavior of your Specification builder when combining multiple query criterion
     * @param spec First specification
     * @param specToAdd Specification to add on to first specification
     * @return Combined Specification according to chosen behavior
     */
    protected abstract Specification<T> combineSpecifications(Specification<T> spec, Specification<T> specToAdd);

    /**
     * Combine and set the specification according to the chained specification behavior
     * @param specToAdd Specification to combine with the specification being built
     */
    private void combineAndSetSpecification(Specification<T> specToAdd) {
        this.specification = this.combineSpecifications(this.specification, specToAdd);
    }

    // Add Specification with criteria where x = y
    public <V> SpecificationBuilder<T> equal(V value, String attr, String... attrs) {
        if(value != null) this.combineAndSetSpecification(this.buildSpec(Operator.EQUALS, value, attr, attrs));
        return this;
    }

    // Add Specification with criteria where d1 after d2
    public SpecificationBuilder<T> after(Date date, String attr, String... attrs) {
        if(date != null) this.combineAndSetSpecification(this.buildSpec(Operator.AFTER, date, attr, attrs));
        return this;
    }

    // Add Specification with criteria where d1 before d2
    public SpecificationBuilder<T> before(Date date, String attr, String... attrs) {
        if(date != null) this.combineAndSetSpecification(this.buildSpec(Operator.BEFORE, date, attr, attrs));
        return this;
    }

    /**
     * Build a specification for an operation
     * @param operator Operator for specification
     * @param value Value to compare to
     * @param attr Field of <T> to compare against
     * @param attrs Sub-fields of attr to compare against
     * @param <V> Type of value
     * @return Specification comparing attribute specified and value with the given operator
     */
    private <V> Specification<T> buildSpec(Operator operator, V value, String attr, String... attrs) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                // Build path
                Path path = root.get(attr);
                if(ArrayUtils.isNotEmpty(attrs)) {
                    for(String attribute : attrs) path = path.get(attribute);
                }
                // Return specification with correct operation
                switch (operator) {
                    case EQUALS:
                        return criteriaBuilder.equal(path, value);
                    case BEFORE:
                        return criteriaBuilder.lessThan(path, (Date) value);
                    case AFTER:
                        return criteriaBuilder.greaterThan(path, (Date) value);
                    default:
                        throw new IllegalArgumentException("Operation not supported. Operation must be a member of Operator enum.");
                }
            }
        };
    }

    /**
     * Operation types supported by the Specification builder
     */
    private enum Operator {
        EQUALS,      // x = y
        AFTER,       // d1 after d2
        BEFORE       // d1 before d2
    }
}



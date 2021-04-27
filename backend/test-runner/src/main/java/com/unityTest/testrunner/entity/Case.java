package com.unityTest.testrunner.entity;

import com.unityTest.testrunner.models.PLanguage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

/**
 * Models a test case in a test suite
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "TEST_CASE")
public class Case {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TEST_CASE_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1000"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private int id;

    // Test suite that case belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUITE_ID", referencedColumnName = "ID", nullable = false)
    private Suite suite;

    // Author id
    @Column(name = "AUTHOR_ID")
    private String authorId;

    // Name of function for test case code snippet
    @Column(name = "FUNCTION_NAME")
    private String functionName;

    // Optional description of test case
    @Column(name = "DESCRIPTION")
    private String description;

    // Number of upvotes for test case, needed for easy sorting & pagination when pulling test cases
    @Column(name = "UPVOTE_COUNT")
    private int upvoteCount;

    @Column(name = "RUN_COUNT")
    private int runCount;

    @Column(name = "PASS_COUNT")
    private int passCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "LANG")
    private PLanguage language;

    @Column(name = "CODE")
    private String code;
}

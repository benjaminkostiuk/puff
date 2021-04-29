package com.unityTest.testrunner.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unityTest.testrunner.models.PLanguage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Models a test suite comprising multiple test cases
 */
@Data
@ApiModel(value = "TestSuite", description = "Models a test suite comprising multiple test cases")
@Entity
@Table(name = "TEST_SUITE")
public class Suite {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TEST_SUITE_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1000"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private int id;

    // Id of assignment that suite tests
    @ApiModelProperty(value = "Assignment id", required = true, example = "1001")
    @NotNull
    @Column(name = "ASSIGNMENT_ID")
    private int assignmentId;

    // Name of test suite
    @ApiModelProperty(value = "Name", example = "Function A tests")
    @Column(name = "NAME")
    private String name;

    // Programming language of all tests in suite
    @ApiModelProperty(value = "Language", required = true, example = "JAVA")
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "LANG")
    private PLanguage language;

    // Upvote count of test suite
    @ApiModelProperty(value = "upvotes")
    @Column(name = "UPVOTE_COUNT")
    private int upvotes;

    @JsonIgnore
    @Column(name = "AUTHOR_ID")
    private String authorId;
}

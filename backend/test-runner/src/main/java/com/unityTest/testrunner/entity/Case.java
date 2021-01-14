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
 * Models a test case in a test suite
 */
@Data
@ApiModel(value = "TestCase", description = "Models a test case in a test suite")
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

    // Id of test suite that case belongs to
    @ApiModelProperty(value = "Test suite id", required = true, example = "1012")
    @Column(name = "SUITE_ID")
    private int suiteId;

    // Author id
    @JsonIgnore
    @NotNull
    @Column(name = "AUTHOR_ID")
    private String authorId;

    @ApiModelProperty(value = "Case description", example = "Tests boundary case if x = 0 for FUNC A")
    @Column(name = "DESCRIPTION")
    private String description;

    @ApiModelProperty(value = "Number of runs")
    @Column(name = "RUN_COUNT")
    private int runCount;

    @ApiModelProperty(value = "Number of passed runs")
    @Column(name = "PASS_COUNT")
    private int passCount;

    @ApiModelProperty(value = "Language", required = true, example = "JAVA")
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "LANG")
    private PLanguage language;

    @ApiModelProperty(value = "Case code", required = true)
    @NotNull
    @Column(name = "CODE")
    private String code;
}

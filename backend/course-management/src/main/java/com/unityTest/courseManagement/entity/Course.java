package com.unityTest.courseManagement.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Models a university course for a given semester
 */
@Data
@ApiModel(value = "Course", description = "Models a university course")
@Entity
@Table(name = "COURSE")
public class Course {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "COURSE_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1000"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private int id;

    // Course code
    @ApiModelProperty(value = "Course code", required = true, example = "COMPSCI 1JC3")
    @NotBlank
    @Column(name = "CODE")
    private String code;

    // Course level, between 1 and 4
    @ApiModelProperty(value = "Program level", required = true, example = "1")
    @Min(1)
    @Max(4)
    @Column(name = "LEVEL")
    private int level;

    // School term
    @ApiModelProperty(value = "School term", required = true, allowableValues = "FALL, WINTER, SPRING, SUMMER")
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "TERM")
    private Term term;

    // Academic year
    @ApiModelProperty(value = "Academic year", required = true, example = "2020")
    @NotNull
    @Column(name = "ACADEMIC_YEAR")
    private Integer academicYear;
}

/**
 * Academic semesters
 */
enum Term {
    WINTER,
    SPRING,
    SUMMER,
    FALL
}

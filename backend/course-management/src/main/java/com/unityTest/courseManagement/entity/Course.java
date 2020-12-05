package com.unityTest.courseManagement.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * Models a university course for a given semester
 */
@Data
@ApiModel(value = "Course", description = "Models a university course.")
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

    @ApiModelProperty(value = "Course code", required = true)
    @NotBlank
    @Column(name = "CODE")
    private String code;

    @ApiModelProperty(value = "Program level", required = true)
    @Min(1)
    @Max(4)
    @Column(name = "LEVEL")
    private int level;

    @ApiModelProperty(value = "School term")
    @NotBlank
    @Column(name = "TERM")
    private String term;

    @Column(name = "ACADEMIC_YEAR")
    private int academicYear;
}

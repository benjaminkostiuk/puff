package com.unityTest.courseManagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Models a key-value attribute for a Course
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value = "CourseAttribute", description = "Attribute for a course")
@Entity
@Table(name = "COURSE_ATTR")
public class CourseAttribute {

    @Id
    @Column(name = "Id")
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "COURSE_ATTR_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1000"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private int id;

    // Associated course
    @JsonIgnore
    @Column(name = "COURSE_ID")
    private Integer courseId;

    // Attribute name
    @ApiModelProperty(value = "Course attribute name", required = true, example = "professorName")
    @NotBlank
    @Column(name = "ATTR_NAME")
    private String name;

    // Attribute value
    @ApiModelProperty(value = "Course attribute value", required = true, example = "William M. Farmer")
    @NotBlank
    @Column(name = "ATTR_VALUE")
    private String value;
}

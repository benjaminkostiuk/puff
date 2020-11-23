package com.benko.unitytest.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@ApiModel(value = "Class")
@Entity
@Table(name = "CLASS")
public class Course {

    // Lombok has @Data, @ToString, @Getter, @Setter etc.
    // Validation has @NotNull, @Email, @NotBlank and @Size
    // add @Valid on the Rest Controller Body or @Validated for the entire class

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ApiModelProperty(value = "Course code", required = true)
    @Column(name = "CODE")
    private String code;

    @ApiModelProperty("Program level")
    @Column(name = "LEVEL")
    private int level;

    @Column(name = "TERM")
    private String term;

    @Column(name = "ACADEMIC_YEAR")
    private int academicYear;
}

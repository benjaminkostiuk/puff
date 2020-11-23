package com.benko.unitytest.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CLASS")
public class Course {

    // Lombok has @Data, @ToString, @Getter, @Setter etc.
    // Validation has @NotNull, @Email, @NotBlank and @Size
    // add @Valid on the Rest Controller Body or @Validated for the entire class

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "LEVEL")
    private int level;

    @Column(name = "TERM")
    private String term;

    @Column(name = "ACADEMIC_YEAR")
    private int academicYear;
}

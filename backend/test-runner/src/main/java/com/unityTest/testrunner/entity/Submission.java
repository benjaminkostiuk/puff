package com.unityTest.testrunner.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "SUBMISSION")
public class Submission {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SUBMISSION_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1000"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private int id;

    // List of source files uploaded by the submission
    @OneToMany(targetEntity = SourceFile.class, mappedBy = "submission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SourceFile> sourceFiles = new ArrayList<>();

    // Id of related assignment
    @Column(name = "ASSIGNMENT_ID")
    private int assignmentId;

    // Date of submission upload
    @Column(name = "SUBMISSION_DATE")
    @CreationTimestamp
    private Date submissionDate;

    @Column(name = "AUTHOR_ID")
    private String authorId;
}

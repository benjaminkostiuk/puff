package com.unityTest.testrunner.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 *  Models a source code file uploaded by a user
 */
@Data
@AllArgsConstructor
@Entity
@Table(name = "SOURCE_FILE")
public class SourceFile {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SOURCE_FILE_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1000"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private int id;

    // SourceFiles uploaded together are grouped by submissionId
    @Column(name = "SUBMISSION_ID")
    private int submissionId;

    // Id of assignment that the source file is for
    @Column(name = "ASSIGNMENT_ID")
    private int assignmentId;

    @Column(name = "FILE_NAME")
    private String fileName;

    // Date of submission upload
    @Column(name = "SUBMISSION_DATE")
    private Date submissionDate;

    @Column(name = "AUTHOR_ID")
    private String authorId;

    @Column(name = "CONTENT")
    private byte[] content;
}
package com.unityTest.testrunner.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 *  Models a source code file uploaded by a user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBMISSION_ID", referencedColumnName = "ID", updatable = false, nullable = false)
    private Submission submission;

    // Name of source file
    @Column(name = "FILE_NAME")
    private String fileName;

    // Size of source file
    @Column(name = "FILE_SIZE")
    private long fileSize;

    @Column(name = "AUTHOR_ID")
    private String authorId;

    @Column(name = "CONTENT")
    private byte[] content;

    public SourceFile(Submission submission, String fileName, long fileSize, String authorId, byte[] content) {
        this.submission = submission;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.authorId = authorId;
        this.content = content;
    }
}
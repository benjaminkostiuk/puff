package com.unityTest.testrunner.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Models a source code file uploaded by a user as part of a submission.
 * Submissions are uploads of user source code to test against
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SUBMISSION_FILE")
public class SubmissionFile extends SourceFile {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SUBMISSION_FILE_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1000"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private int id;

    // Submission files uploaded together are grouped by submissionId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBMISSION_ID", referencedColumnName = "ID", updatable = false, nullable = false)
    private Submission submission;

    public SubmissionFile(Submission submission, String fileName, long fileSize, String authorId, byte[] content) {
        super(fileName, fileSize, authorId, content);
        this.submission = submission;
    }
}

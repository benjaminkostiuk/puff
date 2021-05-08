package com.unityTest.testrunner.entity;

import com.unityTest.testrunner.models.response.SubmissionEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Models a submission of code files to be tested
 */
@Data
@NoArgsConstructor
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
    @OneToMany(targetEntity = SubmissionFile.class, mappedBy = "submission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<SubmissionFile> sourceFiles = new ArrayList<>();

    // Id of related assignment
    @Column(name = "ASSIGNMENT_ID")
    private int assignmentId;

    // Date of submission upload
    @Column(name = "SUBMISSION_DATE")
    @CreationTimestamp
    private Date submissionDate;

    @Column(name = "AUTHOR_ID")
    private String authorId;

    public Submission(int assignmentId, String authorId) {
        this.assignmentId = assignmentId;
        this.authorId = authorId;
    }

    /**
     * Generate a submission event based on the submission
     * @return SubmissionEvent with information based on the submission
     */
    public SubmissionEvent generateSubmissionEvent() {
        SubmissionEvent event = new SubmissionEvent(this.id, this.assignmentId, this.submissionDate);
        for(SourceFile file : this.sourceFiles) {
            event.addFile(file.getFileName(), file.getFileSize());
        }
        return event;
    }

    /**
     * Add a source file to the submission
     * @param file SourceFile to add
     */
    public void addSubmissionFile(SubmissionFile file) {
        this.sourceFiles.add(file);
    }
}

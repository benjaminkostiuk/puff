package com.unityTest.testrunner.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *  Models a source code file uploaded by a user
 */
@Data
@ApiModel(value = "SourceFile", description = "Models a submitted source file")
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
    @ApiModelProperty(value = "Submission id", required = true, example = "101")
    @Column(name = "SUBMISSION_ID")
    private int submissionId;

    // Date of upload
    @ApiModelProperty(value = "Submission datetime")
    @CreationTimestamp
    @Column(name = "SUBMISSION_DATE")
    private Date submissionDate;

    @JsonIgnore
    @NotNull
    @Column(name = "AUTHOR_ID")
    private String authorId;

    @NotNull
    @Column(name = "CONTENT")
    private byte[] content;
}
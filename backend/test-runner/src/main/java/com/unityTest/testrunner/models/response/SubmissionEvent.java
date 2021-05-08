package com.unityTest.testrunner.models.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Models the response to a file submission, or upload of one or more files
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "Submission", description = "Models a submission of source files")
public class SubmissionEvent extends FileUploadEvent {

    @JsonIgnore
    private int submissionId;

    @ApiModelProperty(value = "Assignment id of submission", required = true)
    private int assignmentId;

    @ApiModelProperty(value = "Link to download the files contained in the submission", required = true, example = "/download?submissionId=12")
    private String downloadUrl;

    private SubmissionEvent(Date submissionTime) {
        super(submissionTime);
    }

    public SubmissionEvent(int submissionId, int assignmentId, Date submissionTime) {
        this(submissionTime);
        this.submissionId = submissionId;
        this.assignmentId = assignmentId;
        this.downloadUrl = String.format("%s?submissionId=%d", "/download", submissionId);
    }
}

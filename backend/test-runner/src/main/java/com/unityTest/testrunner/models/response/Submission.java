package com.unityTest.testrunner.models.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "Submission", description = "Models a submission of source files")
public class Submission {

    @JsonIgnore
    private int submissionId;

    @ApiModelProperty(value = "Assignment id of submission", required = true)
    private int assignmentId;

    @ApiModelProperty(value = "Submission id", required = true)
    private Date submissionTime;

    @ApiModelProperty(value = "List of files uploaded with the submission", required = true)
    private List<FileInfo> filesUploaded;

    @ApiModelProperty(value = "Link to download the files contained in the submission", required = true, example = "/download?submissionId=12")
    private String downloadUrl;

    private Submission(Date submissionTime) {
        this.submissionTime = submissionTime;
    }

    public Submission(int submissionId, int assignmentId, Date submissionTime) {
        this(submissionTime);
        this.submissionId = submissionId;
        this.assignmentId = assignmentId;
        this.filesUploaded = new ArrayList<>();
        this.downloadUrl = String.format("%s?submissionId=%d", "/download", submissionId);
    }

    public void addSourceFile(String name, long size) {
        this.filesUploaded.add(new FileInfo(name, size));
    }
}

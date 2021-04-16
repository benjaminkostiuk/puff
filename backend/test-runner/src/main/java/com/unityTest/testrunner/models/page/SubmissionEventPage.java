package com.unityTest.testrunner.models.page;

import com.unityTest.testrunner.entity.Submission;
import com.unityTest.testrunner.models.response.SubmissionEvent;
import io.swagger.annotations.ApiModel;
import org.springframework.data.domain.Page;

@ApiModel(value = "SubmissionPage", description = "Pageable listing of Submissions")
public class SubmissionEventPage extends BasePage<SubmissionEvent> {
    public SubmissionEventPage(Page<Submission> page) {
        super(page.map(Submission::generateSubmissionEvent));
    }
}

package com.unityTest.testrunner.models.page;

import com.unityTest.testrunner.entity.Suite;
import io.swagger.annotations.ApiModel;
import org.springframework.data.domain.Page;

@ApiModel(value = "SuitePage", description = "Page request for test suites")
public class SuitePage extends BasePage<Suite> {
    public SuitePage(Page<Suite> page) {
        super(page);
    }
}

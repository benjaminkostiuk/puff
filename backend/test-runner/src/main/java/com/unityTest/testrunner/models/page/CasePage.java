package com.unityTest.testrunner.models;

import com.unityTest.testrunner.entity.Case;
import io.swagger.annotations.ApiModel;
import org.springframework.data.domain.Page;

@ApiModel(value = "CasePage", description = "Page request for test cases")
public class CasePage extends BasePage<Case> {
    public CasePage(Page<Case> page) {
        super(page);
    }
}

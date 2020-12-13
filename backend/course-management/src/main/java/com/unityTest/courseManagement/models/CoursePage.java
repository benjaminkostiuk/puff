package com.unityTest.courseManagement.models;

import com.unityTest.courseManagement.entity.Course;
import io.swagger.annotations.ApiModel;
import org.springframework.data.domain.Page;

@ApiModel(value = "CoursePage", description = "Page request for courses")
public class CoursePage extends BasePage<Course> {
    public CoursePage(Page<Course> page) {
        super(page);
    }
}

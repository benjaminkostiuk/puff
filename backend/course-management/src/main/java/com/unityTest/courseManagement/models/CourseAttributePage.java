package com.unityTest.courseManagement.models;

import com.unityTest.courseManagement.entity.CourseAttribute;
import io.swagger.annotations.ApiModel;
import org.springframework.data.domain.Page;

@ApiModel(value = "CourseAttributePage", description = "Page request for course attributes")
public class CourseAttributePage extends BasePage<CourseAttribute> {
    public CourseAttributePage(Page<CourseAttribute> page) {
        super(page);
    }
}

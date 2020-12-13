package com.unityTest.courseManagement.repository;

import com.unityTest.courseManagement.entity.CourseAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseAttrRepository extends
        JpaRepository<CourseAttribute, Integer>, JpaSpecificationExecutor<CourseAttribute> {
}

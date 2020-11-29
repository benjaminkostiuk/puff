package com.unityTest.courseManagement.serviceImpl;

import com.unityTest.courseManagement.entity.Course;
import com.unityTest.courseManagement.repository.CourseRepository;
import com.unityTest.courseManagement.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Course createClass(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course updateClass(Course course) {
        return null;
    }

    @Override
    public List<Course> getAllClasses() {
        return courseRepository.findAll();
    }

    @Override
    public Course getClassById(int id) {
        return null;
    }

    @Override
    public void deleteProduct(int id) {

    }
}

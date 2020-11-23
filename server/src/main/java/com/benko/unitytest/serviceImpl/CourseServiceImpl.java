package com.benko.unitytest.serviceImpl;

import com.benko.unitytest.entity.Course;
import com.benko.unitytest.repository.CourseRepository;
import com.benko.unitytest.service.CourseService;
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

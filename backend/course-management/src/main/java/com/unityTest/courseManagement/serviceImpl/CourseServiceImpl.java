package com.unityTest.courseManagement.serviceImpl;

import com.unityTest.courseManagement.entity.Course;
import com.unityTest.courseManagement.entity.CourseAttribute;
import com.unityTest.courseManagement.entity.CourseAttribute_;
import com.unityTest.courseManagement.entity.Course_;
import com.unityTest.courseManagement.models.Term;
import com.unityTest.courseManagement.exception.ElementNotFoundException;
import com.unityTest.courseManagement.repository.CourseAttrRepository;
import com.unityTest.courseManagement.repository.CourseRepository;
import com.unityTest.courseManagement.service.CourseService;
import com.unityTest.courseManagement.utils.specification.AndSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseAttrRepository courseAttrRepository;

    /**
     * Create or update a course
     * @param course Course to create or update
     * @return Course created
     */
    @Override
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    /**
     * Get a list of courses that match the passed arguments
     * @param id Id of course to fetch
     * @param code Course code to match
     * @param level Course level to match
     * @param term Course term to match
     * @param academicYear Course academic year to match
     * @return List of courses with fields matching the passed arguments
     */
    @Override
    public List<Course> getCourses(Integer id, String code, Integer level, Term term, Integer academicYear) {
        Specification<Course> spec = new AndSpecification<Course>()
                .equal(id, Course_.ID)
                .equal(code, Course_.CODE)
                .equal(level, Course_.LEVEL)
                .equal(term, Course_.TERM)
                .equal(academicYear, Course_.ACADEMIC_YEAR).getSpec();
        return courseRepository.findAll(spec);
    }

    /**
     * Finds and returns a course by its id
     * @param id Id of course to find
     * @return Course with matching id from JPA repository
     * @throws ElementNotFoundException if no course with matching id can be found
     */
    @Override
    public Course getCourseById(int id) throws ElementNotFoundException {
        Optional<Course> opt = courseRepository.findById(id);
        // Throw exception if element for id does not exist
        if(!opt.isPresent()) throw new ElementNotFoundException(Course.class, "id", id);
        return opt.get();
    }

    /**
     * Delete a course
     * @param id Id of course to delete
     */
    @Override
    public void deleteCourse(int id) {
        courseRepository.deleteById(id);
    }

    /**
     * Create or update an attribute for a course
     * @param attribute CourseAttribute to create
     * @return CourseAttribute created
     */
    @Override
    public CourseAttribute createCourseAttr(CourseAttribute attribute) {
        return courseAttrRepository.save(attribute);
    }

    /**
     * Get a list of course attributes that match the passed arguments
     * @param id Id of courseAttribute to find
     * @param courseId CourseAttribute courseId to match
     * @param name CourseAttribute name to match
     * @return List of CourseAttributes with fields matching the passed arguments
     */
    @Override
    public List<CourseAttribute> getCourseAttributes(Integer id, Integer courseId, String name) {
        Specification<CourseAttribute> spec = new AndSpecification<CourseAttribute>()
                .equal(id, CourseAttribute_.ID)
                .equal(courseId, CourseAttribute_.COURSE_ID)
                .equal(name, CourseAttribute_.NAME).getSpec();
        return courseAttrRepository.findAll(spec);
    }

    /**
     * Delete a course attribute
     * @param id Id of course attribute to delete
     */
    @Override
    public void deleteCourseAttr(int id) {
        courseAttrRepository.deleteById(id);
    }
}

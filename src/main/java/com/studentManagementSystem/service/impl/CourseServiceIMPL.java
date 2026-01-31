package com.studentManagementSystem.service.impl;

import com.studentManagementSystem.dto.CourseDTO;
import com.studentManagementSystem.dto.paginated.PaginatedResponseCourseDTO;
import com.studentManagementSystem.entity.Course;
import com.studentManagementSystem.exception.NotFoundException;
import com.studentManagementSystem.repo.CourseRepo;
import com.studentManagementSystem.service.CourseService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseServiceIMPL implements CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseServiceIMPL.class);

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        log.info("creating course with code: {}", courseDTO.getCourseCode());
        Course course = modelMapper.map(courseDTO, Course.class);
        courseRepo.save(course);
        return modelMapper.map(course, CourseDTO.class);
    }

    @Override
    public boolean existsByCourseCode(String courseCode) {
        log.info("checking if code exists: {}", courseCode);
//
        return courseRepo.existsByCourseCodeIgnoreCase(courseCode);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseCourseDTO getCoursesByActiveWithPagination(boolean active, int page, int size) {
        Page<Course> courses = courseRepo.findAllByActive(active, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        
        PaginatedResponseCourseDTO paginatedResponseCourseDTO = new PaginatedResponseCourseDTO(
                courses.map(course -> modelMapper.map(course, CourseDTO.class)).toList(),
                courseRepo.countAllByActive(active)
        );

        return paginatedResponseCourseDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepo.findById(id).orElseThrow(() -> new NotFoundException("Course not found with id: " + id));
        return modelMapper.map(course, CourseDTO.class);
    }

    @Override
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        log.info("updating course with id: {}", id);
        Course course = courseRepo.findById(id).orElseThrow(() -> new NotFoundException("Course not found with id: " + id));
        
        course.setCourseName(courseDTO.getCourseName());
        course.setCourseCode(courseDTO.getCourseCode());
        course.setDuration(courseDTO.getDuration());
        course.setFee(courseDTO.getFee());
        course.setActive(courseDTO.isActive());
        course.setDescription(courseDTO.getDescription());
        
        courseRepo.save(course);
        return modelMapper.map(course, CourseDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getAllActiveCourses() {
        log.info("getting all active courses");
        return courseRepo.findAllByActive(true).stream()
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveCourses() {
        log.info("counting active courses");
        return courseRepo.countAllByActive(true);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllCourses() {
        log.info("counting all courses");
        return courseRepo.count();
    }

    @Override
    public void toggleActiveStatus(Long id) {
        log.info("toggling active status for course with id: {}", id);
        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found with id: " + id));
        course.setActive(!course.isActive());
        courseRepo.save(course);
        log.info("Course id: {} active status changed to: {}", id, course.isActive());
    }
}

package com.studentManagementSystem.service.impl;

import com.studentManagementSystem.dto.CourseDTO;
import com.studentManagementSystem.dto.paginated.PaginatedResponseCourseDTO;
import com.studentManagementSystem.entity.Course;
import com.studentManagementSystem.exception.NotFoundException;
import com.studentManagementSystem.repo.CourseRepo;
import com.studentManagementSystem.service.CourseService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CourseServiceIMPL implements CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseServiceIMPL.class);

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private ModelMapper modelMapper;
//    private final CourseRepo courseRepo;
//
//    CourseServiceIMPL(CourseRepo courseRepo) {
//        this.courseRepo = courseRepo;
//    }

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
    public PaginatedResponseCourseDTO getCoursesByActiveWithPagination(boolean active, int page, int size) {
        Page<Course> courses = courseRepo.findAllByActive(active, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
        if (courses.isEmpty()) {
            throw new NotFoundException("No Data");
        }
        PaginatedResponseCourseDTO paginatedResponseCourseDTO = new PaginatedResponseCourseDTO(
                courses.map(course -> modelMapper.map(course, CourseDTO.class)).toList(),
                courseRepo.countAllByActive(active)
        );

        return paginatedResponseCourseDTO;
    }


}

package com.micolegio.domain.service.course;

import com.micolegio.adapters.db.ICourseRepository;
import com.micolegio.domain.Course;
import com.micolegio.domain.service.dto.response.CourseListResponse;
import com.micolegio.domain.service.mappers.CourseMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseService implements ICourseService{

    private final ICourseRepository courseRepository;

    @Override
    public List<CourseListResponse> getCoursesBySchool(Long schoolId){

        List<Course> courses = courseRepository.findBySchoolId(schoolId);

        return courses.stream()
                .map(CourseMapper::toCourseListResponse)
                .toList();
    }
}

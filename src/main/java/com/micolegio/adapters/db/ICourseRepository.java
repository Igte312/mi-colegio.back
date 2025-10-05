package com.micolegio.adapters.db;

import com.micolegio.domain.Course;

import java.util.List;

public interface ICourseRepository {

    List<Course> findBySchoolId(long schoolId);
}

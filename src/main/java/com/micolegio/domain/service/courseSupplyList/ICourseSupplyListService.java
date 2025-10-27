package com.micolegio.domain.service.courseSupplyList;

import com.micolegio.domain.service.dto.request.CourseSupplyListRequest;

public interface ICourseSupplyListService {

    void updateCourseSupplies(CourseSupplyListRequest request, Long schoolId);
}
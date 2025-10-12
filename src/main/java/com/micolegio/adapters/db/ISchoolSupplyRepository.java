package com.micolegio.adapters.db;

import com.micolegio.domain.SchoolSupply;
import com.micolegio.domain.service.dto.response.CourseSupplyResponse;

import java.util.List;

public interface ISchoolSupplyRepository {

    List<SchoolSupply> getGeneralActiveSupplies();

    List<CourseSupplyResponse> getSuppliesByCourseId(Long schoolId, Long courseId);
}
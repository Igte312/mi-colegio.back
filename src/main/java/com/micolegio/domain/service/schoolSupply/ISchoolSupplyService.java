package com.micolegio.domain.service.schoolSupply;

import com.micolegio.domain.SchoolSupply;
import com.micolegio.domain.service.dto.response.CourseSupplyResponse;

import java.util.List;

public interface ISchoolSupplyService {

    List<SchoolSupply> getGeneralActiveSupplies();

    List<CourseSupplyResponse> getSuppliesByCourseId(Long schoolId, Long courseId);
}

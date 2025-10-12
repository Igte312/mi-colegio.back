package com.micolegio.domain.service.schoolSupply;

import com.micolegio.adapters.db.ISchoolSupplyRepository;
import com.micolegio.commons.exception.BadRequestException;
import com.micolegio.domain.SchoolSupply;
import com.micolegio.domain.service.dto.response.CourseSupplyResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SchoolSupplyService implements ISchoolSupplyService{

    private final ISchoolSupplyRepository schoolSupplyRepository;

    @Override
    public List<SchoolSupply> getGeneralActiveSupplies() {
        return schoolSupplyRepository.getGeneralActiveSupplies();
    }

    @Override
    public List<CourseSupplyResponse> getSuppliesByCourseId(Long schoolId, Long courseId) {

        if (schoolId == null) {
            throw new BadRequestException("El usuario no tiene un schoolId asociado.");
        }

        return schoolSupplyRepository.getSuppliesByCourseId(schoolId, courseId);
    }
}

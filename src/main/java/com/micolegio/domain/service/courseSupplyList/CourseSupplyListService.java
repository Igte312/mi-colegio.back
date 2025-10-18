package com.micolegio.domain.service.courseSupplyList;

import com.micolegio.adapters.db.ICourseSupplyListRepository;
import com.micolegio.commons.exception.BadRequestException;
import com.micolegio.commons.exception.NotFoundException;
import com.micolegio.domain.CourseSupplyList;
import com.micolegio.domain.service.dto.request.CourseSupplyListRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseSupplyListService implements ICourseSupplyListService {

    private final ICourseSupplyListRepository courseSupplyListRepository;

    @Override
    @Transactional
    public void updateCourseSupplies(CourseSupplyListRequest request, Long schoolId) {
        if (schoolId == null) {
            throw new BadRequestException("El usuario no tiene un schoolId asociado.");
        }

        // Validate that all schoolSupplyIds exist
        List<Long> invalidSupplies = new ArrayList<>();
        for (var supply : request.getSupplies()) {
            if (!courseSupplyListRepository.existsBySchoolSupplyId(supply.getSchoolSupplyId())) {
                invalidSupplies.add(supply.getSchoolSupplyId());
            }
        }

        if (!invalidSupplies.isEmpty()) {
            throw new NotFoundException("Los siguientes supplies no existen: " + invalidSupplies);
        }

        // Get existing supplies for the course
        List<CourseSupplyList> existingSupplies = courseSupplyListRepository.findByCourseId(request.getCourseId());

        // Extract existing schoolSupplyIds
        List<Long> existingSchoolSupplyIds = existingSupplies.stream()
                .map(CourseSupplyList::getSchoolSupplyId)
                .collect(Collectors.toList());

        // Extract incoming schoolSupplyIds
        List<Long> incomingSchoolSupplyIds = request.getSupplies().stream()
                .map(supply -> supply.getSchoolSupplyId())
                .collect(Collectors.toList());

        // Find supplies to reactivate (existing but inactive and in incoming list)
        List<Long> suppliesToReactivate = existingSupplies.stream()
                .filter(existing -> !existing.getIsActive() && incomingSchoolSupplyIds.contains(existing.getSchoolSupplyId()))
                .map(CourseSupplyList::getSchoolSupplyId)
                .collect(Collectors.toList());

        // Reactivate supplies that were inactive but are now requested
        if (!suppliesToReactivate.isEmpty()) {
            courseSupplyListRepository.updateIsActiveByCourseIdAndSchoolSupplyIds(
                request.getCourseId(), suppliesToReactivate, true);
        }

        // Find supplies to deactivate (existing but not in incoming)
        List<Long> suppliesToDeactivate = existingSchoolSupplyIds.stream()
                .filter(id -> !incomingSchoolSupplyIds.contains(id))
                .collect(Collectors.toList());

        // Deactivate supplies not in the incoming list
        if (!suppliesToDeactivate.isEmpty()) {
            courseSupplyListRepository.updateIsActiveByCourseIdAndSchoolSupplyIds(
                request.getCourseId(), suppliesToDeactivate, false);
        }

        // Create new CourseSupplyList entities only for supplies that don't already exist for this course
        List<CourseSupplyList> newSupplies = request.getSupplies().stream()
                .filter(supply -> !existingSchoolSupplyIds.contains(supply.getSchoolSupplyId()))
                .map(supply -> {
                    CourseSupplyList courseSupplyList = new CourseSupplyList();
                    courseSupplyList.setSchoolSupplyId(supply.getSchoolSupplyId());
                    courseSupplyList.setCourseId(request.getCourseId());
                    courseSupplyList.setIsActive(true);
                    courseSupplyList.setQuantity(supply.getQuantity());
                    return courseSupplyList;
                })
                .collect(Collectors.toList());

        // Save new supplies (only those that didn't exist before)
        if (!newSupplies.isEmpty()) {
            courseSupplyListRepository.saveAll(newSupplies);
        }
    }
}
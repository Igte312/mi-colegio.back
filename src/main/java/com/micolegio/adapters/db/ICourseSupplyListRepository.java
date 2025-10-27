package com.micolegio.adapters.db;

import com.micolegio.domain.CourseSupplyList;

import java.util.List;

public interface ICourseSupplyListRepository {

    void saveAll(List<CourseSupplyList> courseSupplyLists);

    List<CourseSupplyList> findByCourseId(Long courseId);

    void updateIsActiveByCourseIdAndSchoolSupplyIds(Long courseId, List<Long> schoolSupplyIds, boolean isActive);

    boolean existsBySchoolSupplyId(Long schoolSupplyId);
}
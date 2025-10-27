package com.micolegio.adapters.db;

import com.micolegio.domain.service.dto.response.GuardianNotificationResponse;

import java.util.List;

public interface IGuardianNotificationRepository {

    List<GuardianNotificationResponse> findGuardiansByCourseId(Long courseId);
}
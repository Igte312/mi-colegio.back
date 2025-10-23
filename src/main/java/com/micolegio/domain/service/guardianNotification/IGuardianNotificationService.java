package com.micolegio.domain.service.guardianNotification;

import com.micolegio.domain.service.dto.response.ComparacionResponse;
import com.micolegio.domain.service.dto.response.CourseSupplyResponse;
import com.micolegio.domain.service.dto.response.GuardianNotificationResponse;
import jakarta.mail.MessagingException;

import java.util.List;

public interface IGuardianNotificationService {

    // --- Métodos para preview ---
    List<CourseSupplyResponse> getSuppliesForPreview(Long schoolId, Long courseId);

    ComparacionResponse getComparacionForPreview(List<CourseSupplyResponse> supplies);

    //String buildHtmlEmailWithLogoCorner(List<CourseSupplyResponse> supplies, ComparacionResponse comparacion);

    String buildHtmlEmailWithLogoCorner(List<CourseSupplyResponse> supplies,
                                        ComparacionResponse comparacion,
                                        GuardianNotificationResponse guardian);

    void sendSupplyListNotification(Long courseId, Long school_id)throws MessagingException;
}
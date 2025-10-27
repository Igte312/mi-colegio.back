package com.micolegio.controllers;

import com.micolegio.adapters.db.dto.UserBasicInfo;
import com.micolegio.commons.security.CurrentUser;
import com.micolegio.domain.service.dto.response.ComparacionResponse;
import com.micolegio.domain.service.dto.response.CourseSupplyResponse;
import com.micolegio.domain.service.guardianNotification.IGuardianNotificationService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guardian-notification")
@AllArgsConstructor
public class GuardianNotificationController {

    private final IGuardianNotificationService guardianNotificationService;

    @PostMapping("/send-supply-list/{courseId}")
    public ResponseEntity<Void> sendSupplyListNotification(
            @PathVariable Long courseId,
            @CurrentUser UserBasicInfo user) throws MessagingException {
        guardianNotificationService.sendSupplyListNotification(courseId, user.getSchoolId());
        return ResponseEntity.ok().build();
    }
}
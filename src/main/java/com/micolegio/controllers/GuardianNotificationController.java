package com.micolegio.controllers;

import com.micolegio.adapters.db.dto.UserBasicInfo;
import com.micolegio.commons.security.CurrentUser;
import com.micolegio.domain.service.guardianNotification.IGuardianNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guardian-notification")
@AllArgsConstructor
public class GuardianNotificationController {

    private final IGuardianNotificationService guardianNotificationService;

    @PostMapping("/send-supply-list/{courseId}")
    public ResponseEntity<Void> sendSupplyListNotification(
            @PathVariable Long courseId,
            @CurrentUser UserBasicInfo user) {
        guardianNotificationService.sendSupplyListNotification(courseId);
        return ResponseEntity.ok().build();
    }
}
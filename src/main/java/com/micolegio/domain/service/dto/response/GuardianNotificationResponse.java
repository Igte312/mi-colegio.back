package com.micolegio.domain.service.dto.response;

import lombok.Data;

@Data
public class GuardianNotificationResponse {
    private String studentName;
    private String guardianName;
    private String guardianEmail;
}
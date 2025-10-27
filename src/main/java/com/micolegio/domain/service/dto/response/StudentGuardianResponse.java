package com.micolegio.domain.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentGuardianResponse {
    private Integer student_id;
    private String studentName;
    private String studentEmail;
    private Integer guardian_id;
    private String guardianName;
    private String guardianEmail;
}
package com.micolegio.domain.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentGuardianCsvData {
    @NotBlank(message = "Student first name is required")
    private String studentFirstName;

    @NotBlank(message = "Student last name is required")
    private String studentLastName;

    @Email(message = "Student email must be valid")
    private String studentEmail; // optional

    @NotBlank(message = "Guardian first name is required")
    private String guardianFirstName;

    @NotBlank(message = "Guardian last name is required")
    private String guardianLastName;

    @NotBlank(message = "Guardian email is required")
    @Email(message = "Guardian email must be valid")
    private String guardianEmail; // required
}
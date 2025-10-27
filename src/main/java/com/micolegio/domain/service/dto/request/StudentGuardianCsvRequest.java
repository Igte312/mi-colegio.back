package com.micolegio.domain.service.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentGuardianCsvRequest {
    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotEmpty(message = "Data list cannot be empty")
    @Valid
    private List<StudentGuardianCsvData> data;
}
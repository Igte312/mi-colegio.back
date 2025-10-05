package com.micolegio.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Course {
    private Long id;
    private String name;
    private String level;
    private String letter;
    private Long schoolId;
    private Long homeroomTeacherId;
    private LocalDateTime createdAt;
}

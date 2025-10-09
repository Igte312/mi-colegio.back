package com.micolegio.adapters.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBasicInfo {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean isActive;
    private String roleName;
    private Long schoolId;
    private String schoolName;
}

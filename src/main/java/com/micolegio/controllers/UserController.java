package com.micolegio.controllers;

import com.micolegio.adapters.db.dto.UserBasicInfo;
import com.micolegio.domain.service.user.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
@AllArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/basic-info")
    public UserBasicInfo getUserBasicInfo(@RequestParam String email) {
        return userService.getUserBasicInfoByEmail(email);
    }
}

//package com.micolegio.controllers;
//
//import com.micolegio.dtos.request.response.CreateUserRequest;
//import com.micolegio.domain.User;
//import com.micolegio.services.user.IUserService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RestController
//@RequestMapping("/users")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final IUserService userService;
//
//    @PostMapping("/create")
//    public ResponseEntity<User> createUser(@Valid @org.springframework.web.bind.annotation.RequestBody CreateUserRequest requestDto) {
//        User user = User.builder()
//                .firstName(requestDto.getFirstName())
//                .lastName(requestDto.getLastName())
//                .email(requestDto.getEmail())
//                .dateOfBirth(requestDto.getDateOfBirth())
//                .isActive(requestDto.getIsActive() != null ? requestDto.getIsActive() : true)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        User createdUser = userService.createUser(user);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
//    }
//
//    @GetMapping
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }
//}

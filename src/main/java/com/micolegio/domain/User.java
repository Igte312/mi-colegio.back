//package com.micolegio.domain;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.Builder;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "Users")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @Column(name = "first_name", nullable = false, length = 100)
//    private  String firstName;
//
//    @Column(name = "last_name", nullable = false, length = 100)
//    private String lastName;
//
//    @Column(name = "email", unique = true, length = 100)
//    private String email;
//
//    @Column(name = "date_of_birth", nullable = true)
//    private LocalDate dateOfBirth;
//
//    @Column(name = "is_active")
//    private Boolean isActive;
//
//    @Column(name = "created_at", nullable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();
//
////    @OneToMany()
//}

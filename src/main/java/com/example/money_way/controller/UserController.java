package com.example.money_way.controller;

import com.example.money_way.dto.PasswordResetDTO;
import com.example.money_way.exception.ApiResponse;
import com.example.money_way.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")

public class UserController{
    private final UserService userService;

    @PutMapping("/reset-password")

ResponseEntity<ApiResponse<String>> resetPassword (@Valid @RequestBody PasswordResetDTO passwordResetDto) {
        ApiResponse apiResponse = userService.updatePassword(passwordResetDto);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }



        }
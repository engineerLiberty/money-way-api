package com.example.money_way.controller;

import com.example.money_way.dto.request.VerifyTokenDto;
import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class UserController {
    private  final UserService userService;

    @PutMapping("/verify-link")
    ResponseEntity<ApiResponse>verifyLink(@RequestBody VerifyTokenDto verifyTokenDto){
        ApiResponse response = userService.verifyLink(verifyTokenDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

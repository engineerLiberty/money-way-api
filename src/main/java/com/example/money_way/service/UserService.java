package com.example.money_way.service;

import com.example.money_way.dto.request.LoginRequestDto;
import com.example.money_way.dto.request.SignUpDto;
import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.exception.ValidationException;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<String> login(LoginRequestDto request);

    ResponseEntity<ApiResponse> signUp(SignUpDto signUpDto) throws ValidationException;

}

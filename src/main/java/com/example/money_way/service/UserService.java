package com.example.money_way.service;

import com.example.money_way.dto.request.PasswordResetDTO;
import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.dto.request.LoginRequestDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ApiResponse<String> updatePassword(PasswordResetDTO passwordResetDTO);

    ResponseEntity<String> login(LoginRequestDto request);
}

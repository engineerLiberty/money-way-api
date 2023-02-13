package com.example.money_way.service;

import com.example.money_way.dto.PasswordResetDTO;
import com.example.money_way.exception.ApiResponse;

public interface UserService {
    ApiResponse<String> updatePassword(PasswordResetDTO passwordResetDTO);
}

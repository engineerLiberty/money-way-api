package com.example.money_way.service;

import com.example.money_way.dto.request.PasswordResetDTO;
import com.example.money_way.dto.response.ApiResponse;

public interface UserService {
    ApiResponse<String> updatePassword(PasswordResetDTO passwordResetDTO);
}

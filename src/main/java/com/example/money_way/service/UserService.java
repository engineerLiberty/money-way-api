package com.example.money_way.service;

import com.example.money_way.dto.request.VerifyTokenDto;
import com.example.money_way.dto.response.ApiResponse;

public interface UserService {
    ApiResponse verifyLink(VerifyTokenDto verifyTokenDto);
}

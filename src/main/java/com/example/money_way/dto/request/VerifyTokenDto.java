package com.example.money_way.dto.request;

import com.example.money_way.dto.response.ApiResponse;
import lombok.Data;

@Data
public class VerifyTokenDto {
    private String token;
}

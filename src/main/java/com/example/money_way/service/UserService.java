package com.example.money_way.service;

import com.example.money_way.dto.request.LoginRequestDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<String> login(LoginRequestDto request);
}

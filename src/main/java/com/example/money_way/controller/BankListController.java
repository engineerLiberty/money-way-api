package com.example.money_way.controller;

import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.model.BankList;
import com.example.money_way.service.BankListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/banks")
public class BankListController {
    private final BankListService bankListService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<BankList>>> viewBalance(){
        return ResponseEntity.ok(bankListService.getAllBanks());
    }
}

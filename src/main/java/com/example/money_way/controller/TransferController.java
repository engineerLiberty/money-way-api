package com.example.money_way.controller;

import com.example.money_way.dto.request.TransferBankDto;
import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.dto.response.TransferBankResponse;
import com.example.money_way.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers")
public class TransferController {
    private final TransferService transferService;

    @GetMapping("/fee")
    public ResponseEntity<ApiResponse> getTransferFee(@RequestParam("amount") BigDecimal amount) {
        return ResponseEntity.ok(transferService.getTransferFee(amount));
    }

    @PostMapping("/bank")
    public ResponseEntity<ApiResponse> transferToBank(@RequestBody TransferBankDto transferBankDto) {
        return ResponseEntity.ok(transferService.transferToBank(transferBankDto));
    }

    @PostMapping("/bank/response")
    public ResponseEntity<String> updateTransferToBankResponse(
            @RequestBody TransferBankResponse transferBankResponse) {
        transferService.updateTransferToBankResponse(transferBankResponse);
        return ResponseEntity.ok("Transfer Response Updated");
    }
}

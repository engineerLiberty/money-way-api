package com.example.money_way.service;

import com.example.money_way.dto.request.TransferBankDto;
import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.dto.response.TransferBankResponse;

import java.math.BigDecimal;

public interface TransferService {
    ApiResponse getTransferFee(BigDecimal amount);
    ApiResponse transferToBank(TransferBankDto transferBankDto);
    void updateTransferToBankResponse(TransferBankResponse transferBankResponse);
}

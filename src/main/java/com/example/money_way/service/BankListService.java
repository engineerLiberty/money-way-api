package com.example.money_way.service;

import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.model.BankList;

import java.util.List;

public interface BankListService {
    ApiResponse<List<BankList>> getAllBanks();
    void updateBankList();
}

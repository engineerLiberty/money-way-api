package com.example.money_way.services;

import com.example.money_way.dto.request.CreateWalletRequest;
import com.example.money_way.dto.response.ApiResponse;

public interface WalletService {
ApiResponse createWallet(CreateWalletRequest request);
}

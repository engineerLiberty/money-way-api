package com.example.money_way.services.impl;

import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.dto.response.ViewWalletResponseDto;
import com.example.money_way.exception.ResourceNotFoundException;
import com.example.money_way.model.User;
import com.example.money_way.model.Wallet;
import com.example.money_way.repository.WalletRepository;
import com.example.money_way.services.WalletService;
import com.example.money_way.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;

    private final AppUtil appUtil;
    @Override
    public ApiResponse viewBalance() {

        ViewWalletResponseDto viewWalletResponseDto;


        User user = appUtil.getLoggedInUser();

        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Wallet Not Found"));

        viewWalletResponseDto = ViewWalletResponseDto.builder()
                .walletId(wallet.getId())
                .balance(wallet.getBalance())
                .build();

        return ApiResponse.builder()
                .status("SUCCESS")
                .data(viewWalletResponseDto)
                .build();
    }

}


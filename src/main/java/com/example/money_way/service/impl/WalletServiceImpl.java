package com.example.money_way.service.impl;


import com.example.money_way.dto.request.CreateWalletRequest;
import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.dto.response.CreateWalletResponse;
import com.example.money_way.dto.response.ViewWalletResponseDto;
import com.example.money_way.exception.ResourceNotFoundException;
import com.example.money_way.model.User;
import com.example.money_way.model.Wallet;
import com.example.money_way.repository.UserRepository;
import com.example.money_way.repository.WalletRepository;
import com.example.money_way.service.WalletService;
import com.example.money_way.utils.AppUtil;
import com.example.money_way.utils.EnvironmentVariables;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final RestTemplate restTemplate;
    private final AppUtil appUtil;
    private final EnvironmentVariables environmentVariables;

    @Override
    public ApiResponse createWallet(CreateWalletRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + environmentVariables.getFLW_SECRET_KEY());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateWalletRequest> entity = new HttpEntity<>(request, headers);

        ApiResponse apiResponse = restTemplate.exchange(environmentVariables.getCreateWalletUrl(),
                HttpMethod.POST, entity, ApiResponse.class).getBody();


        CreateWalletResponse walletResponse;
        if (apiResponse != null && apiResponse.getStatus().equalsIgnoreCase("SUCCESS")) {
            walletResponse = appUtil.getObjectMapper().convertValue(apiResponse.getData(), CreateWalletResponse.class);
            User user = userRepository.findByEmail(request.getEmail()).get();
            Wallet wallet = Wallet.builder()
                    .userId(user.getId())
                    .bankName(walletResponse.getBank_name())
                    .accountNumber(walletResponse.getAccount_number())
                    .balance(BigDecimal.valueOf(0.00))
                    .virtualAccountRef(request.getTx_ref())
                    .build();
            walletRepository.save(wallet);
        }else{
            throw new ResourceNotFoundException("Wallet Creation failed: An error has occurred");
        }

        return new ApiResponse("Success", "Wallet created successfully", null);
    }
   
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

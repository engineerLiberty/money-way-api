package com.example.money_way.services.impl;


import com.example.money_way.dto.request.CreateWalletRequest;
import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.dto.response.CreateWalletResponse;
import com.example.money_way.services.WalletService;
import com.example.money_way.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final RestTemplate restTemplate;
    @Value("${app.FLW_SECRET_KEY}")
    private String FLW_SECRET_KEY;

    @Override
    public ApiResponse<CreateWalletResponse> createWallet(CreateWalletRequest request) {

        String url = "https://api.flutterwave.com/v3/virtual-account-numbers";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + FLW_SECRET_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateWalletRequest> entity = new HttpEntity<>(request, headers);

        ApiResponse apiResponse = restTemplate.exchange(url, HttpMethod.POST, entity, ApiResponse.class).getBody();

        AppUtil appUtil = AppUtil.getInstance();

        CreateWalletResponse walletResponse;
        if (apiResponse != null) {
            walletResponse = appUtil.getMapper().convertValue(apiResponse.getData(), CreateWalletResponse.class);
        }else{
            throw new RuntimeException("Wallet Creation failed: An error has occurred");
        }

        return ApiResponse.<CreateWalletResponse>builder()
                .data(walletResponse)
                .message(apiResponse.getMessage())
                .status(apiResponse.getStatus()).build();
    }
}

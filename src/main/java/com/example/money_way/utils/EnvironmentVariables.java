package com.example.money_way.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class EnvironmentVariables {

    @Value("${app.FLW_SECRET_KEY}")
    private String FLW_SECRET_KEY;

    @Value("${app.create_wallet}")
    private String createWalletUrl;
    @Value("${app.get_banks}")
    private String getBankUrl;
    @Value("${app.get_transfer_fee}")
    private String getTransferFeeUrl;
    @Value("${app.transfer_bank}")
    private String getTransferToBankUrl;
    @Value("${app.retry_transfer_bank}")
    private String getRetryTransferToBankUrl;
}

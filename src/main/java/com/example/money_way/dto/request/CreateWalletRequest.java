package com.example.money_way.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateWalletRequest {
    private String account_name;
    private String email;
    private String mobilenumber;
    private String country;
}


package com.example.money_way.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateWalletResponse {
    private Long id;
    private String account_reference;
    private String account_name;
    private String barter_id;
    private String email;
    private String mobilenumber;
    private String country;
    private String nuban;
    private String bank_name;
    private String bank_code;
    private String status;
    private String created_at;
}

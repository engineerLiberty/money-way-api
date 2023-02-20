package com.example.money_way.dto.response;

import lombok.Data;

import java.util.Map;

@Data
public class TransferBankResponse {
    private String status;
    private String message;
    private Map<String, ?> data;
}

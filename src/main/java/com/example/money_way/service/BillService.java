package com.example.money_way.service;

import com.example.money_way.dto.request.TvPurchaseRequest;
import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.dto.response.TvPurchaseResponse;

public interface BillService {
    ApiResponse<TvPurchaseResponse> purchaseTvSubscription(TvPurchaseRequest tvPurchaseRequest);
}

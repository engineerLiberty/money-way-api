package com.example.money_way.controller;

import com.example.money_way.dto.request.TvPurchaseRequest;
import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.dto.response.TvPurchaseResponse;
import com.example.money_way.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bills")
public class BillController {
    private final BillService billService;

        @PostMapping("/tvSubscription")
        public ResponseEntity<ApiResponse<TvPurchaseResponse>> purchaseTvSubscription(@RequestBody TvPurchaseRequest request){
            return ResponseEntity.ok(billService.purchaseTvSubscription(request));
        }
    }

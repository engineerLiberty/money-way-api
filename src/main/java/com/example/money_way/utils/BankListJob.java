package com.example.money_way.utils;

import com.example.money_way.service.BankListService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BankListJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(BankListJob.class);

    private final BankListService bankListService;

    @Scheduled(initialDelay = 1000, fixedRate = 24 * 60 * 60 * 1000)
    public void retrieveBankList() {
        bankListService.updateBankList();
        LOGGER.info("Retrieved bank list");
    }
}

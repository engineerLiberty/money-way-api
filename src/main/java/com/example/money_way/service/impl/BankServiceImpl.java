package com.example.money_way.service.impl;

import com.example.money_way.dto.response.BanksResponse;
import com.example.money_way.model.Bank;
import com.example.money_way.repository.BankRepository;
import com.example.money_way.service.BankService;
import com.example.money_way.utils.EnvironmentVariables;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BankServiceImpl implements BankService {

    private final BankRepository bankListRepository;
    private final RestTemplate restTemplate;
    private final EnvironmentVariables environmentVariables;
    @Override
    public Page<Bank> getAllBanks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return bankListRepository.findAll(pageable);
    }

    @Override
    public void updateBankList() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + environmentVariables.getFLW_SECRET_KEY());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        BanksResponse banksResponse = restTemplate.exchange(environmentVariables.getGetBankUrl(),
                HttpMethod.GET, entity, BanksResponse.class).getBody();

        if (banksResponse != null && banksResponse.getStatus().equalsIgnoreCase("SUCCESS")) {

            for(Map<String, String> bank : banksResponse.getData()){
                Optional<Bank> bankList = bankListRepository.findByBankName(bank.get("name"));

                if(bankList.isEmpty()){
                    Bank newBank = new Bank(
                            bank.get("name"), bank.get("code")
                    );
                    bankListRepository.save(newBank);
                }
            }
        }
    }
}

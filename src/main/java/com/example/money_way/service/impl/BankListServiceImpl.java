package com.example.money_way.service.impl;

import com.example.money_way.dto.request.CreateWalletRequest;
import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.model.BankList;
import com.example.money_way.repository.BankListRepository;
import com.example.money_way.service.BankListService;
import com.example.money_way.utils.EnvironmentVariables;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BankListServiceImpl implements BankListService {

    private final BankListRepository bankListRepository;
    private final RestTemplate restTemplate;
    private final EnvironmentVariables environmentVariables;
    @Override
    public ApiResponse<List<BankList>> getAllBanks() {

        List<BankList> bankLists = bankListRepository.findAll();

        ApiResponse<List<BankList>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("Success");
        apiResponse.setMessage("List of All Available Banks");
        apiResponse.setData(bankLists);

        return apiResponse;
    }

    @Override
    public void updateBankList() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + environmentVariables.getFLW_SECRET_KEY());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateWalletRequest> entity = new HttpEntity<>(headers);

        ApiResponse apiResponse = restTemplate.exchange(environmentVariables.getGetBankUrl(),
                HttpMethod.GET, entity, ApiResponse.class).getBody();

        if (apiResponse != null && apiResponse.getStatus().equalsIgnoreCase("SUCCESS")) {

            List<Map<String, String>> banks = (List<Map<String, String>>) apiResponse.getData();

            for(Map<String, String> bank : banks){
                Optional<BankList> bankList = bankListRepository.findByBankName(bank.get("name"));

                if(bankList.isEmpty()){
                    BankList newBank = new BankList(
                            bank.get("name"), bank.get("code")
                    );
                    bankListRepository.save(newBank);
                }
            }
        }
    }
}

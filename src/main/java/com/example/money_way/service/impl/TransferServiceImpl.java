package com.example.money_way.service.impl;

import com.example.money_way.dto.request.TransferBankDto;
import com.example.money_way.dto.request.TransferBankRequest;
import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.dto.response.TransferBankResponse;
import com.example.money_way.dto.response.TransferFeeResponse;
import com.example.money_way.enums.Status;
import com.example.money_way.enums.Type;
import com.example.money_way.exception.InvalidCredentialsException;
import com.example.money_way.exception.InvalidTransactionException;
import com.example.money_way.model.*;
import com.example.money_way.repository.*;
import com.example.money_way.service.TransferService;
import com.example.money_way.utils.AppUtil;
import com.example.money_way.utils.EnvironmentVariables;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TransferServiceImpl implements TransferService {
    private final RestTemplate restTemplate;
    private final EnvironmentVariables environmentVariables;
    private final AppUtil appUtil;
    private final WalletRepository walletRepository;
    private final BankListRepository bankListRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransferRepository transferRepository;
    private final TransactionRepository transactionRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<BigDecimal> getTransferFee(BigDecimal amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + environmentVariables.getFLW_SECRET_KEY());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder url = UriComponentsBuilder
                .fromUriString(environmentVariables.getGetTransferFeeUrl())
                .queryParam("amount", amount)
                .queryParam("currency", "NGN");

        TransferFeeResponse feeResponse = restTemplate.exchange(url.toUriString(),
                HttpMethod.GET, entity, TransferFeeResponse.class).getBody();

        ApiResponse<BigDecimal> apiResponse = new ApiResponse<>();
        assert feeResponse != null;
        apiResponse.setStatus(feeResponse.getStatus());
        apiResponse.setMessage(feeResponse.getMessage());
        apiResponse.setData(BigDecimal.valueOf((double) feeResponse.getData().get(0).get("fee")));

        return apiResponse;
    }

    @Override
    @Transactional
    public ApiResponse transferToBank(TransferBankDto transferBankDto) {

        User user = appUtil.getLoggedInUser();
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow();

        BigDecimal fee = getTransferFee(transferBankDto.getAmount()).getData();
        BigDecimal total = transferBankDto.getAmount().add(fee);

        //Checking if user balance is greater than amount + fee
        if (wallet.getBalance().compareTo(total) < 0){
            throw new InvalidTransactionException("Insufficient wallet balance");
        }

        //Checking if user pin matches
        if (!passwordEncoder.matches(transferBankDto.getPin(), user.getPin())) {
            throw new InvalidCredentialsException("Incorrect Pin");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + environmentVariables.getFLW_SECRET_KEY());
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Generating a reference code
        String ref = appUtil.generateReference();

        //Finding bank by name and getting the bank code
        BankList bankList = bankListRepository.findByBankName(transferBankDto.getAccount_bank())
                .orElseThrow();
        String bankCode = bankList.getBankCode();

        TransferBankRequest requestBody = TransferBankRequest.builder()
                .account_bank("044")
                .account_number("0690000031")
                .amount(transferBankDto.getAmount())
                .narration(transferBankDto.getDescription())
                .currency("NGN")
                .reference(ref + "_PMCK_ST_FDU_1")
                .build();

        HttpEntity<TransferBankRequest> entity = new HttpEntity<>(requestBody, headers);

        TransferBankResponse transferBankResponse = restTemplate.exchange(environmentVariables.getGetTransferToBankUrl(),
                HttpMethod.POST, entity, TransferBankResponse.class).getBody();

        //checking if transfer not queued successfully, then retry transfer
        if (transferBankResponse != null && !transferBankResponse.getStatus().equalsIgnoreCase("success")){
            Map<String, String> uriVariables = new HashMap<>();
            uriVariables.put("id", (String) transferBankResponse.getData().get("id"));

            transferBankResponse = restTemplate.exchange(environmentVariables.getGetRetryTransferToBankUrl(),
                    HttpMethod.POST, entity, TransferBankResponse.class, uriVariables).getBody();
        }

        //checking if transfer queued successfully
        if (transferBankResponse != null && transferBankResponse.getStatus().equalsIgnoreCase("success")){
            //Subtracting total from user wallet balance
            wallet.setBalance(wallet.getBalance().subtract(total));
            walletRepository.save(wallet);

            //Saving transfer to database
            Transfer transfer = new Transfer();
            transfer.setBankName(transferBankDto.getAccount_bank());
            transfer.setAccountNumber(transferBankDto.getAccount_number());
            transfer.setDescription(transferBankDto.getDescription());
            transfer.setReferenceId(ref);
            transfer.setAmount(transferBankDto.getAmount());
            transfer.setUserId(user.getId());
            transferRepository.save(transfer);

            //Saving transaction to database
            Transaction transaction = new Transaction();
            transaction.setTransactionId(Long.valueOf((Integer) transferBankResponse.getData().get("id")));
            transaction.setCurrency((String) transferBankResponse.getData().get("currency"));
            transaction.setAmount(BigDecimal.valueOf((Integer) transferBankResponse.getData().get("amount")));
            transaction.setVirtualAccountRef((String) transferBankResponse.getData().get("reference"));
            transaction.setDescription((String) transferBankResponse.getData().get("narration"));
            transaction.setStatus(Status.PENDING);
            transaction.setResponseMessage(transferBankResponse.getMessage());
            transaction.setProviderStatus((String) transferBankResponse.getData().get("status"));
            transaction.setPaymentType("BANK");
            transaction.setUserId(user.getId());
            transactionRepository.save(transaction);

            //Checking if user wants to save beneficiary
            if (transferBankDto.getSaveBeneficiary()){
                Optional<Beneficiary> beneficiary = beneficiaryRepository
                        .findByAccountNumberAndUserId(transferBankDto.getAccount_number(), user.getId());

                if (beneficiary.isEmpty()){
                    Beneficiary newBeneficiary = new Beneficiary();
                    newBeneficiary.setName(transferBankDto.getBeneficiaryName());
                    newBeneficiary.setBankName(transferBankDto.getAccount_bank());
                    newBeneficiary.setAccountNumber(transfer.getAccountNumber());
                    newBeneficiary.setType(Type.BANK);
                    newBeneficiary.setUserId(user.getId());

                    beneficiaryRepository.save(newBeneficiary);
                }
            }
        }

        ApiResponse<Map<String, ?>> apiResponse = new ApiResponse<>();
        assert transferBankResponse != null;
        apiResponse.setStatus("PENDING");
        apiResponse.setMessage(transferBankResponse.getMessage());

        return apiResponse;
    }

    @Override
    @Transactional
    public void updateTransferToBankResponse(TransferBankResponse transferBankResponse) {
        Map<String, ?> data = transferBankResponse.getData();

        //fetching transaction to be updated
        Transaction transaction = transactionRepository
                .findByVirtualAccountRef((String) data.get("reference"))
                .orElseThrow();

        //checking if transaction is successful and updating status
        if (data.get("status").equals("SUCCESSFUL")){
            transaction.setStatus(Status.SUCCESS);
        } else {
            //adding amount and fee back to user wallet and updating status when transaction not successful
            User user = userRepository.findById(transaction.getUserId())
                    .orElseThrow();
            Wallet wallet = walletRepository.findByUserId(user.getId())
                    .orElseThrow();

            BigDecimal fee = BigDecimal.valueOf((double) data.get("fee"));
            BigDecimal amount = BigDecimal.valueOf((Integer) data.get("amount"));
            BigDecimal total = amount.add(fee);

            wallet.setBalance(wallet.getBalance().add(total));
            walletRepository.save(wallet);

            transaction.setStatus(Status.FAILED);
        }

        //updating transaction status and message from provider depending on status
        transaction.setProviderStatus((String) data.get("status"));
        transaction.setResponseMessage((String) data.get("complete_message"));
        transactionRepository.save(transaction);
    }
}

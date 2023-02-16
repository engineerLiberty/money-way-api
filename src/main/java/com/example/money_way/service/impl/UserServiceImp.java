package com.example.money_way.service.impl;

import antlr.Utils;
import com.example.money_way.dto.request.VerifyTokenDto;
import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.exception.UserNotFound;
import com.example.money_way.model.User;
import com.example.money_way.model.Wallet;
import com.example.money_way.repository.UserRepository;
import com.example.money_way.service.UserService;
import com.example.money_way.utils.AppUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@Data
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private AppUtil appUtil;

    @Value("${app.Wallet_Prefix}")
    private String E_Wallet_Prefix;
    private UserRepository userRepository;
    @Override
    public ApiResponse verifyLink(VerifyTokenDto verifyTokenDto) {

        ApiResponse apiResponse = new ApiResponse<>();

        Optional<User>existingUser = userRepository.findByConfirmationToken(appUtil.getLoggedInUser().getConfirmationToken());
        Wallet wallet = new Wallet();
        if (existingUser.isPresent()) {
            existingUser.get().setConfirmationToken(null);
            existingUser.get().setActive(true);
            wallet.setUserId(appUtil.getLoggedInUser().getId());
            return apiResponse.builder().message("Success").status("Account created successfully").build();
        }
        if (!existingUser.isPresent()){
                throw new UserNotFound("Error: No Account found!");

        } else {
            return apiResponse.builder().message("Invalid token").status("Failed").build();
        }
    }
}

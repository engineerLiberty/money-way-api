package com.example.money_way.service.impl;

import com.example.money_way.dto.PasswordResetDTO;
import com.example.money_way.exception.ApiResponse;
import com.example.money_way.exception.InvalidCredentialsException;
import com.example.money_way.model.User;
import com.example.money_way.repository.UserRepository;
import com.example.money_way.service.UserService;
import com.example.money_way.utils.AppUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

//    private Javamail javamail


    @Override
    public ApiResponse<String> updatePassword(PasswordResetDTO passwordResetDTO) {
        AppUtil appUtil = AppUtil.getInstance();


        String currentPassword = passwordResetDTO.getCurrentPassword();
        String newPassword = passwordResetDTO.getNewPassword();
        String confirmNewPassword = passwordResetDTO.getConfirmNewPassword();

         User user = appUtil.getLoggedInUser();

        String savedPassword = user.getPassword();

        if(!passwordEncoder.matches(currentPassword, savedPassword))
            throw new InvalidCredentialsException("Credentials do not match");
        if(!confirmNewPassword.equals(newPassword))
            throw new InvalidCredentialsException("New password must match confirm password");
        else
            passwordResetDTO.setConfirmNewPassword(newPassword);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new ApiResponse<>("Password reset successful", "Success", null);


    }

}


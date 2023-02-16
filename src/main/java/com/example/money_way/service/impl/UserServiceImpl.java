package com.example.money_way.service.impl;

import com.example.money_way.configuration.mail.EmailService;
import com.example.money_way.dto.request.PasswordResetDTO;
import com.example.money_way.dto.response.ApiResponse;
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
    private final AppUtil appUtil;

    private final EmailService emailService;


    @Override
    public ApiResponse<String> updatePassword(PasswordResetDTO passwordResetDTO) {

        String currentPassword = passwordResetDTO.getCurrentPassword();
        String newPassword = passwordResetDTO.getNewPassword();


        User user = appUtil.getLoggedInUser();

        String savedPassword = user.getPassword();

        if(!passwordEncoder.matches(currentPassword, savedPassword))
            throw new InvalidCredentialsException("Credentials must match");

        else
            passwordResetDTO.setNewPassword(newPassword);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        emailService.sendEmail(user.getEmail(), "Update Password", "Your password has been updated  successfully. Ensure to keep it a secret. Never disclose your password to a third party.");
        return new ApiResponse<>( "Success", "Password reset successful", null);


    }

}


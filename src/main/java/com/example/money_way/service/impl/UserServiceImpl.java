package com.example.money_way.service.impl;

import com.example.money_way.configuration.mail.EmailService;
import com.example.money_way.configuration.security.CustomUserDetailService;
import com.example.money_way.configuration.security.JwtUtils;
import com.example.money_way.dto.request.LoginRequestDto;
import com.example.money_way.dto.request.SignUpDto;
import com.example.money_way.dto.response.ApiResponse;
import com.example.money_way.exception.ValidationException;
import com.example.money_way.model.User;
import com.example.money_way.repository.UserRepository;
import com.example.money_way.service.UserService;
import com.example.money_way.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService customUserDetailService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    private final AppUtil appUtils;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<String> login(LoginRequestDto request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetails user = customUserDetailService.loadUserByUsername(request.getEmail());

        if (user != null) {
            return ResponseEntity.ok(jwtUtils.generateToken(user));
        }
        return ResponseEntity.status(400).body("Some Error Occurred");
    }

    @Override
    public ResponseEntity<ApiResponse> signUp(SignUpDto signUpDto) throws ValidationException {

        Boolean isUserExist = userRepository.existsByEmail(signUpDto.getEmail());
        if (isUserExist)
            throw new ValidationException("User Already Exists!");

        User user = new User();
        user.setFirstName(signUpDto.getFirstName());
        user.setLastName(signUpDto.getLastName());
        user.setEmail(signUpDto.getEmail());
        user.setPhoneNumber(signUpDto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setBvn(signUpDto.getBvn());
        user.setPin(passwordEncoder.encode(signUpDto.getPin()));
        String token = jwtUtils.generateSignUpConfirmationToken(signUpDto.getEmail());
        user.setVerified(false);
        userRepository.save(user);

        String link = "Hello "  + signUpDto.getFirstName()  +
                "Copy the token below to activate your account: " + token;
        emailService.sendEmail(signUpDto.getEmail(),"MoneyWay: Verify Your Account", link);

        return ResponseEntity.ok(new ApiResponse<>("Successful", "SignUp Successful. Check your mail to activate your account", null));
    }
}

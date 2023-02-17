package com.example.money_way.controller;
        import com.example.money_way.dto.request.LoginRequestDto;
        import com.example.money_way.service.UserService;
        import lombok.RequiredArgsConstructor;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RequestBody;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class UsersController {
    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestdto) {
        return userService.login(loginRequestdto);
    }
}
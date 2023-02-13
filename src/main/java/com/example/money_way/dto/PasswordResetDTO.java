package com.example.money_way.dto;


import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
@Getter
@Setter
@AllArgsConstructor

public class PasswordResetDTO {
    @NotNull(message = "Enter your current password")
    private String currentPassword;

    @NotNull(message = "Enter a new password")
    private String newPassword;

    @NotNull(message = "Confirm your new password")
    private String confirmNewPassword;
}

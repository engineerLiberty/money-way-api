package com.example.money_way.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class CreateWalletRequest {
    @NotNull(message = "Field cannot be empty, provide a valid email")
    @Email(message = "provide a valid email")
    private String email;

    @Size(min = 11, max = 11)
    private String bvn;

    private String currency;

    private BigDecimal amount;

    private String tx_ref;

    private String is_permanent;

    private String narration;
}
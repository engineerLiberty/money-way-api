package com.example.money_way.model;

import com.example.money_way.enums.Type;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "beneficiary_tbl")
public class Beneficiary extends Base{
    private String name;
    private String accountNumber;
    private String phoneNumber;
    private String email;
    private Type type;
    private String bankName;
    @Column(nullable = false)
    private Long userId;
}

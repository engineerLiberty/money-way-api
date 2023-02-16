package com.example.money_way.model;

import com.example.money_way.enums.Status;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transaction_tbl")
public class Transaction extends Base {

    private Long flutterTransactionId;

    @Column(nullable = false)
    private String currency;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private String txReferenceId;
    private String description;
    private Status status;
    private String responseMessage;
    private String providerStatus;
    private String paymentType;
    @Column(nullable = false)
    private Long userId;


}

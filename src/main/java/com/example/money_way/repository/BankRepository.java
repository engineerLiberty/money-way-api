package com.example.money_way.repository;

import com.example.money_way.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long> {
//    Page<Bank> findAll(Pageable pageable);
    Optional<Bank> findByBankName(String bankName);
}

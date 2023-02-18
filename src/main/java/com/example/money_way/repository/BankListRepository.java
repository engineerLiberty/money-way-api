package com.example.money_way.repository;

import com.example.money_way.model.BankList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankListRepository extends JpaRepository<BankList, Long> {
    Optional<BankList> findByBankName(String bankName);
}

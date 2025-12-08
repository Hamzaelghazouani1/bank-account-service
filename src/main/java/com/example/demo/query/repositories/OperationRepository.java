package com.example.demo.query.repositories;

import com.example.demo.query.entities.Account;
import com.example.demo.query.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<AccountOperation, Long> {
    List<AccountOperation> findByAccountOrderByOperationDateDesc(Account account);
}

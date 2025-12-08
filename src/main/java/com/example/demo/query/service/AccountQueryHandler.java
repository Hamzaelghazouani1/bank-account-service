package com.example.demo.query.service;

import com.example.demo.query.dto.AccountDTO;
import com.example.demo.query.dto.AccountStatementDTO;
import com.example.demo.query.dto.OperationDTO;
import com.example.demo.query.entities.Account;
import com.example.demo.query.queries.GetAccountByIdQuery;
import com.example.demo.query.queries.GetAccountStatementQuery;
import com.example.demo.query.queries.GetAllAccountsQuery;
import com.example.demo.query.repositories.AccountRepository;
import com.example.demo.query.repositories.OperationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountQueryHandler {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    @QueryHandler
    public Account handle(GetAccountByIdQuery query) {
        log.info("Handling GetAccountByIdQuery for account: {}", query.getAccountId());
        return accountRepository.findById(query.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found: " + query.getAccountId()));
    }

    @QueryHandler
    public List<Account> handle(GetAllAccountsQuery query) {
        log.info("Handling GetAllAccountsQuery");
        return accountRepository.findAll();
    }

    @QueryHandler
    public AccountStatementDTO handle(GetAccountStatementQuery query) {
        log.info("Handling GetAccountStatementQuery for account: {}", query.getAccountId());

        Account account = accountRepository.findById(query.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found: " + query.getAccountId()));

        // Map account to DTO
        AccountDTO accountDTO = new AccountDTO(
                account.getId(),
                account.getBalance(),
                account.getStatus(),
                account.getCurrency(),
                account.getCreatedAt());

        // Get operations and map to DTOs
        List<OperationDTO> operationDTOs = operationRepository
                .findByAccountOrderByOperationDateDesc(account)
                .stream()
                .map(op -> new OperationDTO(
                        op.getId(),
                        op.getOperationDate(),
                        op.getAmount(),
                        op.getType()))
                .collect(Collectors.toList());

        return new AccountStatementDTO(accountDTO, operationDTOs);
    }
}

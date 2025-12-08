package com.example.demo.query.service;

import com.example.demo.common.enums.AccountStatus;
import com.example.demo.common.enums.OperationType;
import com.example.demo.common.events.AccountActivatedEvent;
import com.example.demo.common.events.AccountCreatedEvent;
import com.example.demo.common.events.AccountCreditedEvent;
import com.example.demo.common.events.AccountDebitedEvent;
import com.example.demo.query.entities.Account;
import com.example.demo.query.entities.AccountOperation;
import com.example.demo.query.queries.GetAccountByIdQuery;
import com.example.demo.query.repositories.AccountRepository;
import com.example.demo.query.repositories.OperationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountEventHandler {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final QueryUpdateEmitter queryUpdateEmitter;

    @EventHandler
    public void on(AccountCreatedEvent event, @Timestamp Instant timestamp) {
        log.info("Projecting AccountCreatedEvent for account: {}", event.getId());

        Account account = new Account();
        account.setId(event.getId());
        account.setBalance(event.getInitialBalance());
        account.setCurrency(event.getCurrency());
        account.setStatus(AccountStatus.CREATED);
        account.setCreatedAt(Date.from(timestamp));

        accountRepository.save(account);

        // Emit update for subscription queries
        queryUpdateEmitter.emit(
                GetAccountByIdQuery.class,
                query -> query.getAccountId().equals(event.getId()),
                account);
    }

    @EventHandler
    public void on(AccountActivatedEvent event) {
        log.info("Projecting AccountActivatedEvent for account: {} with status: {}", event.getId(), event.getStatus());

        accountRepository.findById(event.getId()).ifPresent(account -> {
            account.setStatus(event.getStatus());
            accountRepository.save(account);

            // Emit update for subscription queries
            queryUpdateEmitter.emit(
                    GetAccountByIdQuery.class,
                    query -> query.getAccountId().equals(event.getId()),
                    account);
        });
    }

    @EventHandler
    public void on(AccountCreditedEvent event, @Timestamp Instant timestamp) {
        log.info("Projecting AccountCreditedEvent for account: {} amount: {}", event.getId(), event.getAmount());

        accountRepository.findById(event.getId()).ifPresent(account -> {
            // Update balance
            account.setBalance(account.getBalance() + event.getAmount());
            accountRepository.save(account);

            // Create operation record
            AccountOperation operation = new AccountOperation();
            operation.setOperationDate(Date.from(timestamp));
            operation.setAmount(event.getAmount());
            operation.setType(OperationType.CREDIT);
            operation.setAccount(account);
            operationRepository.save(operation);

            // Emit update for subscription queries
            queryUpdateEmitter.emit(
                    GetAccountByIdQuery.class,
                    query -> query.getAccountId().equals(event.getId()),
                    account);
        });
    }

    @EventHandler
    public void on(AccountDebitedEvent event, @Timestamp Instant timestamp) {
        log.info("Projecting AccountDebitedEvent for account: {} amount: {}", event.getId(), event.getAmount());

        accountRepository.findById(event.getId()).ifPresent(account -> {
            // Update balance
            account.setBalance(account.getBalance() - event.getAmount());
            accountRepository.save(account);

            // Create operation record
            AccountOperation operation = new AccountOperation();
            operation.setOperationDate(Date.from(timestamp));
            operation.setAmount(event.getAmount());
            operation.setType(OperationType.DEBIT);
            operation.setAccount(account);
            operationRepository.save(operation);

            // Emit update for subscription queries
            queryUpdateEmitter.emit(
                    GetAccountByIdQuery.class,
                    query -> query.getAccountId().equals(event.getId()),
                    account);
        });
    }
}

package ma.enset.hamzaelghazouani.demo.commands.aggregates;

import ma.enset.hamzaelghazouani.demo.commands.commands.CreateAccountCommand;
import ma.enset.hamzaelghazouani.demo.commands.commands.CreditAccountCommand;
import ma.enset.hamzaelghazouani.demo.commands.commands.DebitAccountCommand;
import ma.enset.hamzaelghazouani.demo.commands.commands.UpdateAccountStatusCommand;
import ma.enset.hamzaelghazouani.demo.commands.exceptions.AccountNotActivatedException;
import ma.enset.hamzaelghazouani.demo.commands.exceptions.InsufficientBalanceException;
import ma.enset.hamzaelghazouani.demo.common.enums.AccountStatus;
import ma.enset.hamzaelghazouani.demo.common.events.AccountActivatedEvent;
import ma.enset.hamzaelghazouani.demo.common.events.AccountCreatedEvent;
import ma.enset.hamzaelghazouani.demo.common.events.AccountCreditedEvent;
import ma.enset.hamzaelghazouani.demo.common.events.AccountDebitedEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor
@Slf4j
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;
    private double balance;
    private AccountStatus status;
    private String currency;

    // ==================== COMMAND HANDLERS ====================

    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {
        log.info("CreateAccountCommand received for account: {}", command.getId());

        // Validation
        if (command.getInitialBalance() < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        if (command.getCurrency() == null || command.getCurrency().isBlank()) {
            throw new IllegalArgumentException("Currency is required");
        }

        // Apply AccountCreatedEvent
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(),
                command.getInitialBalance(),
                command.getCurrency()));

        // Auto-activate the account after creation
        AggregateLifecycle.apply(new AccountActivatedEvent(
                command.getId(),
                AccountStatus.ACTIVATED));
    }

    @CommandHandler
    public void handle(CreditAccountCommand command) {
        log.info("CreditAccountCommand received: {} for account: {}", command.getAmount(), command.getId());

        // Validation: account must be activated
        if (this.status != AccountStatus.ACTIVATED) {
            throw new AccountNotActivatedException(command.getId(), this.status.toString());
        }

        if (command.getAmount() <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }

        // Apply AccountCreditedEvent
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()));
    }

    @CommandHandler
    public void handle(DebitAccountCommand command) {
        log.info("DebitAccountCommand received: {} for account: {}", command.getAmount(), command.getId());

        // Validation: account must be activated
        if (this.status != AccountStatus.ACTIVATED) {
            throw new AccountNotActivatedException(command.getId(), this.status.toString());
        }

        if (command.getAmount() <= 0) {
            throw new IllegalArgumentException("Debit amount must be positive");
        }

        // Validation: sufficient balance
        if (this.balance < command.getAmount()) {
            throw new InsufficientBalanceException(command.getAmount(), this.balance);
        }

        // Apply AccountDebitedEvent
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()));
    }

    @CommandHandler
    public void handle(UpdateAccountStatusCommand command) {
        log.info("UpdateAccountStatusCommand received: {} for account: {}", command.getStatus(), command.getId());

        // Apply AccountActivatedEvent (reused for status changes)
        AggregateLifecycle.apply(new AccountActivatedEvent(
                command.getId(),
                command.getStatus()));
    }

    // ==================== EVENT SOURCING HANDLERS ====================

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        log.info("AccountCreatedEvent applied for account: {}", event.getId());
        this.accountId = event.getId();
        this.balance = event.getInitialBalance();
        this.currency = event.getCurrency();
        this.status = AccountStatus.CREATED;
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        log.info("AccountActivatedEvent applied for account: {} with status: {}", event.getId(), event.getStatus());
        this.status = event.getStatus();
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        log.info("AccountCreditedEvent applied for account: {} amount: {}", event.getId(), event.getAmount());
        this.balance += event.getAmount();
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        log.info("AccountDebitedEvent applied for account: {} amount: {}", event.getId(), event.getAmount());
        this.balance -= event.getAmount();
    }
}

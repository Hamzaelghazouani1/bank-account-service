package ma.enset.hamzaelghazouani.demo.commands.controllers;

import ma.enset.hamzaelghazouani.demo.commands.commands.CreateAccountCommand;
import ma.enset.hamzaelghazouani.demo.commands.commands.CreditAccountCommand;
import ma.enset.hamzaelghazouani.demo.commands.commands.DebitAccountCommand;
import ma.enset.hamzaelghazouani.demo.commands.dto.CreateAccountRequestDTO;
import ma.enset.hamzaelghazouani.demo.commands.dto.CreditAccountRequestDTO;
import ma.enset.hamzaelghazouani.demo.commands.dto.DebitAccountRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/commands/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Commands", description = "Write operations for bank accounts (CQRS Command Side)")
public class AccountCommandController {

    private final CommandGateway commandGateway;

    @PostMapping("/create")
    @Operation(summary = "Create a new bank account", description = "Creates a new account with initial balance and currency")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequestDTO request) {
        String accountId = UUID.randomUUID().toString();

        return commandGateway.send(new CreateAccountCommand(
                accountId,
                request.getInitialBalance(),
                request.getCurrency()));
    }

    @PutMapping("/credit")
    @Operation(summary = "Credit an account", description = "Add money to an existing account")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO request) {
        return commandGateway.send(new CreditAccountCommand(
                request.getAccountId(),
                request.getAmount(),
                request.getCurrency()));
    }

    @PutMapping("/debit")
    @Operation(summary = "Debit an account", description = "Withdraw money from an existing account")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO request) {
        return commandGateway.send(new DebitAccountCommand(
                request.getAccountId(),
                request.getAmount(),
                request.getCurrency()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}

package ma.enset.hamzaelghazouani.demo.query.controllers;

import ma.enset.hamzaelghazouani.demo.query.dto.AccountStatementDTO;
import ma.enset.hamzaelghazouani.demo.query.entities.Account;
import ma.enset.hamzaelghazouani.demo.query.queries.GetAccountByIdQuery;
import ma.enset.hamzaelghazouani.demo.query.queries.GetAccountStatementQuery;
import ma.enset.hamzaelghazouani.demo.query.queries.GetAllAccountsQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/query/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Queries", description = "Read operations for bank accounts (CQRS Query Side)")
public class AccountQueryController {

    private final QueryGateway queryGateway;

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", description = "Retrieves account details by ID")
    public CompletableFuture<Account> getAccountById(@PathVariable String id) {
        return queryGateway.query(
                new GetAccountByIdQuery(id),
                Account.class);
    }

    @GetMapping
    @Operation(summary = "Get all accounts", description = "Retrieves all accounts")
    public CompletableFuture<List<Account>> getAllAccounts() {
        return queryGateway.query(
                new GetAllAccountsQuery(),
                ResponseTypes.multipleInstancesOf(Account.class));
    }

    @GetMapping("/{id}/statement")
    @Operation(summary = "Get account statement", description = "Retrieves account with all operations history")
    public CompletableFuture<AccountStatementDTO> getAccountStatement(@PathVariable String id) {
        return queryGateway.query(
                new GetAccountStatementQuery(id),
                AccountStatementDTO.class);
    }

    @GetMapping(value = "/{id}/watch", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Watch account updates (SSE)", description = "Real-time subscription to account changes via Server-Sent Events")
    public Flux<Account> watchAccount(@PathVariable String id) {
        SubscriptionQueryResult<Account, Account> subscriptionQuery = queryGateway.subscriptionQuery(
                new GetAccountByIdQuery(id),
                ResponseTypes.instanceOf(Account.class),
                ResponseTypes.instanceOf(Account.class));

        return Flux.concat(
                subscriptionQuery.initialResult().flux(),
                subscriptionQuery.updates()).doFinally(signal -> subscriptionQuery.close());
    }
}

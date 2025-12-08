# Bank-Account-Service

Service bancaire basé sur l'architecture **CQRS** (Command Query Responsibility Segregation) et **Event Sourcing** avec Axon Framework.

## Stack Technique

- Java 17
- Spring Boot 3.2.0
- Axon Framework 4.10.0
- Lombok 1.18.34
- SpringDoc OpenAPI (Swagger)
- Spring WebFlux (SSE)
- PostgreSQL (Production)
- H2 Database (Development)
- Docker

## Architecture

```
bank-account-service/
├── pom.xml
├── docker-compose.yml
├── src/main/java/com/example/demo/
│   ├── DemoAxonApplication.java
│   ├── commands/                    # Write Side (CQRS)
│   │   ├── aggregates/
│   │   │   └── AccountAggregate.java
│   │   ├── commands/
│   │   │   ├── CreateAccountCommand.java
│   │   │   ├── CreditAccountCommand.java
│   │   │   ├── DebitAccountCommand.java
│   │   │   └── UpdateAccountStatusCommand.java
│   │   ├── controllers/
│   │   │   └── AccountCommandController.java
│   │   ├── dto/
│   │   │   ├── CreateAccountRequestDTO.java
│   │   │   ├── CreditAccountRequestDTO.java
│   │   │   └── DebitAccountRequestDTO.java
│   │   └── exceptions/
│   │       ├── AccountNotActivatedException.java
│   │       └── InsufficientBalanceException.java
│   ├── query/                       # Read Side (CQRS)
│   │   ├── entities/
│   │   │   ├── Account.java
│   │   │   └── AccountOperation.java
│   │   ├── repositories/
│   │   │   ├── AccountRepository.java
│   │   │   └── OperationRepository.java
│   │   ├── queries/
│   │   │   ├── GetAccountByIdQuery.java
│   │   │   ├── GetAccountStatementQuery.java
│   │   │   ├── GetAllAccountsQuery.java
│   │   │   └── WatchAccountQuery.java
│   │   ├── dto/
│   │   │   ├── AccountDTO.java
│   │   │   ├── AccountStatementDTO.java
│   │   │   └── OperationDTO.java
│   │   ├── controllers/
│   │   │   └── AccountQueryController.java
│   │   └── service/
│   │       ├── AccountEventHandler.java
│   │       └── AccountQueryHandler.java
│   └── common/
│       ├── enums/
│       │   ├── AccountStatus.java
│       │   └── OperationType.java
│       └── events/
│           ├── AccountCreatedEvent.java
│           ├── AccountActivatedEvent.java
│           ├── AccountCreditedEvent.java
│           └── AccountDebitedEvent.java
└── src/main/resources/
    ├── application.properties
    └── application-prod.properties
```

## CQRS/Event Sourcing Pattern

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT REQUEST                            │
└─────────────────────────────────────────────────────────────────┘
                              │
          ┌───────────────────┴───────────────────┐
          ▼                                       ▼
┌─────────────────────┐                 ┌─────────────────────┐
│   COMMAND SIDE      │                 │    QUERY SIDE       │
│   (Write Model)     │                 │    (Read Model)     │
├─────────────────────┤                 ├─────────────────────┤
│ AccountController   │                 │ QueryController     │
│        │            │                 │        │            │
│        ▼            │                 │        ▼            │
│ CommandGateway      │                 │ QueryGateway        │
│        │            │                 │        │            │
│        ▼            │                 │        ▼            │
│ AccountAggregate    │                 │ QueryHandler        │
│   @CommandHandler   │                 │        │            │
│   @EventSourcingH.  │                 │        ▼            │
│        │            │                 │ PostgreSQL (Read)   │
│        ▼            │                 └─────────────────────┘
│  Axon Event Store   │◄───────────────────────┐
└─────────────────────┘                        │
          │                                    │
          ▼                                    │
┌─────────────────────┐                        │
│  AccountEventHandler│────────────────────────┘
│    (Projections)    │
│   @EventHandler     │
└─────────────────────┘
```

## Infrastructure Docker

| Service | Port | Description |
|---------|------|-------------|
| **Axon Server** | 8024 (Dashboard), 8124 (gRPC) | Event Store |
| **PostgreSQL** | 5432 | Base de lecture (Read DB) |
| **Application** | 8088 | Spring Boot API |

## Ordre de Démarrage

1. **Docker Compose** (Axon Server + PostgreSQL)
2. **Spring Boot Application**

```bash
# 1. Démarrer l'infrastructure
docker-compose up -d

# 2. Démarrer l'application (mode dev - H2)
mvn spring-boot:run

# 2. Ou démarrer l'application (mode prod - PostgreSQL)
mvn spring-boot:run -Dspring.profiles.active=prod
```

## URLs de Test

### APIs
| URL | Description |
|-----|-------------|
| http://localhost:8088/swagger-ui.html | Swagger UI (Documentation API) |
| http://localhost:8088/api-docs | OpenAPI JSON |
| http://localhost:8088/h2-console | H2 Database Console (dev mode) |
| http://localhost:8024 | Axon Server Dashboard |

### Command Endpoints (Write)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/commands/accounts/create` | Créer un compte |
| PUT | `/commands/accounts/credit` | Créditer un compte |
| PUT | `/commands/accounts/debit` | Débiter un compte |

### Query Endpoints (Read)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/query/accounts` | Liste tous les comptes |
| GET | `/query/accounts/{id}` | Détails d'un compte |
| GET | `/query/accounts/{id}/statement` | Relevé de compte |
| GET | `/query/accounts/{id}/watch` | SSE - Mises à jour temps réel |

## Configuration H2 Console

- **JDBC URL**: `jdbc:h2:mem:bank-db`
- **Username**: `sa`
- **Password**: 

## Exemples de Requêtes

### Créer un compte
```json
POST /commands/accounts/create
{
  "initialBalance": 1000.00,
  "currency": "EUR"
}
```

### Créditer un compte
```json
PUT /commands/accounts/credit
{
  "accountId": "uuid-du-compte",
  "amount": 500.00,
  "currency": "EUR"
}
```

### Débiter un compte
```json
PUT /commands/accounts/debit
{
  "accountId": "uuid-du-compte",
  "amount": 200.00,
  "currency": "EUR"
}
```

## Events (Event Sourcing)

| Event | Description |
|-------|-------------|
| `AccountCreatedEvent` | Compte créé avec solde initial |
| `AccountActivatedEvent` | Compte activé (statut changé) |
| `AccountCreditedEvent` | Argent ajouté au compte |
| `AccountDebitedEvent` | Argent retiré du compte |


## Statuts de Compte

| Statut | Description |
|--------|-------------|
| `CREATED` | Compte créé (non actif) |
| `ACTIVATED` | Compte actif (opérations autorisées) |
| `SUSPENDED` | Compte suspendu |
| `CLOSED` | Compte fermé |

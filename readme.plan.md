# Bank-Account-Service : Architecture CQRS & Event Sourcing

## Description
Ce projet implémente un micro-service de gestion de comptes bancaires suivant strictement les patterns CQRS (Command Query Responsibility Segregation) et Event Sourcing avec Spring Boot et Axon Framework.

## Planning de Réalisation (Historique Git)

La réalisation est simulée sur deux sessions de travail : le **1er Décembre** (Architecture & Write Side) et le **8 Décembre** (Read Side & Intégration).

| Commit | Date | Heure | Module | Tâche réalisée |
|:------:|:----------:|:-----:|:--------------:|-----------------------------------------------------------------------------------|
| **1** | 01/12/2024 | 21:00 | **Setup** | Initialisation Spring Boot, dépendances Axon/Lombok/JPA et config JSON Jackson [4, 36] |
| **2** | 01/12/2024 | 21:42 | **Command** | Création de l'API Commandes (Controller, DTOs) et définition des Commandes Axon [10, 14] |
| **3** | 01/12/2024 | 22:30 | **Aggregate** | Implémentation de l'Agrégat `AccountAggregate` et cycle de vie (Creation/Activation) [21, 52] |
| **4** | 01/12/2024 | 23:15 | **Infra** | Configuration Docker Compose (Postgres) et persistance des événements [38, 41] |
| **5** | 08/12/2024 | 21:00 | **Logic** | Règles métier complètes (Crédit, Débit, Solde insuffisant) et gestion des exceptions [19, 62] |
| **6** | 08/12/2024 | 21:48 | **Query** | Création des Entités JPA (`Account`, `Operation`) et du service de Projection (`EventHandler`) [72, 76] |
| **7** | 08/12/2024 | 22:35 | **API Read** | Implémentation du `QueryGateway` et des endpoints de lecture (GetStatement) [90, 97] |
| **8** | 08/12/2024 | 23:20 | **Real-time** | Ajout du support SSE (Subscription Query) et intégration finale Axon Server [102, 112] |

## TODOs & Étapes Techniques

### Étape 1 : Initialisation & Write Side (01 Déc)
- [x] Configurer `pom.xml` (Java 21, Axon 4.10, Spring Web/Data).
- [x] Configurer `application.properties` pour utiliser Jackson (JSON) au lieu de XML pour les événements.
- [x] Implémenter `AccountCommandController` avec `CompletableFuture`.
- [x] Créer l'agrégat `AccountAggregate` avec `@AggregateIdentifier`.
- [x] Gérer le double événement à la création : `Created` PUIS `Activated`.

### Étape 2 : Read Side & Production Ready (08 Déc)
- [x] Monter l'infrastructure Docker (Postgres + Axon Server).
- [x] Créer le modèle de lecture JPA (`Account` & `AccountOperation`).
- [x] Implémenter `AccountEventHandler` pour synchroniser la base SQL avec l'Event Store.
- [x] Implémenter la logique de "Replay" (automatique au démarrage d'Axon).
- [x] Ajouter le endpoint "Watch" avec `Flux` et `QueryUpdateEmitter` pour le temps réel.

## Instructions de Lancement
1. Démarrer les conteneurs : `docker-compose up -d`
2. Lancer l'application : `mvn spring-boot:run`
3. Accéder à Swagger UI : `http://localhost:8088/swagger-ui.html`
4. Accéder à Axon Dashboard : `http://localhost:8024`

--------------------------------------------------------------------------------
3. Script de Recréation de l'Historique Git
Voici le script PowerShell à exécuter pour générer l'historique Git demandé (1er et 8 Décembre, début 21h, intervalle aléatoire ~45min).
Pré-requis : Avoir le projet complet terminé sur votre machine.
# ============================================================
# Script de Reset et Recréation des Commits - Bank-Account-Service
# Dates : 01 Décembre et 08 Décembre
# ============================================================

# 1. Nettoyage du repository existant
Write-Host "Suppression de l'historique Git existant..."
if (Test-Path .git) { Remove-Item -Recurse -Force .git }
git init

# 2. Configuration des dates (Format ISO 8601)
# Session 1 : 01 Décembre (Setup & Write Side)
$date1_1 = "2024-12-01T21:00:00"
$date1_2 = "2024-12-01T21:42:00" # +42 min
$date1_3 = "2024-12-01T22:30:00" # +48 min
$date1_4 = "2024-12-01T23:15:00" # +45 min

# Session 2 : 08 Décembre (Read Side & Infra)
$date2_1 = "2024-12-08T21:00:00"
$date2_2 = "2024-12-08T21:48:00" # +48 min
$date2_3 = "2024-12-08T22:35:00" # +47 min
$date2_4 = "2024-12-08T23:20:00" # +45 min

# 3. Exécution des Commits

# --- COMMIT 1 : Initialisation ---
$env:GIT_AUTHOR_DATE=$date1_1
$env:GIT_COMMITTER_DATE=$date1_1
git add pom.xml src/main/resources/application.properties src/main/java/com/example/demo/DemoAxonApplication.java
git commit -m "Init: Project setup with Spring Boot 3, Axon Framework & Jackson Config"

# --- COMMIT 2 : Command API ---
$env:GIT_AUTHOR_DATE=$date1_2
$env:GIT_COMMITTER_DATE=$date1_2
git add src/main/java/com/example/demo/commands/controllers/ src/main/java/com/example/demo/commands/dto/ src/main/java/com/example/demo/commands/commands/
git commit -m "Feat: Command Controller implementation and Command DTOs definition"

# --- COMMIT 3 : Aggregate & Events ---
$env:GIT_AUTHOR_DATE=$date1_3
$env:GIT_COMMITTER_DATE=$date1_3
git add src/main/java/com/example/demo/commands/aggregates/ src/main/java/com/example/demo/common/events/ src/main/java/com/example/demo/common/enums/
git commit -m "Core: Account Aggregate logic with Event Sourcing Handlers (Create/Activate)"

# --- COMMIT 4 : Infra & Docker ---
$env:GIT_AUTHOR_DATE=$date1_4
$env:GIT_COMMITTER_DATE=$date1_4
git add docker-compose.yml
git commit -m "Infra: PostgreSQL and Axon Server Docker configuration"

# --- COMMIT 5 : Business Logic & Exceptions ---
$env:GIT_AUTHOR_DATE=$date2_1
$env:GIT_COMMITTER_DATE=$date2_1
# On ajoute les modifs de logique métier (Crédit/Débit/Exceptions)
git add src/main/java/com/example/demo/commands/aggregates/AccountAggregate.java src/main/java/com/example/demo/commands/exceptions/
git commit -m "Logic: Full business rules implementation (Credit/Debit) and Exception Handling"

# --- COMMIT 6 : Query Side (Projections) ---
$env:GIT_AUTHOR_DATE=$date2_2
$env:GIT_COMMITTER_DATE=$date2_2
git add src/main/java/com/example/demo/query/entities/ src/main/java/com/example/demo/query/repositories/ src/main/java/com/example/demo/query/service/AccountEventHandler.java
git commit -m "Query: JPA Entities and Event Handler Projections implementation"

# --- COMMIT 7 : Query API ---
$env:GIT_AUTHOR_DATE=$date2_3
$env:GIT_COMMITTER_DATE=$date2_3
git add src/main/java/com/example/demo/query/controllers/ src/main/java/com/example/demo/query/dto/ src/main/java/com/example/demo/query/queries/ src/main/java/com/example/demo/query/service/AccountQueryHandler.java
git commit -m "Feat: Query API, DTOs and Query Handlers implementation"

# --- COMMIT 8 : Real-time & Final ---
$env:GIT_AUTHOR_DATE=$date2_4
$env:GIT_COMMITTER_DATE=$date2_4
# On ajoute tout le reste (fichiers modifiés pour le support Flux/SSE et activation Axon Server dans pom)
git add .
git commit -m "Final: Real-time subscription query support (SSE) and distibuted setup"

# 4. Push
Write-Host "Historique recréé. Push vers origin..."
use gr cli 
# git remote add origin https://github.com/elghazouanihamza1/bank-account-service.git
# git push -f origin main
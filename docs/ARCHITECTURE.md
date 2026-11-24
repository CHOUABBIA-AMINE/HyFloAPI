# HyFloAPI Architecture

**Status:** ADOPTED design, 2026-11-24. Structural baseline verified; business behavior remains planned.
**Scope:** Development-only functional test backend. One deployable Spring Boot application, one MySQL database.

## 1. Baseline and differences from HidraAPI

At [HyFloAPI e513487](https://github.com/CHOUABBIA-AMINE/HyFloAPI/commit/e5134870a11469a77eb5bf0e1d81fb2455ef3e74), the root entry point, Maven/CI, environment profiles and five smoke/integration test methods exist. The 351 package-info files establish the six sets and 25 module roots. [Build #5](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45383085910) passed on this SHA. No business entities, business endpoints, application authorization, or industrial features have been implemented by creating the packages. Default local Spring Security bootstrap and JWT library smoke test are not a completed Identity module.

| Concern | HidraAPI reference | HyFloAPI decision |
|---|---|---|
| Style | DDD/Hexagonal, explicit ports and adapters | Modular Layered; one JPA business model per entity |
| Package root | dz.sh.hidra | dz.sh.hyflo |
| Module placement | Flat 24 roots | 25 roots grouped in six singular sets |
| Java | 21 | 25 |
| Database | PostgreSQL / Flyway | MySQL / Hibernate; Flyway deferred |
| Identity | Local and enterprise integration capability | Local only; business authorization retained |
| Verification | Broad architecture, API, database, documentation and production gates | Existing single mandatory HyFloAPI Build; targeted functional scenarios |
| Production | Production infrastructure documents | No production deployment, HA, calibration or OT assumptions |

## 2. Repository and package organization

```text
HyFloAPI/
  AGENTS.md
  README.md
  docs/
    ARCHITECTURE.md
    MODULES.md
    ROADMAP.md
  .github/workflows/
    build.yml
    hyflo-source-structure-audit.yml
  .mvn/wrapper/
  scripts/
  pom.xml
  mvnw
  mvnw.cmd
  .env.example
  src/
    main/
      java/dz/sh/hyflo/
        HyFloApi.java
        bootstrap/
        kernel/
        platform/
        modules/
          foundation/
          coordination/
          engineering/
          operation/
          safety/
          intelligence/
      resources/
        application.properties
        application-dev.properties
    test/
      java/dz/sh/hyflo/
      resources/application-test.properties
```

Business roots and ownership are listed in [MODULES.md](MODULES.md). The functional sets are a classification and physical package grouping, not independent Maven modules or a mandatory ring dependency order. Retain the root application class to preserve default component and entity scanning.

## 3. Standard business module

Example: `dz.sh.hyflo.modules.engineering.topology`.

```text
topology/
  api/
    controller/PipelineController.java
    request/PipelineCreateRequest.java
    request/PipelineUpdateRequest.java
    response/PipelineResponse.java
    mapper/PipelineMapper.java
  application/
    service/PipelineService.java
  domain/
    model/Pipeline.java
  infrastructure/
    persistence/
      repository/PipelineRepository.java
```

The eight filenames illustrate a proposed basic CRUD slice, not existing source. Each package has package-info.java. No systematic enum, per-module exception or domain-service package. No duplicate PipelineEntity, port, persistence adapter or persistence mapper. Extra packages and public contracts require justified, registered scope.

| Layer | Owns | Allowed dependencies |
|---|---|---|
| API | HTTP, validation, request/response, mapping | Own application and model types for mapping, shared web facilities |
| Application | Use cases, authorization, transactions | Own model and repository, approved cross-module service contracts, shared platform facilities |
| Domain | JPA business model, relationships, invariants | Java, Jakarta Persistence/Validation, restrained Lombok, kernel; no service/repository/HTTP/platform implementation |
| Infrastructure | Spring Data JPA repositories and technical persistence | Own model, Spring Data JPA, platform technical configuration |
| Kernel | Generic exceptions, immutable values, paging, validation/events | Java standard library only |
| Platform | Configuration, security, persistence plumbing, exception/locale, observability, web/realtime/integration | Kernel and technical libraries; business access only through deliberately admitted contracts |

```text
HTTP -> Controller -> Application service -> own JPA repository -> MySQL
          |                 |
       API mapper        Domain model (business invariants + JPA)
```

This is a layered request flow, not strict inward hexagonal source dependency. The application-to-own-repository dependency is intentional. Domain does not call persistence. Controller returns an explicit DTO, not a managed entity. MapStruct is an API mapping tool, not a replacement for domain behavior.

## 4. Boundary and transaction rules

One business fact has one accountable module. Each repository is private to its owner. A consuming module may use a deliberately exported application service contract with stable IDs and explicit DTOs; it may not import private JPA models, repository interfaces or other modules' controllers. Record named owner, consumer, exposed operation, scope, and failure semantics when a contract is introduced; do not pre-create a matrix of unused interfaces. Model cross-module references using stable identifiers and verify owner eligibility, with database FKs where appropriate without cross-module JPA cascades. No circular module dependencies or generic data-access bypass. Platform is not an alternative owner.

Application services define transaction and authorization boundaries. Multi-owner operations coordinate through owner services with explicit consistency/rollback requirements. Use constraints (not null, unique, FK, checks where appropriate), lifecycle/retirement, optimistic versioning where needed, deterministic pagination, and tests against actual MySQL. Model engineering revisions separately from mutable drafts; published and referenced revisions cannot be silently rewritten. A workflow action records actor, decision, timestamp and target revision; an approval is not proof of physical execution.

## 5. Shared technical capabilities

| Package | Responsibility |
|---|---|
| bootstrap | Startup assembly; entry point remains at root |
| kernel/model, value, pagination, validation, event | Minimal framework-independent primitives only |
| kernel/exception | Reusable typed exception and stable code |
| platform/configuration, persistence | Spring configuration, datasource and transaction support |
| platform/security | Authenticated principal resolution, local security and enforcement |
| platform/exception, localization | Central error translation, locale resolution and fallback |
| platform/web, realtime | HTTP/CORS, WebSocket infrastructure when selected |
| platform/observability, integration | Logging/metrics and reusable technical connectivity |

Current directories are package skeletons. Implement only selected functionality, not all possible utilities.

## 6. API and localization design

Proposed business endpoints use context path `/hyflo/api` and versioned resource paths `/v1/...`, subject to task-specific contract approval. Current health and dev OpenAPI endpoints remain as configured. Explicit DTOs, validated types, units, bounded page size and stable sorting, meaningful HTTP status and generated OpenAPI. No generic CRUD exposure of every JPA entity. Define consistent error envelope based on Spring ProblemDetail with stable machine code, localized detail, status, request/correlation identifier where available and safe field validation information. Handle authentication/filter errors consistently as well as MVC exceptions. No stack traces, secrets, SQL or protected existence disclosures. This is a target, not a currently generated error contract.

Database-managed catalogs return stable IDs and codes plus translated labels. Shared language, geography, unit and common catalogs belong to Reference; business-specific types remain in their owner. Target locale resolution via Accept-Language with supported locales and documented default/fallback (initial languages proposed English, French, Arabic; exact fallback selected with reference task). Resolve labels server-side for all supported locales; no frontend catalog enum or translation table required. Untranslated labels use deterministic fallback, never change code semantics. Do not make hard transition guards configurable merely by adding reference rows. Technical/internal constants may be code-controlled; business catalog values are data.

## 7. Persistence and environments

| Profile | ddl-auto | Purpose |
|---|---|---|
| default | validate | No implicit schema modification |
| dev | update | Disposable local development schema |
| test | create-drop | Isolated Testcontainers MySQL schema only |

Java 25, Spring Boot 4.2.0, wrapper Maven 3.10.2, MapStruct 1.6.3, JJWT 0.13.0, SpringDoc 3.2.1. Dependency versions governed by actual POM/BOM. Database credentials and local bootstrap password externalized. No production profile, Flyway, migration directories or real-data import. Hibernate update cannot guarantee arbitrary schema transformations; never treat it as a production migration mechanism. Production preparation introduces versioned Flyway baseline, validate, release and rollback checks only under explicit authorization.

### Physical database identifiers

The owner requires the original numeric HyFlo naming convention (see [AGENTS.md](../AGENTS.md)). Preserve T_NN_NN_NN tables, F_00 primary keys, F_NN business and join columns, A_01 version, and explicit table-prefixed UK_NN/FK_NN constraint names. Physical case is preserved by Hibernate's standard naming strategy. Java properties retain descriptive names.

Historical source: [User before correction](https://github.com/CHOUABBIA-AMINE/HyFloAPI/blob/6763b5d12655fe02404af2a43d1807bb90d2e8fb/src/main/java/dz/sh/hyflo/modules/foundation/identity/domain/model/User.java); historical security namespace T_00_02_01–07 was Group, User, Role, Action, ResourceType, Resource, Permission. The historical User model assigned F_00 to ID, A_01 to version, F_01 username, F_02 email and F_08 employee. The owner's [latest model revision](https://github.com/CHOUABBIA-AMINE/HyFloAPI/commit/0c96402a6702467d18a0124d86b8b44d69ac4776) supersedes the previous column reservations: F_01 onward is consecutive across ordinary fields, scalar extra-module references and same-module entity associations, with no separate FK range or reserved gaps. FK and UK constraints each use independent consecutive numbering. Password remains in LocalCredential, not User. See AGENTS.md for mandatory declaration order, lower-camel-case entity association names and single-line JoinColumn annotations. The inventory below records the Identity mappings.

| Model | Table | Physical fields | Named constraints |
|---|---|---|---|
| User | T_00_02_02 | F_00 id; F_01 username; F_02 email; F_03 employeeId; F_04 profilePictureFileId; F_05 identityProvider; F_06 userStatus; A_01 version | UK_01 (F_05,F_01); UK_02 (F_02); FK_01 identityProvider; FK_02 userStatus |
| IdentityProvider | T_00_02_08 | F_00 id; F_01 code; F_02 displayName; F_03 enabled | UK_01 code |
| LocalCredential | T_00_02_09 | F_00 id; F_01 passwordHash; F_02 passwordChangedAt; F_03 user | UK_01 user; FK_01 user |
| UserStatus | T_00_02_10 | F_00 id; F_01 code; F_02 displayName | UK_01 code |

The four pre-existing tables retain their `T_00_02` names. The new allocations use the proposed Foundation / Identity prefix `T_10_01`; no older identifier has been reassigned. Existing column layouts changed for the disposable development schema. A previously populated schema requires a separately reviewed compatibility change. The `employeeId` and `profilePictureFileId` columns are scalar owner references. They do not create physical cross-module foreign keys. `IdentityProvider` remains LOCAL only, and `UserStatus` uses persisted rows with stable codes.

| Model | Table | Purpose and constraints |
|---|---|---|
| Group | T_10_01_01 | Security group. Immutable unique code, display name and retirement. |
| Role | T_10_01_03 | Controlled role catalog. Immutable unique code, display name and retirement. |
| Action | T_10_01_04 | Immutable unique operation code and label. |
| ResourceType | T_10_01_05 | Immutable unique resource-type code and label. |
| Permission | T_10_01_07 | Unique code and unique action/resource-type pair. |
| GroupMembership | T_10_01_11 | User and group links, effective interval and revocation attribution. |
| RolePermission | T_10_01_12 | Unique role/permission link. Effective scope belongs to role grants. |
| GroupRoleGrant | T_10_01_13 | Group and role links, embedded scope, interval, reason, and actor attribution. |
| LoginSession | T_10_01_15 | User-linked session with expiry and revocation. |
| AuthenticationEvent | T_10_01_16 | Provider, event code, timestamp, correlation and optional user for unresolved login failures. |
| UserRoleGrant | T_10_01_17 | Direct user and role links, embedded scope, interval, reason, and actor attribution. |

`AuthorizationScope` is embedded in each role grant as F_07–F_10. It carries the station-update mode, company ID, optional pipeline-system ID, and optional selected-region ID. Assigned-region, selected-region, and entire-pipeline modes are stable policy command codes. Each selected-region grant names one region. Administration may issue multiple grants for multiple selected regions. Grant actor IDs record attribution as scalars and must be assigned by the future authorized application service. The station owning module must resolve the target company, pipeline system and region before an update can be authorized. These models do not implement login, permission checks, administrative APIs or runtime catalog seeding.

## 8. Security and operational trust

Local accounts, password hashes, grants and effective permissions belong to Identity. Spring Security and token verification belong to platform.security. Bootstrap default user is temporary; target persistent local authentication must replace it without bypassing authorization. Enforce company/operational scope before disclosing data or existence, counts, export/history. Use authenticated actor attribution and least privilege, fail closed, independent decisions where required. External systems, OT, live SCADA, enterprise SSO and real data are not authorized. Audit evidence records who did what and when; Workflow owns transitions and approvals.

Operational chain target: versioned engineering inputs -> telemetry raw/qualified evidence -> monitoring evaluation -> alarm/leak/incident or analytics -> human-reviewed recommendation and decision -> observed outcome. Gas-first synthetic GZ2 scenario; explicit units, quality, timestamps, source and revision IDs. Scenario preparation is separate from numerical execution. Future internal steady/transient engine is deferred; NeqSim/DWSIM/pandapipes are potential integrations only. No automatic field actuation or safety qualification claim.

## 9. Testing and lifecycle

Use existing unit (Surefire) and integration (Failsafe) tests; MySQL 8.4 container for real DB behavior, no skip for unavailable Docker, JaCoCo evidence and CycloneDX SBOM. Build on every main push via `.github/workflows/build.yml`. Tests mirror actual source packages as functionality arrives. Add architecture tests with selected task: kernel isolation, no controller repository dependency, same-module repository access only, explicit public cross-module contracts, no JPA exposure and no forbidden packages. ArchUnit not currently declared; add in that task. No arbitrary coverage percentage on skeleton. OWASP scan is optional and separate. Evidence is exact-SHA test reports, dependency tree, generated artifacts and CI; source-only inspection does not prove runtime. See [ROADMAP.md](ROADMAP.md) for active tasks.

## 10. Reference basis

Adapted from HidraAPI at 007d2d246b244deb4afe9258ab66d19dc7ff2d6c: [architecture overview](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/architecture/ARCHITECTURE_OVERVIEW.md), [module boundaries](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/architecture/MODULE_BOUNDARIES.md), [bounded-context map](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/architecture/BOUNDED_CONTEXT_MAP.md), [cross-module contracts](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/architecture/CROSS_MODULE_CONTRACTS.md), [domain ownership](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/domain/DOMAIN_OWNERSHIP.md), [API conventions](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/api/API_CONVENTIONS.md), [error model](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/api/ERROR_MODEL.md), [security](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/security/SECURITY_ARCHITECTURE.md), [test strategy](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/testing/TEST_STRATEGY.md) and [product vision](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/vision/HIDRA_MASTER_PRODUCT_VISION.md). Hidra's runtime and production claims are not transferred to HyFloAPI.

# HyFloAPI Architecture

**Status:** ADOPTED design, 2026-11-24. Structural baseline verified; business behavior remains planned.
**Scope:** Development-only functional test backend. One deployable Spring Boot application, one MySQL database.

## 1. Baseline and differences from HidraAPI

At [HyFloAPI e513487](https://github.com/CHOUABBIA-AMINE/HyFloAPI/commit/e5134870a11469a77eb5bf0e1d81fb2455ef3e74), the root entry point, Maven/CI, environment profiles and five smoke/integration test methods exist. The 351 package-info files establish the six sets and 25 module roots. [Build #5](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45383085910) passed on this SHA. No business entities, business endpoints, application authorization, or industrial features have been implemented by creating the packages. Default local Spring Security bootstrap and JWT library smoke test are not a completed Identity module.

| Concern | HidraAPI reference | HyFloAPI decision |
|---|---|---|
| Style | DDD/Hexagonal, explicit ports and adapters | Modular Layered; one JPA business model per entity |
| Package root | dz.sh.hidra | dz.sh.hyflo |
| Module placement | Flat 24 roots | 26 roots grouped in six singular sets |
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

Foundation repository coverage (FDN-REPO-001): each of the 37 JPA entities has a typed Spring Data repository interface in its owning module (Identity 17, Organization 12, Reference 7, Infra 1). The three existing Organization repositories retain their selected operations; the other 34 extend `Repository<Entity, Long>` with no methods. AuthorizationScope is an embeddable and has no standalone repository. The interfaces provide Spring Data discovery and type binding only; they do not expose generic CRUD, REST endpoints or grant permission. Additional operations must be selected with their owning use case and constraints. The existing real-MySQL integration context checks repository registration, entity and identifier types, and module-local interfaces.

Foundation application service coverage (FDN-SVC-001): 37 entity-specific Spring application services, one for every existing JPA entity, in the owning module's application.service package. EmployeeAssignmentService retains the implemented assignment and transfer operations; 36 new service skeletons contain only their corresponding private final owner repository and explicit constructor injection. OrganizationUnitScopeService and AuthorizationScopeService remain separate internal scope services, for 39 total Foundation service classes. The new OrganizationUnitService does not replace or duplicate scope validation. No generic CRUD, new service contract, endpoint or business operation is introduced; structural coverage does not establish a complete workflow. Real-MySQL integration context verifies unique bean discovery and constructor/repository binding for the new classes.

IDN-01 provides the explicit one-time trusted Identity initialization command. Run only against an approved disposable development schema after Organization has provisioned an active, date-effective COMPANY-classified unit. Use `java -jar <built-jar> --identity-bootstrap` as the sole argument with normal external database configuration and environment variables HYFLO_BOOTSTRAP_COMPANY_ID, HYFLO_BOOTSTRAP_USERNAME, HYFLO_BOOTSTRAP_EMAIL, HYFLO_BOOTSTRAP_PASSWORD and HYFLO_BOOTSTRAP_OPERATOR. Never pass secrets on the command line. Normal startup never seeds Identity or creates authority; the bootstrap branch uses a non-web context and exits. The password is encoded with the explicit platform IdentityPasswordEncoder, internally delegating to Spring Security's bcrypt encoding (minimum 15 characters, maximum 72 UTF-8 bytes). The typed wrapper is not a globally discoverable PasswordEncoder bean, so the temporary in-memory HTTP account retains its existing authentication configuration without an insecure fallback. The persistent Identity workflow explicitly selects its encoder; runtime local authentication will be wired in its own selected task. The unique LOCAL provider insertion and independent-root emptiness checks guard the single atomic transaction. The reserved disabled SERVICE user hyflo-bootstrap has no credential or grant and is the attribution actor of the one initial HUMAN administrator grant; the trusted operator reference is recorded in the grant reason, not claimed to be an authenticated human identity. The explicit initial permission manifest in ROADMAP.md reserves 15 definitions for the Identity pilot; GLOBAL is limited to the validated company. Existing Identity data or repeated invocation is rejected without restoring authority. No authentication endpoint, runtime authorization or production deployment is delivered. Organization eligibility uses the admitted owner contract; the old classification-only method retains its semantics. No schema changes.

IDN-02 adds persistent local authentication and the first two Identity HTTP contracts: POST /v1/auth/login and GET /v1/me under /hyflo/api. The former temporary Spring Security in-memory account is removed; normal servlet startup requires external HYFLO_JWT_KEY (base64, at least 32 random bytes). The non-web IDN-01 command remains independent of signing configuration. Login normalizes username, bounds input, applies a bounded single-process fixed-window global/direct-peer/username limiter, locks the user row, verifies the existing bcrypt credential and checks account/provider eligibility. A single application transaction persists LoginSession, SHA-256 digest of a cryptographically random opaque refresh secret and LOGIN_SUCCEEDED before returning tokens; rejected credentials with a persisted LOCAL provider record LOGIN_FAILED in a committed transaction. Missing initialization or failed persistence denies authentication. The platform issues HS256 access JWTs with strict issuer/audience hyflo-api, access type, subject/session IDs, jti and timestamps; defaults access 600s and absolute session 28800s, configurable and bounded. Every protected request validates signature/claims and the current owner-resolved account/session; no cached permissions or token-based privilege claims. At IDN-02, only login POST and health GET were public; own identity and enabled development OpenAPI/Swagger GET required bearer authentication. Subsequent additions are listed below. No HTTP session, Basic, form login, remember-me or global CORS opening; refresh/logout were deferred at IDN-02. The technical limiter is not distributed protection. Explicit DTOs/mapping, no-store token responses and safe authentication errors are implemented; English fallback pending localization. No schema or dependency changes. Effective scoped authorization remains IDN-03; protected engineering and administration are not available merely because login succeeds.

IDN-03 adds direct company-scoped read authorization. The server obtains the authenticated CurrentIdentity, verifies it against the SecurityContext and revalidates its live session in the read transaction. Only exact GLOBAL grants (null target reference) for the requested company and current user, with half-open validity/revocation and enabled roles, contribute actual RolePermission associations. Permission codes must exactly equal the resource/action pair; literal code equality and deduplication occur in Java, independent of database collation. For a protected operation, Identity checks the exact required permission before invoking Organization's company eligibility contract or reading protected data. Standalone decisions and catalog reads use repeatable-read read-only transactions; the catalog fetch shares the authorization transaction. GET /v1/me/permissions returns own sorted direct codes for one eligible company, not a promise of implemented operations; GET /v1/identity/user-statuses requires identity-catalog.read and returns the existing status catalog in code order. Both are bearer-protected with an explicit application gate, safe 400/401/403/503 and no-store. All other unregistered routes remain denied. No group inheritance (IDN-10), non-GLOBAL resource authority, cross-user account administration, new seeds, schema changes or durable authorization decision audit. No cached authorization. Future writes require their own transaction and locking contract; a read snapshot does not establish mutation atomicity. GLOBAL is not an engineering-resource ownership proof.

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

The inventory starts with entities that have no internal foreign-key dependencies. Each subsequent level can depend on entities from earlier levels. The dependencies shown are distinct Identity entities referenced by the model.

| Level | Model | Table | Internal dependencies | Physical fields, purpose and constraints |
|---|---|---|---|---|
| 1 | IdentityProvider | T_10_01_01 | None | F_00 id; F_01 code; F_02 displayName; F_03 enabled. Constraints — UK_01 code. |
| 1 | UserStatus | T_10_01_02 | None | F_00 id; F_01 code; F_02 displayName. Constraints — UK_01 code. |
| 1 | Group | T_10_01_03 | None | Security group. Immutable unique code, display name and retirement. |
| 1 | Role | T_10_01_04 | None | Controlled role catalog. Immutable unique code, display name and retirement. |
| 1 | Action | T_10_01_05 | None | Immutable unique operation code and label. |
| 1 | ResourceType | T_10_01_06 | None | Immutable unique resource-type code and label. |
| 1 | UserType | T_10_01_07 | None | F_00 id; F_01 code; F_02 nameAr; F_03 nameFr; F_04 nameEn. UK_01 code. HUMAN and SERVICE. |
| 2 | User | T_10_01_08 | IdentityProvider, UserStatus, UserType | F_00 id; F_01 username; F_02 email; F_03 employeeId; F_04 profilePictureFileId; F_05 owningOrganizationUnitId; F_06 identityProvider; F_07 userStatus; F_08 userType; A_01 version. Constraints — UK_01 (F_06,F_01); UK_02 (F_02); FK_01 identityProvider; FK_02 userStatus; FK_03 userType. |
| 2 | Permission | T_10_01_09 | Action, ResourceType | Unique code and unique action/resource-type pair. |
| 3 | LocalCredential | T_10_01_10 | User | F_00 id; F_01 passwordHash; F_02 passwordChangedAt; F_03 user. Constraints — UK_01 user; FK_01 user. |
| 3 | LoginSession | T_10_01_11 | User | User-linked session with expiry and revocation. |
| 3 | AuthenticationEvent | T_10_01_12 | IdentityProvider, User | Provider, event code, timestamp, correlation and optional user for unresolved login failures. |
| 3 | GroupMembership | T_10_01_13 | User, Group | Effective interval, User actor links at F_04 (grant) and F_05 (revocation), member User at F_06 and Group at F_07. |
| 3 | RolePermission | T_10_01_14 | Role, Permission | Unique role/permission link. Effective scope belongs to role grants. |
| 3 | UserRoleGrant | T_10_01_15 | User, Role | Direct user and role links, embedded scope, interval, reason, and actor attribution. |
| 3 | GroupRoleGrant | T_10_01_16 | Group, Role, User | Group and role links, embedded scope, interval, reason, and actor attribution. |
| 4 | RefreshToken | T_10_01_17 | LoginSession | Unique SHA-256 digest (F_01), issuance (F_02), optional consumption (F_03), and required LoginSession link (F_04). |
| 3 | UserLifecycleEvent | T_10_01_18 | User | F_00 id; F_01 eventCode; F_02 occurredAt; F_03 correlationId; F_04 reason; F_05 previousStatusCode; F_06 resultingStatusCode; F_07 owningOrganizationUnitId; F_08 user (FK_01); F_09 performedBy (FK_02). Append-only lifecycle evidence. |

All Identity tables use `T_10_01_NN`, where `10` is Foundation, `01` is Identity, and `NN` is the one-based position in the inventory above. This allocation supersedes the previous Identity table names for fresh development schemas. Several table codes previously identified different entities. An existing schema with Identity tables requires a separately reviewed migration before reuse. The `employeeId`, `profilePictureFileId` and required immutable `owningOrganizationUnitId` columns are scalar owner references. The owning organization unit is a COMPANY-classified unit checked by Organization during bootstrap and account registration; ownership is distinct from operational grants. They do not create physical cross-module foreign keys. `IdentityProvider` remains LOCAL only, and `UserStatus` uses persisted rows with stable codes. UserType is a required immutable association to a stable-code HUMAN or SERVICE classification row; classification alone grants no rights. No preferredLanguageId or catalog seed rows are introduced.

A login session can retain multiple refresh token digests as tokens rotate. The model records consumption once and uses optimistic locking to detect competing updates to a token row. Session expiry and revocation govern token usability. IDN-04 implements atomic rotation and session revocation on reuse through the Identity application service. Access JWTs require no token table.

IDN-05 implements company-owned account administration. New accounts are LOCAL/PENDING, with explicit HUMAN/SERVICE type and no implicit credential, employee link or grant. Existing bootstrap attribution and first administrator have the validated owning company. User ownership is immutable in this scope, not permission to operate in the company. The User table renumbering and new lifecycle evidence table require a fresh disposable schema (never Hibernate-update an earlier physical layout). The seven HTTP operations are GET collection/detail and POST collection/activate/suspend/reactivate/disable at /v1/identity/users with a companyId selector. Collection is bounded to 100 rows, afterId keyset; missing/foreign targets deny identically, and reserved bootstrap is excluded. The exact user.read or operation permission is required. DTOs contain only id/username/email/companyId/type/status; no credentials, grants or event history. Creation conflicts preserve existing global DB uniqueness with generic 409. Mutations deny self-administration, require a reason and server-generated correlation, and record immutable before/after lifecycle evidence. No credential provisioning, reset, owner transfer or employee linking is implemented.

For protected writes, UserAdministrationService uses READ_COMMITTED and a separate locked authority path, not IDN-03 read permission as a write proof. Lock actor User, actor LoginSession, LOCAL provider, direct company grant candidates/role rows, role-permission links and their joined permission catalog rows, then Organization company/type/status; check exact live permission and effective company eligibility. Query target by ID and owner only after gate; lock target User and recheck live authorization after lock acquisition and before evidence append. Lock target unrevoked LoginSessions and revoke on suspend/disable, avoiding resurrection on reactivate. All mutations and UserLifecycleEvent append commit atomically. Activation requires existing local credential; PENDING activation and SUSPENDED reactivation are separate operations, DISABLED is terminal. Lock contention/deadlock fails closed; no automatic retry or claim of global serializability with future mutators. Future grant/role/company writers must preserve locking contracts. General Audit remains its own module; this is Identity-owned business transition evidence, not an audit export or production readiness claim.

IDN-06 admits POST /v1/identity/users/{userId}/credential/initialize and /credential/reset with companyId selector and explicit password/reason input. It uses the same locked company-owned write gate and exact credential.initialize or credential.reset permission, not user.read or account activation. Initialization is one-time for PENDING without a credential; reset requires an existing credential in PENDING, ACTIVE or SUSPENDED, never DISABLED or self/reserved/foreign accounts. Neither operation changes status or grants. The existing bootstrap policy (minimum 15 code points, maximum 72 UTF-8 bytes, nonblank, no normalization) and tagged bcrypt encoder are reused; same current supported password is rejected on reset, no password history. Reset updates the same T_10_01_10 row and revokes all target sessions under the User lock. CREDENTIAL_INITIALIZED / CREDENTIAL_RESET evidence in T_10_01_18 captures unchanged status, owner, actor, reason, time and correlation atomically with the mutation, no secret or hash. Both operations return 204/no-store. Credential input is redacted in diagnostics; no credential read, self-service change, recovery, forced first-login rotation, email delivery or secret delivery mechanism. This is a development administrator-supplied password workflow, not production onboarding. No physical mapping change.

IDN-07 admits POST /v1/identity/users/{userId}/sessions/revoke with companyId and reason, using the exact login-session.revoke permission and IDN-05 locked write authority. After authorization and target User lock, lock its unrevoked LoginSession rows and revoke those live at server decision time. Preserve expired, future and previously revoked rows. A successful account-wide operation records one SESSIONS_REVOKED event with unchanged account status and actor, target, owning company, reason, correlation and time, atomically with session changes. No-live-session conflict is 409; success is 204/no-store with no identifiers or counts. Self, reserved bootstrap, missing and foreign targets deny uniformly. No per-session read/list/selection, new permission or schema, and no change to credential, account status, grants or own-session logout. A new login remains possible for an eligible account.

IDN-08 admits company-scoped GET/POST /v1/identity/users/{userId}/role-grants and POST /v1/identity/users/{userId}/role-grants/{grantId}/revoke with explicit companyId and exact user-role-grant.read/assign/revoke respectively. GLOBAL only, bounded 100-row keyset read (including history) without account discovery; missing recipient and no selected-company grant return empty. Assignment requires an existing enabled exact same role held in a currently effective direct GLOBAL grant that individually covers the entire requested finite interval. Start is server time; expiry is explicit ISO Instant at microsecond precision. Recipient account is locked, self/disabled/reserved targets denied, actual permission links validated and intervals cannot overlap; revoked history remains. Administrative delegation is same-owner-company only. Foreign-owned recipients may receive only roles comprising the explicit catalog-read allowlist identity-catalog.read, role.read, permission.read; unclassified permissions fail closed for foreign delegation. No new roles, resource-scoped or group delegation, scope or role editing. Revoke only exact selected company/recipient/grant, no self or bootstrap-issued grant, cancel future or current unrevoked grant, preserve original assignment reason and actors. Revocation stores actor and time, no separate reason/correlation. No account status/credential/session change or cascading revoke; no full last-admin guarantee. Lock actor authority and target User before grant mutation, revalidate after locks, use existing row and permission locking contracts; future role edits must preserve the foreign-recipient invariant. No schema changes.

`AuthorizationScope` is embedded in both role grant tables. F_04 is the grant reason. F_05 is the scope type, F_06 is the required positive organizationUnitId (a COMPANY-classified OrganizationUnit), and F_07 is the optional target ID. F_08 is the user or group foreign key and F_09 is the role foreign key. The scope types are stable codes carried by the class.

| Scope type | Target ID | Authorization boundary |
|---|---|---|
| GLOBAL | Absent | Company-wide access for the permissions included in the grant. |
| REGION | Region ID | Resources covered by the named region within the company. |
| PIPELINE | Pipeline ID | The named pipeline and its associated resources within the company, as resolved by their owners. |
| INFRASTRUCTURE | Infrastructure ID | The named infrastructure within the company, such as a station, terminal, or valve. |

A company is an OrganizationUnit whose associated OrganizationUnitType has the stable exact code COMPANY; no separate Company entity or OrganizationUnitKind. OrganizationUnitScopeService.requireCompanyUnit(long) is the internal Organization-owned contract consumed by Identity's AuthorizationScopeService.validateCompanyBoundary(AuthorizationScope). It rejects missing and non-company units identically; localized names and parent status do not determine classification. The Identity application service validates the company boundary via this contract, without importing an Organization JPA model or repository. Pure domain factories validate shape only; the owner check is a type check, not effective-date/status eligibility, full authorization, universal grant-write interception or cross-module physical FK enforcement. No company type rows are seeded. Scope target IDs are scalar references. The authorized application service must verify the target type and company ownership through the owning module. A request that cannot be matched to the grant's company, permission and scope must be denied. Both role grant tables reference Identity User (T_10_01_08) through F_10 (grantedBy, required and immutable) and F_11 (revokedBy, set together with revokedAt). The field names distinguish the two audit roles from the recipient association. The application service must derive each actor from the authenticated context. This column layout is for disposable development schemas. These models store scope and grant data. Request-time authorization, login, administrative APIs and catalog seeding require application wiring.

### Organization persistence

Foundation Organization uses the `T_10_02_NN` prefix. Entity codes follow dependency order.

| Model | Table | Internal dependencies |
|---|---|---|
| OrganizationUnitType | T_10_02_01 | None |
| EmployeeType | T_10_02_02 | None |
| EmployeeStatus | T_10_02_03 | None |
| PositionLevel | T_10_02_04 | None |
| PositionStatus | T_10_02_05 | None |
| OrganizationUnitStatus | T_10_02_06 | None |
| AssignmentType | T_10_02_07 | None |
| AssignmentStatus | T_10_02_08 | None |
| Employee | T_10_02_09 | EmployeeType, EmployeeStatus |
| Position | T_10_02_10 | PositionLevel, PositionStatus |
| OrganizationUnit | T_10_02_11 | OrganizationUnitType, optional parent OrganizationUnit, OrganizationUnitStatus |
| EmployeeAssignment | T_10_02_12 | OrganizationUnit, Position, Employee, AssignmentType, AssignmentStatus |

Employee includes optional birth date, Arabic/Latin birthplace and address, email, phone, birth/address locality IDs, nationality country ID and employee picture file ID. Geography and File are foreign-owner scalar references: no cross-module JPA association, physical FK or owner validation is claimed by this task. User email and user profile picture remain independent account fields. OrganizationUnitType retains its table and stable code and adds optional Arabic/French/English descriptions and an active flag (default active, retirement retains historical references). No OrganizationUnitKind or kind field. The existing self-association keeps the mandatory entity-name field convention; getParentUnit exposes its meaning. Position descriptions are optional trilingual text. Organization units have validity dates and a status catalog. Status/type catalogs have finite stable codes and localized labels; no seed values are provisioned. Assignment status is recorded lifecycle metadata; the date lookup remains historical and does not infer authorization. Only ACTIVE creation and ENDED transfer are supported; suspension, cancellation and concurrent secondary assignments require later selection. Renumbering is for disposable fresh schemas only.

The baseline allows one effective assignment per employee on a given date. An assignment starts on an inclusive date and ends on an exclusive date. When employment has an end date, it bounds the assignment. Positions can be reused across units. A transfer ends the previous assignment and creates the replacement in one transaction. The assignment service locks the employee before checking periods or writing changes. Its date lookup returns the historical unit and position in effect on the requested date.

The hierarchy and directory are persisted through JPA. Assignment, transfer and date lookup are internal application operations. Organization REST endpoints and company-bound appointment rules are outside this baseline.

### Reference geography and shared files

| Module | Model | Table | Internal dependencies |
|---|---|---|---|
| Reference | Country | T_10_03_01 | None |
| Reference | Language | T_10_03_02 | None |
| Reference | MeasurementUnit | T_10_03_03 | None |
| Reference | State | T_10_03_04 | Country |
| Reference | District | T_10_03_05 | State |
| Reference | Locality | T_10_03_06 | District |
| Reference | Location | T_10_03_07 | Optional Locality |
| Infra | File | T_10_00_01 | None |

Reference stores Arabic, French and English names. Country codes are unique across the catalog. State, district and locality codes are unique within their parent. The required parent joins preserve the full geographic hierarchy. Country codes are normalized to three uppercase ASCII letters (shape only, not ISO membership verification). All four geographic records retain an active flag; Locality also stores an optional postal code. Retirement does not delete historical geography. Language (F_00 id, F_01 lowercase two-letter code, F_02–04 trilingual names, F_05 nativeName, F_06 rightToLeft, F_07 active) and MeasurementUnit (F_00 id, F_01 uppercase stable code, F_02–04 trilingual names, F_05 symbol, F_06 active) use unique immutable codes and default active/retirement. Language code shape is not verified ISO membership; unit symbols preserve case and are not unique identifiers. No conversions, quantity catalog, reference-condition inference or translation completeness is implied. Location (F_00 id, F_01–03 trilingual names, F_04 latitude, F_05 longitude, F_06 elevation, F_07 optional locality FK_01) stores finite WGS84 decimal-degree point coordinates and optional metres above mean sea level, not surveyed engineering qualification or route geometry. Identity and Reference renumbering in FDN-DATA-001 is fresh disposable-schema only; do not reuse existing physical schemas through Hibernate update. Company and AuthorizationScope remain unchanged.

Infra stores a unique opaque storage key, original filename, media type, size in bytes and creation time for each file. Other modules can refer to this record by ID. The persistence model is in place for file metadata. Content storage, uploads and downloads require application wiring.

## 8. Security and operational trust

Local accounts, password hashes, grants and effective permissions belong to Identity. Spring Security and token verification belong to platform.security. Bootstrap default user is temporary; target persistent local authentication must replace it without bypassing authorization. Enforce company/operational scope before disclosing data or existence, counts, export/history. Use authenticated actor attribution and least privilege, fail closed, independent decisions where required. External systems, OT, live SCADA, enterprise SSO and real data are not authorized. Audit evidence records who did what and when; Workflow owns transitions and approvals.

Operational chain target: versioned engineering inputs -> telemetry raw/qualified evidence -> monitoring evaluation -> alarm/leak/incident or analytics -> human-reviewed recommendation and decision -> observed outcome. Gas-first synthetic GZ2 scenario; explicit units, quality, timestamps, source and revision IDs. Scenario preparation is separate from numerical execution. Future internal steady/transient engine is deferred; NeqSim/DWSIM/pandapipes are potential integrations only. No automatic field actuation or safety qualification claim.

## 9. Testing and lifecycle

Use existing unit (Surefire) and integration (Failsafe) tests; MySQL 8.4 container for real DB behavior, no skip for unavailable Docker, JaCoCo evidence and CycloneDX SBOM. Build on every main push via `.github/workflows/build.yml`. Tests mirror actual source packages as functionality arrives. Add architecture tests with selected task: kernel isolation, no controller repository dependency, same-module repository access only, explicit public cross-module contracts, no JPA exposure and no forbidden packages. ArchUnit not currently declared; add in that task. No arbitrary coverage percentage on skeleton. OWASP scan is optional and separate. Evidence is exact-SHA test reports, dependency tree, generated artifacts and CI; source-only inspection does not prove runtime. See [ROADMAP.md](ROADMAP.md) for active tasks.

## 10. Reference basis

Adapted from HidraAPI at 007d2d246b244deb4afe9258ab66d19dc7ff2d6c: [architecture overview](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/architecture/ARCHITECTURE_OVERVIEW.md), [module boundaries](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/architecture/MODULE_BOUNDARIES.md), [bounded-context map](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/architecture/BOUNDED_CONTEXT_MAP.md), [cross-module contracts](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/architecture/CROSS_MODULE_CONTRACTS.md), [domain ownership](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/domain/DOMAIN_OWNERSHIP.md), [API conventions](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/api/API_CONVENTIONS.md), [error model](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/api/ERROR_MODEL.md), [security](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/security/SECURITY_ARCHITECTURE.md), [test strategy](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/testing/TEST_STRATEGY.md) and [product vision](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/vision/HIDRA_MASTER_PRODUCT_VISION.md). Hidra's runtime and production claims are not transferred to HyFloAPI.

IDN-04 adds POST /v1/auth/refresh (opaque refresh secret, no access bearer required) and bearer-authenticated POST /v1/auth/logout (current session only). Rotation stores only a successor digest, consumes the old token, retains the original absolute expiry and appends REFRESH_SUCCEEDED atomically; a consumed token presented against an eligible active session revokes that session and appends REFRESH_REPLAY before generic 401. User/session/token locks serialize refresh; logout uses the same user/session order and LOGOUT evidence. A racing duplicate can invalidate the first success; clients must serialize refresh. Existing JWTs remain valid until expiry or session revocation. Login and refresh share bounded global/peer rate limits; no token keys are retained by the limiter. No new physical mappings, permissions, cookie/session infrastructure, external providers or admin revocation. CI acceptance remains in ROADMAP.md.


IDN-09 adds read-only shared role and permission definition catalogs: GET /v1/identity/roles, /v1/identity/permissions, and /v1/identity/roles/{roleId}/permissions. Explicit eligible companyId and live direct GLOBAL authority are checked inside a read-only REPEATABLE_READ transaction before fetching definitions: role.read, permission.read, or both for bundle consultation. Fixed maximum 100 rows by ascending ID with afterId (default zero), no totals. Disabled roles and structurally malformed stored permissions remain visible only as definition metadata; validDefinition reflects the exact resource.action rule, not an implemented route or actual entitlement. Missing role bundle is empty for an authorized reader. Explicit safe DTOs, no-store, existing error conventions and default-deny for unregistered writes. No role/permission mutations, new definitions, grants, account data, schema, localization or resource authority.


IDN-10 (original W18/IDN-06 residual) admits bearer-only POST /v1/auth/password/change with currentPassword and newPassword, no target selector and no token response. Reuses current LOCAL password policy, tagged bcrypt, and login global/peer/authenticated-username limiter; exact SecurityContext principal and active owned session revalidated inside READ_COMMITTED after User/session locks and before mutation. Update the same credential row and revoke every unrevoked session of the authenticated account (including current), retaining timestamp ordering and requiring fresh login. PASSWORD_CHANGED/PASSWORD_CHANGE_FAILED in existing AuthenticationEvent provide user/provider/time/correlation evidence without secret material. Success commits credential, sessions and event atomically; wrong/same password generic 401 with failure evidence, invalid input 400, throttled 429, persistence failure 503. Existing administration/role/scope unchanged. No new tables, permission, forced rotation, password history, external identity or general audit; no in-flight read cancellation or distributed rate limiting claim.

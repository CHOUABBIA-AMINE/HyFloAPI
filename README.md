# HyFloAPI

Development-only functional testing foundation for hydrocarbon transportation. Existing architecture, Java 25, Spring Boot 4.2.0 and MySQL are retained. Foundation Identity and Organization data and the registered persistent LOCAL authentication/authorization workflows are implemented; broader operational capabilities remain roadmap-controlled. No external identity provider, AI runtime, or deployment is enabled.

## Build

Install JDK 25 and a Docker-compatible runtime for integration tests. The checked-in Maven Wrapper pins Maven 3.10.2 and its distribution checksum.

Linux/macOS:

```sh
./mvnw --version
./mvnw test
./mvnw clean verify
```

Windows: use `mvnw.cmd` with the same arguments.

Surefire runs unit tests and Failsafe runs the MySQL integration tests. Both reject an empty suite. The integration suite starts the real MySQL 8.4 LTS container, starts the application, generates a synthetic schema from a test-only JPA entity, verifies a persisted record through JPA and JDBC, and checks health, persistent local authentication, company-scoped authorization and first-use setup. The test profile uses Hibernate create-drop exclusively against the disposable container. Docker unavailability is a failure, not a silent skip. No real operating data or credentials are needed. MySQL's 8.4 image tag follows the LTS line; exact pulled image digest is runtime-dependent.

## Local development

Use an empty disposable MySQL database and a dedicated account; never use root or production data. Set the environment variables listed in `.env.example` in your shell (Spring Boot does not load this file automatically). Set the external JWT signing key (base64 of at least 32 random bytes). For first-use setup, also supply a separate random base64 installation secret of at least 32 bytes and a bounded operator reference. Start normally with:

```sh
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

The default base path is `/hyflo/api`. Development OpenAPI is available at `/hyflo/api/api-docs`, protected by persistent bearer authentication. Health is at `/hyflo/api/actuator/health`. The dev profile enables SQL logging. Do not expose this development application to an untrusted network.

### First-use setup (IDN-01-HTTP)

Use the normal running application, not a separate command or SQL script. On a fresh disposable schema:

1. GET `/hyflo/api/v1/setup/status` returns only `{"available":true}` when installation authority is configured and the required roots are empty.
2. POST JSON to `/hyflo/api/v1/setup/initialize` with `X-Hyflo-Setup-Secret` containing the configured installation secret. Supply companyCode, one or more of companyNameAr/companyNameFr/companyNameEn, username, email and password (at least 15 code points and at most 72 UTF-8 bytes). Do not put secrets in a URL or log requests. A successful request returns 204 and atomically initializes the first company and its Identity administrator. No default password or automatic login.
3. Use `POST /hyflo/api/v1/auth/login` for normal authentication. Setup is unavailable while initialized or partially populated roots remain. Remove the installation secret from deployment configuration after successful setup. Missing/invalid setup configuration disables setup without disabling ordinary authentication.

Use restricted installer access and trusted HTTPS outside local development. No network perimeter, production deployment or frontend setup wizard is established by this backend change. The old non-HTTP bootstrap command is removed. IDN-PLATFORM-01 adds independent platform authority for the same initial HUMAN account: the optional initialPlatformRoleCode and initialPlatformRoleName fields select its initial platform role (defaults INITIAL_PLATFORM_ADMIN and Initial platform administrator); the existing company grant and its 17 capabilities remain unchanged. Fresh HTTP installation initially grants platform-role.read, platform-role.create, platform-capability.reconcile, platform-permission.read, platform-role-permission.associate, platform-role-permission.remove, platform-role.rename and platform-role.retire for platform administration; these are executable capabilities, not a fixed customer role hierarchy. An initialized schema is never silently upgraded with platform privileges.

### Runtime role administration

Bearer-authenticated GET `/hyflo/api/v1/platform/roles?afterId=0` returns at most 100 role definitions with id, code, displayName, enabled and platformRole. POST `/hyflo/api/v1/platform/roles` accepts JSON code, displayName, platformRole (required boolean) and reason. A successful response is 201; the new role has no permission links or grants. The two operations require exact independent platform permissions, not company GLOBAL or a role name. Company administrators cannot mutate an account with any platform grant history. No other platform configuration endpoint, role-permission mutation or platform-grant administration is established by this stage. Configuration remains user-controlled within implemented capabilities and authorization invariants.

The owner has deferred Flyway until production preparation. The dev profile uses Hibernate `ddl-auto=update` to create and evolve tables in the development database as JPA entities are introduced. The default profile retains `validate` to avoid implicit schema changes outside development. Hibernate update is not a production migration strategy and does not reliably perform all destructive or structural changes. Use disposable data only. Before any production deployment, establish and verify a versioned Flyway baseline, disable Hibernate schema updates, and retain `validate` for production. No production migration or rollout is selected. Legacy AI, Envers, storage, JWT and business policy settings are preserved in Git history, not represented as working features. Restore typed configuration alongside the responsible implementation. JJWT retains its own Jackson 2 serializer; new MVC code uses Boot's Jackson stack. Current persistent LOCAL JWT authentication is documented in the architecture and roadmap; no external identity provider is enabled.

Any historical real credential or signing key must be rotated by its owner; removing configuration from the current tree does not erase Git history.

## CI and evidence

Every push to main triggers HyFloAPI Build: compile, unit and integration tests, package, JaCoCo coverage, CycloneDX SBOM, effective POM and dependency tree. CI retains reports and the verified JAR. No numeric coverage threshold is imposed on an empty business domain. Add meaningful package-level tests and coverage gates as modules return. The source structure audit remains available manually.

Optional vulnerability scan: configure the repository Actions secret `NVD_API_KEY`, then choose Actions → HyFloAPI Build → Run workflow → security_scan. The separate job checks dependencies and fails on CVSS >= 7 or scanner errors. Local invocation:

```sh
./mvnw -Psecurity-scan org.owasp:dependency-check-maven:13.0.0:check
```

This scan is opt-in; an ordinary successful build does not establish vulnerability-scan acceptance. No release, automatic push, deployment or architecture migration is performed by Maven.


### Supported capability reconciliation (IDN-15)

Authenticated POST `/hyflo/api/v1/platform/capabilities/reconcile` with JSON `{"reason":"Synchronize supported application capabilities"}` requires the exact current platform-capability.reconcile permission. The application supplies its reviewed immutable capability manifest; the caller does not supply codes, labels, executable handlers, company IDs or role assignments. The response is 200/no-store with manifestId, manifestDigest and sorted insertedActionCodes, insertedResourceTypeCodes and insertedPermissionCodes arrays. Exact definitions are reused, missing ones inserted, and conflicting code/pair/label definitions reject the entire transaction with 409. Each successful invocation, including a no-op, records one immutable attributed event. No automatic role-permission association, grant, deletion, startup synchronization or upgrade of existing privileged accounts occurs. Fresh disposable development installation supports the new permission; existing populated installations require a separately selected controlled provisioning mechanism. Never reset a non-disposable database to obtain the permission.


### Runtime role composition (IDN-16)

The platform administrator selects supported permissions from GET `/hyflo/api/v1/platform/permissions?afterId=0`, and reads a role bundle through GET `/hyflo/api/v1/platform/roles/{roleId}/permissions?afterId=0`. Both return bounded ID-ordered pages; stored unsupported definitions are marked as such, not silently adopted. POST `/hyflo/api/v1/platform/roles/{roleId}/permissions/{permissionId}/associate` or `/remove` (same path prefix) accepts JSON `{"reason":"Configure operational role"}` and returns 204/no-store on success. Each write requires its own exact permission and records an immutable attributed role event. Company role changes preserve the foreign-recipient catalog-only rule, prohibit adding privileges to a caller's own non-ended grants, and conservatively prohibit adding permissions to roles with non-ended group grants. Platform roles with non-ended direct grants remain temporarily protected from composition changes pending platform grant administration/continuity safeguards. Unassigned platform roles can be configured. No automatic assignment or upgrade of existing accounts; fresh setup includes the three new operation permissions. Existing schemas and recovery require separately selected controlled provisioning.


### Runtime role lifecycle (IDN-17)

POST `/hyflo/api/v1/platform/roles/{roleId}/rename` accepts `{"displayName":"Operator configured role","reason":"Update role label"}`; POST `/hyflo/api/v1/platform/roles/{roleId}/retire` accepts `{"reason":"Retire obsolete role"}`. Each requires its distinct live platform permission (platform-role.rename or platform-role.retire). Both return 200/no-store with the existing role response. Renaming changes only the label; duplicate labels are permitted. Retirement is permanent and retains the role, permission links and grant history while disabling effective authority and subsequent delegation/composition. A platform role with any non-ended direct grant and a company role with a non-ended bootstrap-attributed direct grant cannot be retired, regardless of its name. Rename records paired old/new snapshots; retirement records one snapshot. The mutation and evidence are atomic. Fresh disposable setup explicitly grants eight platform capabilities; existing initialized accounts are not silently upgraded. No reactivation, hard deletion, platform account/grant management, recovery or production migration is included.

### IDN-18 — Platform role assignment in User Management

Existing user management adds bearer-only GET/POST `/v1/identity/users/{userId}/platform-role-grants` and POST `/v1/identity/users/{userId}/platform-role-grants/{grantId}/revoke`. Exact permissions: `platform-user-role-grant.read`, `platform-user-role-grant.assign`, `platform-user-role-grant.revoke`. Existing company role grants are unchanged. New direct platform grants are immediate and nonexpiring for eligible active LOCAL HUMAN users with supported credentials. Assignment denies self, duplicate non-ended grants, unsupported/empty role bundles and delegation beyond the actor's durable authority. History is cursor-paged; revocation retains actor, reason, time and correlation. Exclusive LOCAL provider serialization and target locks protect against concurrent privilege changes. A grant mutation requires at least one durable eligible administrator holding all implemented platform capabilities through effective grants; finite/future grants and over-cap histories do not count. Fresh HTTP setup admits manifest v4 (17 company, 11 platform); no automatic upgrade of an initialized schema. Existing platform-managed account protections and assigned-role composition/retirement restrictions remain. No new entity/table, workflow engine, production migration or recovery workflow. Exact-SHA CI acceptance pending.

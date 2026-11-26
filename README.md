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

### IDN-19 — Group management and membership

Group Management uses existing Group and GroupMembership models and services, plus GroupDefinitionEvent for immutable definition lifecycle evidence. Bearer-only /v1/platform/groups and /{groupId} reads; POST create, /{groupId}/rename, /{groupId}/retire; GET/POST /{groupId}/memberships and POST /{groupId}/memberships/{membershipId}/revoke. Manifest v5 admits seven exact platform group/membership capabilities (17 company, 18 platform), with fresh explicit HTTP setup only. Groups have configurable stable codes and names, no company owner or inherent authority. Only eligible active LOCAL HUMAN users without platform grant history can be added, no self assignment, overlapping non-ended membership or addition to retired groups or groups with non-ended role grants. Retire preserves history, revocation remains possible, all changes use existing exclusive provider and target locks. No inheritance, group role administration, new workflow framework, production migration or automatic initialized-schema upgrade. Membership actor/time evidence is already persisted; no duplicate membership audit records. Development CI pending.

### IDN-20 — Group role assignment and inherited company authorization

Identity reuses GroupRoleGrant and the existing group administration controller to assign, list and revoke finite company GLOBAL group grants. Manifest v6 (17 company/21 platform) adds platform-group-role-grant.read/assign/revoke to fresh explicit setup only. Assignments require independent platform authority and a covering direct same-company role grant; group membership must respect the selected company and cannot amplify delegated authority. Existing GroupMembershipService admits additions to valid already-granted groups only under the same covering direct role checks. EffectiveAuthorizationService combines direct and eligible inherited company role permissions in both protected reads and company mutation decisions, retaining direct-source delegation rules. Active membership, finite effective grant, enabled group and company role, same owner, eligible LOCAL HUMAN and absence of platform grant history are required for inheritance. Exact scope and supported inherited permission checks; no JWT cache, platform inheritance, nested groups, resource authorization, new table or workflow. Exclusive provider serialization protects group changes against shared provider company writes; history and existing role composition guards retained. No automatic upgrade of initialized schemas. Exact-SHA development acceptance pending.

IDN-21 adds authenticated `GET /v1/me/profile` and `POST /v1/me/profile/email` for own contact email only. HUMAN changes require current local password confirmation; SERVICE accounts can consult their own profile. Email is unverified contact information, not login/recovery authority. Profile pictures remain deferred pending the Infra File owner contract. Exact-SHA CI acceptance pending.


### IDN-22 — Authorized Identity security history consultation

Seven bounded GET collections reuse the six existing immutable Identity event models: /v1/identity/security-history/{authentication,account-lifecycle,employee-associations} requires security-history.read and explicit companyId; /v1/platform/security-history/{role-definitions,group-definitions,capability-reconciliations,unattributed-authentication} requires platform-security-history.read and no company selector. Optional afterId, fixed 100 rows ordered by ID, no counts or exports. Live authorization precedes owner-filtered database reads within read-only REPEATABLE_READ; safe explicit projections omit reason, secrets and private account/employee details, preserve stored event attribution, time, correlation and snapshots. Unattributed failed logins are isolated from company-owned records. Manifest v7 defines 18 company and 22 platform capabilities for fresh explicit setup; no automatic grants to existing initialized schemas. Grant/membership history remains on existing owner routes. No schema or event-write changes; 43 mappings preserved. Runtime acceptance pending exact-SHA CI.


### IDN-23 — Safe assigned-platform role composition and retirement

Existing platform role association, removal and retirement routes now allow changes to assigned roles only under the existing exclusive LOCAL provider gate, independent live operation permission, durable credential-qualified actor authority and prospective continuity. Addition requires pre-existing durable authority for the added supported platform capability and rejects non-ended temporal grant fixtures or unsupported bundles; removal excludes only the exact role/permission link, retirement excludes the entire role. At least one eligible direct nonexpiring HUMAN administrator retains all 22 platform capabilities; actor must retain the authorizing operation after destructive changes. Existing company role guards and grant assignment/revocation semantics remain. Links/grant history and atomic RoleDefinitionEvent evidence retained on retirement. No new routes, permissions, mappings or initialized-schema upgrade/recovery. Manifest v7 remains 40 capabilities. Runtime acceptance pending exact-SHA CI.


### IDN-25 — Protected platform-account lifecycle

Existing User Management now admits five independent bearer-only POST operations under `/v1/identity/users/{userId}/platform-account`: `/suspend`, `/reactivate`, `/disable`, `/credential/reset` and `/sessions/revoke`, gated respectively by platform-user.suspend, platform-user.reactivate, platform-user.disable, platform-credential.reset and platform-login-session.revoke. Manifest v8 defines 45 capabilities (18 company, 27 platform); only fresh explicit setup supplies the expanded initial role, with initialized-schema upgrade deferred by IDN-24. Protected targets are distinct LOCAL HUMAN accounts with any platform grant history, including ended/revoked history. Existing company mutation exclusions remain. Reuse existing User lifecycle transitions, supported LocalCredential hashes, LoginSession and immutable UserLifecycleEvent. Under existing exclusive LOCAL provider/actor/target locks require live exact durable operation authority, a complete credential-qualified administrator (exclude target for suspend/disable/reset), and for credential reset exact durable shared-role coverage of enabled non-ended platform grants and all non-ended same-company GLOBAL direct company grants, including future grants; reject unproved, unsupported, foreign/resource-scoped and over-cap target authority. Suspend/disable/reset revoke target sessions, standalone revocation affects live sessions; reactivation requires supported credential and does not revive old sessions. No new entity/DTO/table, global account discovery, privilege expansion, role/grant mutation or automatic upgrade. Existing actor/target/owner/previous/result status/reason/correlation/time evidence persists atomically. Trusted administrator-supplied password handover remains a bounded development mechanism, not independent target-controlled recovery. Exact-SHA runtime acceptance pending.

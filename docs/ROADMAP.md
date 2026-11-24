# HyFloAPI active task register

## ORG-DATA-001 — Complete organization and geography business data

Status: IN PROGRESS — owner selected 2026-11-24. Baseline 282a51464af23be6da7b15a8110efebd5479ebf7; HyFloAPI Build [passed](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45467359751). The previous roadmap blob is empty; this registration does not reconstruct or claim acceptance of other tasks.

Owner scope: Employee.country means nationality; restore birth, address, picture and contact data; create EmployeeType, EmployeeStatus and five remaining proposed support catalogs. No OrganizationUnitKind; OrganizationUnitType remains byte-identical including table T_10_02_01. Reference retains geography and Infra owns File; Employee uses scalar cross-module IDs, not foreign JPA entities. Preserve the single dated assignment and atomic transfer invariant; additional assignment classifications are data, not concurrent assignment support. No seed rows, REST exposure, authorization expansion, production migration, or deployment. Geography adds active and postal code; country code must have ISO alpha-3 shape. Keep user picture distinct from employee picture.

Table order: OrganizationUnitType 01 (unchanged), EmployeeType 02, EmployeeStatus 03, PositionLevel 04, PositionStatus 05, OrganizationUnitStatus 06, AssignmentType 07, AssignmentStatus 08, Employee 09, Position 10, OrganizationUnit 11, EmployeeAssignment 12 (prefix T_10_02_). Owner-approved disposable schema renumbering only; do not run Hibernate update over existing populated tables.

Registration commit: docs(organization): register approved data completeness task

Implementation commit: feat(organization): complete employee and organization data

### Exact implementation allowlist

Create:
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/domain/model/EmployeeType.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/domain/model/EmployeeStatus.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/domain/model/PositionLevel.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/domain/model/PositionStatus.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/domain/model/OrganizationUnitStatus.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/domain/model/AssignmentType.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/domain/model/AssignmentStatus.java`

Update:
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/domain/model/Employee.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/domain/model/Position.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/domain/model/OrganizationUnit.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/domain/model/EmployeeAssignment.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/domain/model/Country.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/domain/model/State.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/domain/model/District.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/domain/model/Locality.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/application/service/EmployeeAssignmentService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/infrastructure/persistence/repository/EmployeeAssignmentRepository.java`
- `src/test/java/dz/sh/hyflo/HyFloApiIT.java`
- `src/test/java/dz/sh/hyflo/HyFloApiTest.java`
- `docs/ARCHITECTURE.md`

Delete: none.

Acceptance: compile and existing unit tests; synthetic MySQL integration test persists/reloads catalog, profile, nationality IDs, geography and dated assignment data; verify sequential physical columns and named constraints, existing transfer and overlap rejection, finite codes and invalid birth/period/code tests. Exact-SHA HyFloAPI Build must pass before acceptance. Cross-module scalar owner eligibility/FKs and profile read/write authorization require later explicitly selected integration; no runtime proof is claimed by registration.

### ORG-DATA-001 corrective envelope — owner amendment, 2026-11-24

Status: IN PROGRESS. [Build #22 attempt 1](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45471419353) failed at testCompile on ccc57653e5cd4fe44ad1661e2708ede8c70f3d71. Main compilation succeeded; no integration tests ran. The broad previous test edit changed the unrelated Role constructor at line 206; the Position fixture at line 351 was left on the obsolete signature. Restore Role's original two-argument fixture and pass persisted classification fixtures and descriptions to Position. No test suppression or production Role change.

The owner explicitly supersedes the earlier OrganizationUnitType preservation decision: append descriptionAr/Fr/En and active, exclude kind and OrganizationUnitKind. Preserve T_10_02_01, F_00–F_04, code uniqueness and all other entity tables. New types default active; retirement retains references. Optional descriptions use the existing Position text mapping. Do not seed catalog rows or extend assignment/authorization behavior. Dependencies: existing implemented Organization module and disposable MySQL schema.

Approval/scope amendment commit: docs(organization): record CI failure and approved unit type extension

Corrective implementation commit: fix(organization): repair test fixtures and enrich unit types

Exact corrective allowlist (update only):
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/domain/model/OrganizationUnitType.java`
- `src/test/java/dz/sh/hyflo/HyFloApiIT.java`
- `src/test/java/dz/sh/hyflo/HyFloApiTest.java`
- `docs/ARCHITECTURE.md`

Create/delete: none. Acceptance: exact-SHA HyFloAPI Build must compile tests and pass existing unit and MySQL integration assertions, including corrected Identity fixture, Position persistence, type descriptions/active round-trip, retirement preservation and F_00–F_08 physical columns. Prior scope and evidence remain retained except the explicitly superseded type-field restriction.

### ORG-DATA-001 acceptance — 2026-11-24

Status: COMPLETED — corrective SHA 6a82e3ea1b99fd27060153ac26316ab6d4a116ee, [HyFloAPI Build #23 attempt 1](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45473527112) success, including 8 MySQL 8.4 integration tests, zero failures/errors/skips, required test and SBOM evidence and verified JAR. Optional security scan skipped. Acceptance is development/software only. Preserve all preceding entries.

## FDN-DATA-001 — Add approved UserType, Language, Location and MeasurementUnit

Status: IN PROGRESS — owner approved the four retained entities and requested implementation on 2026-11-24. Baseline 6a82e3ea1b99fd27060153ac26316ab6d4a116ee, CI #23 passed. Dependencies: accepted ORG-DATA-001; existing Identity and Reference persistence models. Implement the previously proposed dependency-order table allocation on fresh disposable schemas only. The owner excluded Company; preserve Organization and Infra byte-for-byte, AuthorizationScope including companyId, and all existing business behavior. No optional preferredLanguageId on User (not essential and not separately selected), QuantityType, conversions, external identity, seed data, APIs, migrations or deployment.

- UserType: immutable unique code (HUMAN, SERVICE only), trilingual names; User requires explicit same-module userType, no implicit/default persisted type or classification-based authorization.
- Language: unique lowercase two-letter language code shape, trilingual names, nativeName, rightToLeft, active default true/retire. No claim of ISO catalog membership or translation completeness.
- MeasurementUnit: immutable unique uppercase stable code, trilingual names, case-sensitive symbol text (not a unique key), active default true/retire. No unit conversion, quantity classification, reference-condition handling or solver qualification.
- Location: trilingual name (at least one), finite WGS84 latitude/longitude in decimal degrees within physical range, optional finite elevation in metres above mean sea level (survey datum/engineering qualification deferred), optional same-module Locality. No route geometry or extra entity. Optional elevation is not defaulted to zero.
- Localized names follow current 120-character convention with at least one nonblank name; UserType and MeasurementUnit codes max 64; Language code length 2, nativeName max 120; MeasurementUnit symbol max 40, case preserved.
- Identity table order: existing 01–06 unchanged, UserType 07, User 08, Permission 09, LocalCredential 10, LoginSession 11, AuthenticationEvent 12, GroupMembership 13, RolePermission 14, UserRoleGrant 15, GroupRoleGrant 16, RefreshToken 17. User adds F_07 userType (FK_03), preserve F_00–F_06 and A_01.
- Reference table order: Country 01 unchanged, Language 02, MeasurementUnit 03, State 04, District 05, Locality 06, Location 07. Update dependent named constraints, raw SQL tests and inventory. No table ID duplication. AGENTS examples updated only to reflect User's new table number; governance rules unchanged. The prior proposal explicitly presented this renumbering; owner-approved implementation is interpreted to include the published dependency order, not existing-data migration.

Registration commit: docs(foundation): accept organization data and register approved catalogs

Implementation commit: feat(foundation): add approved identity and reference catalogs

Exact implementation allowlist:

Create:
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/domain/model/UserType.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/domain/model/Language.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/domain/model/MeasurementUnit.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/domain/model/Location.java`

Update:
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/domain/model/User.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/domain/model/Permission.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/domain/model/LocalCredential.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/domain/model/LoginSession.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/domain/model/AuthenticationEvent.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/domain/model/GroupMembership.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/domain/model/RolePermission.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/domain/model/UserRoleGrant.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/domain/model/GroupRoleGrant.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/domain/model/RefreshToken.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/domain/model/State.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/domain/model/District.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/domain/model/Locality.java`
- `src/test/java/dz/sh/hyflo/HyFloApiIT.java`
- `src/test/java/dz/sh/hyflo/HyFloApiTest.java`
- `docs/ARCHITECTURE.md`
- `AGENTS.md`

Delete: none.

Acceptance: compile and existing tests pass with explicit UserType fixtures and all old behavior retained; new unit tests reject unsupported user type, missing names, bad language code/symbol/coordinates (including nonfinite elevation); synthetic MySQL integration persists/reloads both account classifications, multilingual language and RTL metadata, measurement symbols and retirement, Location with/without locality and optional elevation, named constraints, sequential columns and updated actor FK targets; verify mandatory UserType FK and optional Location FK. Exact-SHA HyFloAPI Build pass required, no skipped MySQL test accepted. Fresh-schema testing only; do not run Hibernate update against an existing populated or old-mapped database.

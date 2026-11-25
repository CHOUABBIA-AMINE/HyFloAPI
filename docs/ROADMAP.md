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

### FDN-DATA-001 acceptance — 2026-11-24

Status: COMPLETED — exact implementation SHA 5e333c57a51eebc8354feb8ebaf0fee059011a77, [HyFloAPI Build #24 attempt 1](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45482369400) success. Surefire: 9 tests; Failsafe: 8 integration tests against real disposable MySQL 8.4; both zero failures, errors or skips. Maven verify, required test/JaCoCo/SBOM evidence and verified JAR upload passed. Optional security scan skipped. Acceptance is development/software only, not production schema migration, complete authentication/authorization, catalog seeding, surveyed engineering coordinates or industrial qualification. Company and AuthorizationScope remain unchanged; companyId ownership semantics are unresolved and require a separate owner decision. No next task is registered or selected. Preserve preceding scope and evidence append-only.

Acceptance documentation commit: docs(foundation): accept approved identity and reference catalogs

## FDN-SCOPE-001 — Represent company boundary by a classified organization unit

Status: IN PROGRESS — owner decision 2026-11-24: Company is a specific case of OrganizationUnit; companyId becomes organizationUnitId and the referenced unit must have the company organization-unit type. Baseline dd84c80bf3d07c068464b1509cef229d6774b2b0, [Build #25](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45483175416) success. Depends on accepted FDN-DATA-001 and ORG-DATA-001. Supersedes the previously unresolved companyId decision only; no separate Company or OrganizationUnitKind entity, no new tables or columns, no renumbering.

- Keep F_06 required, immutable positive scalar ID in both embedded grant scopes; rename Java field/getter and all factory parameters to organizationUnitId. Existing GLOBAL/REGION/PIPELINE/INFRASTRUCTURE meanings remain company-bounded; F_05 and F_07 unchanged.
- OrganizationUnitType.COMPANY_CODE = "COMPANY" is the stable code, not a translated name or arbitrary root-unit assumption. No new enum or seed data. Organization owns the exact case-sensitive type check of a persisted unit (query returns type code, not entity). Missing and non-company units rejected identically; positive ID required.
- Admit internal OrganizationUnitScopeService.requireCompanyUnit(long) (Organization owner; Identity application consumer; no public HTTP/API; caller must already be authorized to request the grant). Identity AuthorizationScopeService.validateCompanyBoundary(AuthorizationScope) returns the same structurally valid scope after owner validation. Both grant construction paths in the synthetic integration scenario use it. Pure domain scope factories still validate shape only. No claims of universal write interception, database cross-module FK, effective-date/status eligibility, resource ownership or request-time authorization; those require separate selected protected grant/evaluation use cases. The owner check is deliberately type-only as requested.
- Preserve existing table layouts, historical role-grant behavior, audit actors and temporal scope. No cross-module entity/repository imports by Identity, no external identity, migration, seed or deployment.

Registration commit: docs(foundation): register company organization-unit scope decision
Implementation commit: fix(identity): bind authorization scope to company-classified organization units

Create:
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/infrastructure/persistence/repository/OrganizationUnitRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/application/service/OrganizationUnitScopeService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/AuthorizationScopeService.java`

Update:
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/domain/model/AuthorizationScope.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/domain/model/OrganizationUnitType.java`
- `src/test/java/dz/sh/hyflo/HyFloApiIT.java`
- `src/test/java/dz/sh/hyflo/HyFloApiTest.java`
- `docs/ARCHITECTURE.md`
- `docs/MODULES.md`

Delete: none.

Acceptance: existing exact-SHA build passes; update all Java scope getters, preserve F_06 and other fields; real MySQL integration creates a persisted COMPANY-typed unit and child with a different type, verifies accepted GLOBAL and specific scopes, rejects missing/non-company references with identical failure, persists/reloads user and group grants using actual company-unit ID, preserves actor and revocation tests. Unit scope tests retain all four shape cases and check renamed accessor; no skipped tests. No production authorization or schema compatibility claim.

### FDN-SCOPE-001 CI #26 correction — 2026-11-24

Status: IN PROGRESS. [Build #26 attempt 1](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45491336790) at 2b0efe235693e7374eabe8454aa0f0fcdefbd220: production and test compilation passed; Surefire 9/9 passed; MySQL 8.4 Failsafe 8 tests, 0 failures, 1 error, 0 skips. Error at HyFloApiIT.persistsDirectAndGroupRoleGrantsWithIndependentResourceScopes line 241: duplicate ACTIVE code for T_10_02_06_UK_01. The prior committed assignment fixture and the new transactional grant fixture both tried to create the same unique OrganizationUnitStatus row. This is a fixture isolation defect, not permission to relax the catalog constraint.

Corrective scope: reuse the existing ACTIVE catalog row when present, create a synthetic one only when absent, within the existing transactional grant test. Do not depend on test ordering; keep original committed assignment/transfer scenario and all assertions intact. Run expected service rejection checks after successful persistence/readback/revocation assertions, since expected exceptions propagated through participating transaction proxies can mark the test transaction rollback-only. The test is rolled back by Spring; no production transaction semantics are changed. Preserve all model/service/repository code, physical constraints, CI configuration, tests and original task acceptance. No seed, production change or subsequent task.

Correction documentation: docs(foundation): record scope integration test fixture failure
Corrective implementation: test(foundation): reuse organization status fixture in scope integration test

Exact corrective allowlist: update only `src/test/java/dz/sh/hyflo/HyFloApiIT.java`. Create/delete: none.

Acceptance: mandatory exact-SHA CI passes all 9 unit and 8 MySQL integration tests with no errors/failures/skips, retains all existing grant/company validation and assignment tests, and verifies required evidence. No local runtime claim.

### FDN-SCOPE-001 acceptance — 2026-11-25

Status: COMPLETED — corrective SHA ccf1bbf80fd9c81a9084490b6ab868275fa46c88, [HyFloAPI Build #27 attempt 1](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45491729721) success. Surefire 9 tests, Failsafe 8 tests against MySQL 8.4, zero failures/errors/skips in both. Build and required evidence steps passed; optional security scan skipped. Company classification by OrganizationUnitType code COMPANY and organizationUnitId scope mapping accepted within the registered development scope. No claim of complete authorization, seed data or production readiness. Prior entries retained.

## FDN-REPO-001 — Complete Foundation repository coverage

Status: IN PROGRESS — owner explicitly requests repositories for all created models, at least an empty repository, 2026-11-25. Baseline ccf1bbf80fd9c81a9084490b6ab868275fa46c88, CI #27 success. Dependencies: accepted FDN-SCOPE-001, FDN-DATA-001 and ORG-DATA-001. Source inventory: 37 @Entity classes, all Long identifiers, and one @Embeddable AuthorizationScope. Existing EmployeeRepository, EmployeeAssignmentRepository and OrganizationUnitRepository retain byte-identical methods and mappings. Add 34 empty typed Spring Data Repository<Entity, Long> interfaces in the owning module's infrastructure.persistence.repository package, consistent with existing restricted repository convention. No JpaRepository/CrudRepository inheritance, generic mutation methods, extra queries, services, APIs, cross-module access, model changes, new tables, seed data or deployment. AuthorizationScope is embedded and has no standalone identity/table; no repository for it. This is explicitly selected structural work, not a functional workflow or blanket CRUD authorization. Repository bean discovery/type binding shall be verified in existing real-MySQL application context; no new dependencies or CI changes.

Registration commit: docs(foundation): accept company scope and register repository coverage
Implementation commit: feat(foundation): add missing entity repository interfaces

Create:
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/ActionRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/AuthenticationEventRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/GroupRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/GroupMembershipRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/GroupRoleGrantRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/IdentityProviderRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/LocalCredentialRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/LoginSessionRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/PermissionRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/RefreshTokenRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/ResourceTypeRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/RoleRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/RolePermissionRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/UserRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/UserRoleGrantRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/UserStatusRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/UserTypeRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/infrastructure/persistence/repository/AssignmentStatusRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/infrastructure/persistence/repository/AssignmentTypeRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/infrastructure/persistence/repository/EmployeeStatusRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/infrastructure/persistence/repository/EmployeeTypeRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/infrastructure/persistence/repository/OrganizationUnitStatusRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/infrastructure/persistence/repository/OrganizationUnitTypeRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/infrastructure/persistence/repository/PositionRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/infrastructure/persistence/repository/PositionLevelRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/infrastructure/persistence/repository/PositionStatusRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/infrastructure/persistence/repository/CountryRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/infrastructure/persistence/repository/DistrictRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/infrastructure/persistence/repository/LanguageRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/infrastructure/persistence/repository/LocalityRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/infrastructure/persistence/repository/LocationRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/infrastructure/persistence/repository/MeasurementUnitRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/infrastructure/persistence/repository/StateRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/infra/infrastructure/persistence/repository/FileRepository.java`

Update:
- `docs/ARCHITECTURE.md`
- `src/test/java/dz/sh/hyflo/HyFloApiIT.java`

Delete: none.

Acceptance: 37 entity-specific Spring Data repository interfaces, one per Foundation entity, 17 Identity, 12 Organization, 7 Reference, 1 Infra, no repository for embeddable; existing three repositories unchanged; test resolves each managed Foundation entity's repository by Spring Data repository metadata, asserts module-local interface and Long ID binding and verifies the count, excludes SchemaProbe and embeddable; existing unit and real-MySQL integration tests pass with no skips or weakened gates. Exact-SHA HyFloAPI Build must pass before acceptance. No runtime CRUD or production authorization claim.

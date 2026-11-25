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

### FDN-REPO-001 acceptance — 2026-11-25

Status: COMPLETED — exact implementation SHA df4bc14e934f54bee952e9ff3af8610e28112c23, [HyFloAPI Build #28 attempt 1](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45566373412) success. Surefire 9 tests and Failsafe 9 integration tests against MySQL 8.4, zero failures/errors/skips in both. Spring Data runtime discovered 37 JPA repository interfaces; repository coverage test passed. Required build, test/SBOM evidence and artifact steps passed; optional security scan skipped. Acceptance is structural/development only; no generic CRUD or production authorization claim. Previous entries retained.

## FDN-SVC-001 — Complete Foundation entity application service coverage

Status: IN PROGRESS — owner requests the same structural completion for services, 2026-11-25. Baseline df4bc14e934f54bee952e9ff3af8610e28112c23, CI #28 success. Depends on accepted FDN-REPO-001 and FDN-SCOPE-001. Add one named concrete Spring @Service per existing Foundation entity lacking its own EntityNameService: 36 new classes, each in the owning module's application.service package, with only a private final same-module EntityNameRepository and explicit constructor injection. No methods or transaction annotations until a concrete use case is selected. Keep EmployeeAssignmentService, OrganizationUnitScopeService and AuthorizationScopeService byte-identical. OrganizationUnitService is the entity-specific skeleton, distinct from the existing type-validation contract OrganizationUnitScopeService. AuthorizationScope is an embeddable, not an entity; its existing service remains an internal scope operation. Result: 37 entity-specific services and two existing scope services (39 total), 37 existing repositories, unchanged 37 entities. No generic CRUD/base service, endpoint, new export/contract, additional permission, seed, migration or production behavior. This expressly selected structural task does not establish functional completeness.

Registration commit: docs(foundation): accept repository coverage and register service coverage
Implementation commit: feat(foundation): add missing entity application services

Create:
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/ActionService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/AuthenticationEventService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/GroupService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/GroupMembershipService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/GroupRoleGrantService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/IdentityProviderService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/LocalCredentialService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/LoginSessionService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/PermissionService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/RefreshTokenService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/ResourceTypeService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/RoleService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/RolePermissionService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/UserService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/UserRoleGrantService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/UserStatusService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/UserTypeService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/application/service/AssignmentStatusService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/application/service/AssignmentTypeService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/application/service/EmployeeService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/application/service/EmployeeStatusService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/application/service/EmployeeTypeService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/application/service/OrganizationUnitService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/application/service/OrganizationUnitStatusService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/application/service/OrganizationUnitTypeService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/application/service/PositionService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/application/service/PositionLevelService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/application/service/PositionStatusService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/application/service/CountryService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/application/service/DistrictService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/application/service/LanguageService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/application/service/LocalityService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/application/service/LocationService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/application/service/MeasurementUnitService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/reference/application/service/StateService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/infra/application/service/FileService.java`

Update:
- `docs/ARCHITECTURE.md`
- `src/test/java/dz/sh/hyflo/HyFloApiIT.java`

Delete: none.

Acceptance: 37 entity-specific service classes registered as unique Spring beans, all 36 skeletons have exactly one constructor taking their corresponding owner repository and no declared business methods, existing EmployeeAssignmentService and both scope services preserved and registered. Test in existing real-MySQL integration context using JPA metamodel and Spring bean lookup, verify 37 entity mappings and repository constructor types; preserve repository discovery and all prior assertions, no new dependencies or CI changes. Exact-SHA build must pass all unit and integration tests without failures, errors or skips; no business functionality or deployment claimed.

### FDN-SVC-001 CI #29 correction — 2026-11-25

Status: IN PROGRESS. [Build #29 attempt 1](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45566954436) at ae03858c811a41fc88c9b3d8824cc7a7fc3d6c5a: production and test compilation passed, Surefire 9/9 passed, Failsafe 10 tests against MySQL with 1 failure, 0 errors, 0 skips. Repository scanning found 37 interfaces. Failure: HyFloApiIT.registersOneApplicationServiceForEveryFoundationEntity line 158 expected getDeclaredMethods() to be empty, but UserStatusService contains the synthetic private static JaCoCo $jacocoInit method added by the coverage agent. This is an instrumentation-aware reflection assertion defect, not a demonstrated service registration or constructor defect.

Corrective scope: retain the zero-business-method invariant for all 36 new skeletons. Exclude only the synthetic private static method named $jacocoInit from the declared-method assertion; reject every other declared method, including private methods. Preserve unique bean registration, all constructor/repository binding checks, existing tests, production source, JaCoCo instrumentation, Maven configuration and CI gates. Add a service-specific assertion diagnostic. No additional functionality, schema, dependencies or subsequent task.

Correction documentation commit: docs(foundation): record service coverage instrumentation failure
Corrective implementation commit: test(foundation): account for JaCoCo service instrumentation

Exact corrective allowlist: update only `src/test/java/dz/sh/hyflo/HyFloApiIT.java`. Create/delete: none.

Acceptance: exact-SHA HyFloAPI Build passes all 9 unit and 10 integration tests, including repository discovery and service registration with coverage instrumentation enabled; no failures, errors or skips, required test/JaCoCo/SBOM evidence and verified artifact steps pass. No local runtime claim.

### FDN-SVC-001 acceptance — 2026-11-25

Status: COMPLETED — corrective SHA 435f3ea5e48eb4bfabbbe7d1a718f231c27b8d9b, [HyFloAPI Build #30 attempt 1](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45567475259) success. Surefire 9 tests and Failsafe 10 integration tests against MySQL 8.4, zero failures/errors/skips in both. Runtime repository scan found 37 interfaces; structural service registration test passed with JaCoCo enabled. All mandatory verification, test/JaCoCo/SBOM and application JAR evidence steps passed; optional security scan skipped. Accepted 37 entity-specific services plus two scope services as a structural baseline only. Preserve the previous evidence. When a selected functional task introduces service methods or additional dependencies, explicitly replace the corresponding temporary skeleton assertion with functional and structural evidence; retain the service discovery and ownership checks.

## IDN-01 — Identity initialization and trusted administrator bootstrap (candidate)

Status: BLOCKED — workflow-driven Identity pilot is selected for design by the owner; the next proposed package combines initial reference/catalog initialization and the first trusted administrator. Baseline 435f3ea5e48eb4bfabbbe7d1a718f231c27b8d9b, Build #30 passed. Dependencies: accepted FDN-SVC-001, FDN-REPO-001, FDN-SCOPE-001, Organization company eligibility. No implementation authorization/envelope yet. This is a recorded decision gate, not an executable task. The wider 42-operation / 12-package pilot inventory remains a design proposal until individually registered; no other module selected.

Verified blocker: UserRoleGrant.grantedBy is a required FK to an already persisted User; ordinary grant actors must derive from authenticated context. The empty Identity database has no authenticated granting administrator. The existing OrganizationUnitScopeService validates only the COMPANY type, not current company eligibility. Identity cannot create an Organization unit by itself, and no Organization catalog or company rows have been seeded. A first grant must not use a fabricated authenticated actor, a nullable FK, an ordinary self-grant, a permanent HTTP bootstrap endpoint or a default superuser bypass.

Recommended single owner decision for IDN-01:
- Explicit, opt-in, one-time non-HTTP bootstrap command executed by a trusted deployment operator on a fresh development Identity schema. No automatic startup privilege creation. Credentials supplied externally, hashed using a supported password encoder; no plaintext persistence or logging. Never overwrite existing credentials or re-grant authority on repeated execution.
- Require an already provisioned COMPANY-classified OrganizationUnit, validated through an Organization-owned contract for type, active status and effective dates; missing/ineligible company fails closed. No automatic Company entity or Organization provisioning inside Identity; use synthetic Organization fixtures in tests. Provisioning of an initial company for a clean environment remains an explicit owner prerequisite.
- Use a dedicated persisted UserType SERVICE bootstrap attribution account, permanently DISABLED, no LocalCredential, no LoginSession and no role grants. This is a proposed one-time trusted initialization attribution exception to normal authenticated-actor governance, not a new UserType, a login-capable account, or a claim that a human was authenticated. Capture the trusted operator reference and initialization provenance in the initial grant reason without secrets; attribute the first human admin grant to this technical account. Ordinary later grant paths must still derive an authenticated actor and must not use this exception.
- Initialize LOCAL, HUMAN/SERVICE, PENDING/ACTIVE/SUSPENDED/DISABLED and only explicitly approved action/resource/permission definitions. Proposed initial role IDENTITY_ADMIN is restricted to the supplied company and approved Identity administrative capabilities; no wildcard or engineering permission, no platform-wide GLOBAL, and no implicit privilege from UserType. The initial exact capability manifest must be included in the implementation envelope before code; definitions for later workflows do not authorize HTTP operations until enforced by their selected functional tasks.
- Atomically provision the named HUMAN admin, encoded credential, valid activation, approved role/permission associations and first company-scoped grant. Prove concurrency safety and atomic rollback on failures; concurrent/repeated initialization cannot create another initial admin or restore revoked authority. No login endpoint, JWT, generic CRUD, seed on ordinary startup, new model fields/tables, production migration or deployment as part of this candidate.
- Required acceptance: real disposable MySQL positive initialization/readback, secrets not exposed, technical actor cannot authenticate, exact company and explicit permission scope, missing/invalid company rejection, disabled-by-default invocation, invalid inputs and rollback, duplicate/repeated/concurrent initialization behavior, preservation of constraints and existing tests. Future functional service changes must preserve bean discovery and same-module repository ownership while replacing only superseded empty-skeleton assertions. Exact implementation paths, commit message and approved permission catalog must be registered after the decision and before edits.

No source edits under this decision gate. The next executable package requires explicit approval of the bootstrap attribution exception and a completed registered implementation envelope; no subsequent task may be advanced automatically.

### IDN-01 approved implementation — 2026-11-25

Status: IN PROGRESS — owner explicitly approved the one-time disabled SERVICE attribution account proposal. Baseline 5923bbf58628cf8a7af5293ebb9ec357cd8c08ab; [Build #31](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45572722929) success. Depends on accepted Foundation baseline and a pre-provisioned eligible Organization company. This entry resolves the preceding decision gate without changing normal authenticated actor rules. No other task selected.

Implementation contract:
- Explicit sole argument `--identity-bootstrap` dispatches from HyFloApi to a non-web Spring context, invokes IdentityBootstrapCommand and closes the context; ordinary startup never invokes initialization. No HTTP or automatic runner. Read only external environment HYFLO_BOOTSTRAP_COMPANY_ID (positive Long), HYFLO_BOOTSTRAP_USERNAME, HYFLO_BOOTSTRAP_EMAIL, HYFLO_BOOTSTRAP_PASSWORD, HYFLO_BOOTSTRAP_OPERATOR (trusted operator reference, bounded safe characters). Reject missing/invalid inputs, reserved bootstrap identity and additional command arguments. Password minimum 15 characters, maximum 72 UTF-8 bytes for bcrypt, no blank secret; shared platform PasswordEncoder bean uses Spring delegating bcrypt encoding. No default or password in arguments/logs/result. Command failure returns nonzero through a sanitized exception. Development schema only, existing DB configuration/profile preserved.
- Organization owner contract requireEligibleCompanyUnit(long, LocalDate) checks exact COMPANY code, active type, ACTIVE unit status and half-open date validity at UTC initialization date. Query the owner entity with fetched type/status and pessimistic read lock; no JPA entity returned to Identity. Keep original classification-only contract unchanged. No Organization seed or creation.
- All initialization in one Identity application transaction via module-owned repositories with only explicit count/save operations; reject any existing independent root (IdentityProvider, UserStatus, UserType, Action, ResourceType, Role, Group), which also excludes dependent Identity rows via existing FKs. Insert the unique LOCAL provider row first, before other identity writes; its unique constraint and transactional insert serialize competing initialization attempts even with different company IDs. Losing attempts fail, no retry/upsert; any failure rolls back the whole initialization. Existing data or repeated invocation fails without mutation; manual out-of-band destructive DB changes not supported.
- Create LOCAL, PENDING/ACTIVE/SUSPENDED/DISABLED, HUMAN/SERVICE. Create reserved username hyflo-bootstrap with reserved non-routable email hyflo-bootstrap@hyflo.invalid, SERVICE, DISABLED, no credential/session/grant. Create supplied HUMAN admin with no employee assumption, encoded credential and valid activation. Initial grant is GLOBAL (supplied company only), with reason containing IDN-01 and externally supplied trusted operator reference (not independent identity proof); grantedBy is the persisted disabled attribution account. No user schema changes or generic actor exception for other workflows.
- Initial explicit capability manifest (resource.action codes, each distinct ResourceType/Action pair) chosen conservatively from the pilot: user.read, user.create, user.activate, user.suspend, user.reactivate, user.disable, credential.initialize, credential.reset, login-session.revoke, role.read, permission.read, user-role-grant.read, user-role-grant.assign, user-role-grant.revoke, identity-catalog.read. Seed only these 15 definitions, their distinct action/resource codes and one IDENTITY_ADMIN role with exactly these associations. Stable technical codes, English display labels and trilingual UserType names. These are reserved definitions for the approved Identity pilot, not implemented HTTP capabilities. No wildcard, global role/group mutations, engineering permissions, security history across companies or platform superuser. Later catalog additions require selected functional tasks and actual scope enforcement. 
- No login/refresh/JWT endpoint, no runtime administrative authorization, no API DTO/controller, no new entity/table/column, no schema migration, no change to existing entity services/structural assertions. Existing temporary Spring Security baseline remains only for existing smoke tests; not an authorization bypass or usable bootstrap administrator login.

Registration commit: docs(identity): approve and register trusted initialization
Implementation commit: feat(identity): implement one-time company administrator bootstrap

Create:
- `src/main/java/dz/sh/hyflo/bootstrap/IdentityBootstrapCommand.java`
- `src/main/java/dz/sh/hyflo/platform/security/PasswordEncodingConfiguration.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/IdentityBootstrapService.java`
- `src/test/java/dz/sh/hyflo/IdentityBootstrapIT.java`

Update:
- `src/main/java/dz/sh/hyflo/HyFloApi.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/IdentityProviderRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/UserStatusRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/UserTypeRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/ActionRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/ResourceTypeRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/RoleRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/GroupRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/UserRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/LocalCredentialRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/PermissionRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/RolePermissionRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/UserRoleGrantRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/infrastructure/persistence/repository/OrganizationUnitRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/application/service/OrganizationUnitScopeService.java`
- `docs/ARCHITECTURE.md`
- `docs/MODULES.md`

Delete: none.

Acceptance: real isolated MySQL 8.4 integration test invokes the actual command handler in a non-web application context, asserts no automatic initialization and rejects missing opt-in, additional args, invalid input, missing/non-company/inactive/expired/future company, and retired type; forces late DB failure using test-only trigger and proves complete rollback; races two different administrator requests and requires exactly one successful initialization. Verify all exact catalog codes/role associations, admin active HUMAN, one encoded credential, disabled SERVICE attribution account without credential/session/grants, one GLOBAL grant with correct company, persisted actor and provenance. Prove re-invocation after revocation does not change credentials, create a new admin or restore privileges. Retain existing 9 unit/10 integration tests, repository discovery and service skeleton checks; new independent MySQL context, no skipped tests or CI gate changes. Verify exact-SHA build before acceptance. Packaged external command invocation/deployment is not performed in this task; real integration covers its handler and wiring.

### IDN-01 CI #32 correction — 2026-11-25

Status: IN PROGRESS. [Build #32 attempt 1](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45579718319) at 8ea5a41a9d2c4ffb1582dfb6723944ac667baac9: compilation passed, Surefire 9/9 passed; Failsafe 11 tests, 1 failure, 0 errors, 0 skips. IdentityBootstrapIT passed its full real-MySQL scenario, including synthetic rollback trigger and competing LOCAL insert. HyFloApiIT.openApiRequiresLocalAuthentication line 723 expected 200 but got 401. Security log reports DelegatingPasswordEncoder rejecting the unprefixed password of the existing auto-configured in-memory user after the new global PasswordEncoder bean was registered. This is a regression in temporary HTTP authentication, not a demonstrated bootstrap transaction failure. Shutdown also exceeded the 30-second fork exit timeout while cached contexts attempted schema cleanup after MySQL containers had stopped.

Corrective scope: isolate the persistent Identity encoder behind a typed platform IdentityPasswordEncoder bean that does not implement Spring Security PasswordEncoder; internally delegate to the same PasswordEncoderFactories.createDelegatingPasswordEncoder() for encode/matches. Inject that explicit type in IdentityBootstrapService and its integration test. No global PasswordEncoder bean, no NoOp fallback, no change to temporary account configuration or HTTP security, no weaker bcrypt policy or changed stored format. Assert that no PasswordEncoder bean is globally discoverable in the bootstrap context and retain hash matching and the existing authenticated/unauthenticated HTTP checks. Close both MySQL-backed Spring test contexts after their test classes with DirtiesContext AFTER_CLASS to avoid cached-context cleanup against stopped containers; no timeouts or CI changes. Preserve all original business acceptance scenarios and constraints.

Correction documentation: docs(identity): register bootstrap encoder isolation correction
Corrective implementation: fix(identity): isolate persistent credential encoder from temporary authentication

Exact corrective allowlist — update:
- `src/main/java/dz/sh/hyflo/platform/security/PasswordEncodingConfiguration.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/IdentityBootstrapService.java`
- `src/test/java/dz/sh/hyflo/IdentityBootstrapIT.java`
- `src/test/java/dz/sh/hyflo/HyFloApiIT.java`
- `docs/ARCHITECTURE.md`

Create/delete: none.

Acceptance: exact-SHA CI passes 9 unit and 11 MySQL integration tests, existing OpenAPI anonymous/wrong password 401 and correct password 200, unchanged bootstrap scope, stored {bcrypt} hash and matching, no global encoder bean, no skipped tests; retain mandatory evidence and artifact gates. Verify actual shutdown evidence, not merely annotations. No subsequent package or runtime login implementation.

### IDN-01 acceptance — 2026-11-25

Status: COMPLETED — corrective SHA b67ac5b2840ad0c4b8bc9c1f37b8c1dc4824ec47, [HyFloAPI Build #33 attempt 1](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45581326981) success. Surefire 9/9, Failsafe 11/11 against two isolated MySQL 8.4 containers, zero failures/errors/skips. HyFloApiIT and IdentityBootstrapIT passed; existing HTTP authentication assertions retained; synthetic final-insert failure and duplicate LOCAL race exercised, bootstrap hashing and no global PasswordEncoder bean verified. Both Hikari pools logged shutdown completed, graceful servlet shutdown completed, no fork-exit timeout. Mandatory test/JaCoCo/SBOM/artifact gates passed; optional security scan skipped. Accept initialization handler and transactional behavior in development only; no actual packaged-command/deployment execution, company provisioning, login or runtime authorization claim. Preserve all preceding evidence.

## IDN-02 — Local authentication and stateless access-token enforcement

Status: PLANNED — registered next eligible functional package of the owner-selected Identity pilot following IDN-01 acceptance. Baseline b67ac5b2840ad0c4b8bc9c1f37b8c1dc4824ec47; Build #33 passed. Dependencies: IDN-01, FDN-SCOPE-001, FDN-REPO-001, FDN-SVC-001. No implementation in this acceptance/registration commit. Next selects this package after refreshing exact-SHA CI; no subsequent package automatically selected.

Workflow coverage: IDN-W12 local login, IDN-W13 access-token validation, IDN-W40 successful/failed login evidence. Provide minimal own authenticated identity read as prerequisite to later profile operations; IDN-W14/15 refresh rotation/logout and all administrative/scoped authorization operations remain later work. Business owner Identity; User status/provider eligibility, LocalCredential verification, LoginSession validity, initial RefreshToken digest and AuthenticationEvent are existing model responsibilities; no new entities/fields/tables. Platform security owns cryptography, servlet filter chain, bounded technical throttling and safe HTTP security errors. No external provider, HTTP session or generic CRUD.

Contract and invariants:
- POST /hyflo/api/v1/auth/login accepts validated JSON LoginRequest (username and password only; existing username normalization; nonblank bounded input, bcrypt 72 UTF-8 byte maximum). Success 200 TokenResponse with accessToken, tokenType Bearer, accessExpiresAt, opaque refreshToken, sessionExpiresAt; no-store/no-cache. GET /hyflo/api/v1/me returns CurrentIdentityResponse (own user ID, username, session ID; no grants, credential, cross-user or company data). No generic User endpoint; explicit mapper from safe immutable application result records, never entity serialization.
- Resolve LOCAL provider by exact stable code, user by normalized username/provider, current status/provider and own credential. Only ACTIVE eligible HUMAN/SERVICE accounts with valid credential and enabled LOCAL provider; reserved disabled bootstrap actor cannot authenticate. Use existing IdentityPasswordEncoder explicitly, reject unsupported/malformed credential hashes, no plaintext/noop fallback; use a precomputed dummy bcrypt hash for unresolved/ineligible accounts and generic login rejection to reduce enumeration. No credentials, JWTs, refresh values/digests or sensitive request bodies in logs. Malformed HTTP input gets safe 400; valid but rejected credentials get indistinguishable 401; throttling gets generic 429. Server generates correlation ID; no trusting caller actor/source headers.
- Application login transaction: lock matching User row (pessimistic write) and read its credential/provider/status inside the transaction; use same owner lock ordering for future credential/account lifecycle changes. On success persist one LoginSession, one cryptographically random opaque refresh token digest (SHA-256 lowercase hex of 32 random bytes encoded as base64url without padding), LOGIN_SUCCEEDED event; sign access JWT, commit before response. Rejected known/unknown user with existing LOCAL provider records LOGIN_FAILED with optional user ID and returns a rejected application outcome (not an exception that rolls back evidence). Before LOCAL initialization or when audit cannot be persisted, deny without inventing a provider/event; operational readiness failure distinct from success, no sensitive details. Successful login failure injection must rollback all three writes and return no tokens. No rotation/refresh endpoint yet; issued refresh tokens unusable until IDN-04. Session revocation and expiry are enforced now even though the revocation API remains later.
- Signed access JWT HS256 with explicitly selected algorithm, external base64-encoded key of at least 32 random bytes, strict issuer/audience hyflo-api, access token type, subject positive user ID, session ID, jti, iat/exp. Validate signature, algorithm, required claims, issuer/audience, expiry and positive identifiers; token expiration bounded by session expiry. Reject invalid/missing/expired/wrong-session tokens, no client-supplied privilege claims, no role name/permission claim authorization. Defaults proposed for development pilot: access 10 min, absolute session 8 h, configurable positive bounded durations (access max 15 min, session max 24 h, access no longer than session), no hardcoded signing key. Servlet startup fails closed if key/config absent or invalid; non-web bootstrap context must not depend on JWT key or servlet-only components.
- SessionAuthenticationService is the explicit Identity owner contract to platform.security: after signature validation, accept immutable user/session IDs and current server time, return immutable own principal IDs/username only if session is active and belongs to ACTIVE user with enabled LOCAL provider; no JPA model or private repository crosses boundary. Evaluate on every protected request; DB errors fail closed. No claims of permission evaluation, cross-company/resource coverage or stale grant authorization. No cached authorization.
- Replace temporary in-memory HTTP authentication (remove its configuration and disable actual Boot user-details auto-configuration using verified dependency/source) with a servlet-only stateless SecurityFilterChain: no Basic/form/remember-me or cookie auth, no HTTP session or saved request; CSRF disabled only for bearer-only JSON API, CORS not opened globally. Permit POST login and health GET; require live bearer identity for GET /v1/me and existing enabled development OpenAPI/Swagger read surfaces; deny every other route, including engineering/administrative endpoints, until explicitly admitted by IDN-03/later workflows. No authenticated blanket business access. Filter must run once inside chain, not additionally auto-registered as servlet filter. Authentication failures use 401, authenticated disallowed routes 403; safe ProblemDetail with stable code/correlation ID where available, English fallback until localization selected. Keep actuator exposure config; non-health endpoints not admitted.
- Before expensive credential work, apply bounded thread-safe single-process login limiter (global, direct remote IP, normalized username independent limits; no forwarded-header trust). Configurable fixed window default 60s, defaults global 120, IP 20, username 10 attempts; bounded bucket capacity and expired-entry eviction, fail closed if saturated, 429 and Retry-After. Only syntactically valid attempts reach expensive auth; do not expose identity existence. This is development single-instance rate limiting, not distributed attack protection or permanent account lock. No schema change.
- Reuse existing entity services unchanged; implement coherent AuthenticationService workflow and SessionAuthenticationService rather than adding generic repository CRUD. Add only explicit same-owner lookup/save methods to six repositories, preserving IDN-01 count/save methods. Wire servlet-only token/HTTP collaborators without making non-web initialization require signing configuration. Existing 37 entity service skeleton assertions remain valid; new orchestration classes do not replace entity-specific services. Existing password format and bootstrap behavior preserved.
- Update the previous temporary-auth smoke test deliberately: anonymous and wrong/old valid Basic credentials must be 401; replace positive Basic proof with successful persistent login -> authenticated OpenAPI read in the new real-MySQL HTTP integration scenario. Do not make docs public, weaken health/scope checks, or suppress tests. Test-only JWT key is synthetic. No new dependencies, migrations, POM/workflow changes, or deployment.

Implementation commit: feat(identity): implement local login and stateless access authentication

Exact create:
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/AuthenticationService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/SessionAuthenticationService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/api/controller/AuthenticationController.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/api/request/LoginRequest.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/api/response/TokenResponse.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/api/response/CurrentIdentityResponse.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/api/mapper/AuthenticationMapper.java`
- `src/main/java/dz/sh/hyflo/platform/security/IdentityAuthenticationProperties.java`
- `src/main/java/dz/sh/hyflo/platform/security/JwtTokenService.java`
- `src/main/java/dz/sh/hyflo/platform/security/LoginAttemptLimiter.java`
- `src/main/java/dz/sh/hyflo/platform/security/BearerAuthenticationFilter.java`
- `src/main/java/dz/sh/hyflo/platform/security/IdentitySecurityConfiguration.java`
- `src/main/java/dz/sh/hyflo/platform/exception/AuthenticationProblems.java`
- `src/test/java/dz/sh/hyflo/IdentityAuthenticationIT.java`
- `src/test/java/dz/sh/hyflo/IdentityAuthenticationTest.java`

Exact update:
- `src/main/java/dz/sh/hyflo/HyFloApi.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/IdentityProviderRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/UserRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/LocalCredentialRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/LoginSessionRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/RefreshTokenRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/AuthenticationEventRepository.java`
- `src/main/resources/application.properties`
- `src/test/resources/application-test.properties`
- `src/test/java/dz/sh/hyflo/HyFloApiIT.java`
- `docs/ARCHITECTURE.md`
- `docs/MODULES.md`

Delete: none.

Acceptance:
- Existing 9 unit and 11 integration methods retained (temporary positive Basic expectation replaced with bearer positive proof, not removed without replacement); new IdentityAuthenticationTest exercises JWT validation/config, expiry boundary, bounded limiter and safe response contracts; IdentityAuthenticationIT uses separate MySQL 8.4 disposable context with clean closure, approved bootstrap handler and actual HTTP endpoints.
- Bootstrap admin login with normalized username and real encoded password, exact session/refresh digest/success event readback, JWT claims and lifetime, no plaintext/digest/hash returned except the intended opaque refresh token, no HTTP session cookie, no-store; own identity and enabled OpenAPI read succeed only with a valid live bearer.
- Anonymous, wrong password, unknown username, missing credential, pending/suspended/disabled users, disabled provider, reserved bootstrap account, malformed input, throttle exhaustion, missing/invalid key, expired/tampered/wrong issuer/audience/algorithm/session-owner token, revoked/expired session, inactive account after issuance; generic rejection and failure event persistence where LOCAL exists. Reject Basic even with old correct password, deny unregistered/admin routes even to authenticated admin; do not infer effective grant authorization. Forced DB failure on success path verifies complete rollback and no token response. Verify clean MySQL context shutdown, no skips, exact-SHA mandatory CI and actual runtime OpenAPI, no new claim about deployment, multi-instance throttling, refresh rotation, or downstream authorization.
- IDN-03 remains proposed next (effective permission and company scope); requires separate registration/selection after IDN-02 acceptance.

Registered design choices for owner review: 10-minute access / 8-hour session configurable defaults, symmetric HS256 externally configured secret, opaque initial refresh, bounded single-instance throttling, minimal own identity read, deliberate removal of temporary Basic mechanism. No credentials or secrets seeded by IDN-02. No source edits in this registration.

### IDN-02 acceptance — 2026-11-25

Status: COMPLETED — implementation SHA 6fab6dea44f621b7e57d01a66723a91fec5a2e05, [HyFloAPI Build #35](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45587335683) success. Surefire 13/13, Failsafe 12/12 across three isolated MySQL 8.4 containers, zero failures/errors/skips. IdentityAuthenticationTest and IdentityAuthenticationIT passed; actual HTTP persistent login, live bearer/session/account checks, old Basic rejection, deny-by-default routing, authenticated runtime OpenAPI, rate-limit and security evidence assertions passed. The synthetic login rollback trigger exercised success/failure event persistence failures. All three Hikari pools logged shutdown completed, both servlet contexts graceful shutdown, no fork-exit timeout. Mandatory test/JaCoCo/SBOM/artifact evidence passed; optional security scan skipped. Accept only the registered development authentication capability; no refresh rotation/logout, permission enforcement, production deployment or distributed rate limiting claim. Preserve prior evidence.

## IDN-03 — Effective permission and company-scoped authorization

Status: PLANNED — registered next eligible package of the selected Identity pilot, baseline 6fab6dea44f621b7e57d01a66723a91fec5a2e05; CI #35 passed. Dependencies: IDN-01, IDN-02, FDN-SCOPE-001 and accepted Foundation model/repository/service baseline. Next selects this package following exact-SHA CI refresh. No implementation or later task selection in this registration.

Workflow coverage: IDN-W36/37 direct effective permission resolution and protected company-level authorization, with a minimal own-scope entitlement consultation (part of IDN-W39) and one actual protected read of the existing initialized Identity catalog. Identity owns grants, roles, permission definitions, account/session eligibility and authorization decisions. Organization owns authoritative company eligibility; platform.security owns authenticated principal and route enforcement. No general authorization-check HTTP oracle, no cross-user search, no grant/role/group changes or seeded permissions.

Scope and semantics:
- Reuse UserRoleGrant, Role, RolePermission, Permission, Action, ResourceType and AuthorizationScope. A GLOBAL grant is company-wide, never application-wide. Match exact requested positive company OrganizationUnit ID; accept only exact GLOBAL with null scopeReferenceId. The request's companyId is the requested boundary, not evidence of access or company membership. The recipient's optional employeeId, username, actor role name, or token claims never confer scope. Only current eligible authenticated user/session with enabled LOCAL provider can receive an authorization decision. Resolve actor from the authenticated CurrentIdentity principal (not an HTTP actor parameter, header or JWT privilege claim) and revalidate using the existing SessionAuthenticationService inside the authorization read transaction. Internal calls accept that trusted principal only, not an arbitrary caller-supplied user ID.
- Use server-selected Instant per decision, UTC company date, half-open validity and revocation rules via existing isActiveAt, enabled roles and actual RolePermission links. Query only the user's grants for the requested company; prefilter candidates by time/role/scope as useful and enforce exact case-sensitive shape and enabled role in Java (MySQL collation cannot define security equality). Resolve exact stable permission code and action/resource pair, validate the code equals resourceType.code + "." + action.code, deduplicate and deterministically sort. An exact permission match is required; no prefix, wildcard, implicit role hierarchy or implicit privilege from grant actor. Never consider grantedBy account eligibility a condition of an otherwise valid grant; approved bootstrap attribution stays disabled.
- IDN-03 implements direct grants only. The earlier pilot package IDN-10 owns group membership/inherited role evaluation; GroupRoleGrant and GroupMembership do not confer runtime authority yet. REGION, PIPELINE, INFRASTRUCTURE grants remain stored but cannot grant access here; even matching scalar IDs or a GLOBAL grant cannot authorize a real engineering resource without an authoritative resource-owner coverage contract. No invented target owner or permissive fallback. User account company ownership and multi-company administrative mutation semantics remain unresolved for IDN-05/08; this task does not infer them or expose account management.
- Introduce Organization-owned isEligibleCompanyUnit(long, LocalDate) read-only boolean contract for Identity authorization. Fetch the unit, type and status through its own repository, check exact COMPANY code, active type, ACTIVE status and half-open validity. No entity crosses module boundary. Preserve the classification-only and initialization-locking contracts. Invoke the new contract only after a matching effective direct permission candidate exists (for a specific protected operation, after the exact required permission matches), so users without authority cannot use Organization lookups to probe protected company existence. Unknown, non-company, inactive, expired, future, retired-type and unauthorized company are indistinguishable at HTTP denial. No company name or other protected metadata returned.
- EffectiveAuthorizationService exposes a trusted internal read decision/require contract for company-level capabilities plus own effective company permission codes. A trusted owner selects the required exact permission; no endpoint accepts arbitrary permission checks or user IDs. Evaluate session, grants, role-permission links and Organization eligibility in one explicit fresh read transaction (repeatable-read consistent snapshot for standalone reads against the current single MySQL database). No persistent/cached entitlements, JWT authorization claims or cross-request permission reuse. For the actual catalog read, authorization and catalog fetch must occur in the same transaction; do not disclose data, counts, existence or query the catalog before the gate. A later protected write must specify its own locking/serialization and revocation race contract and re-evaluate authority in that write transaction; IDN-03 read proof is not authorization atomicity for future mutations. No long-lived decision or historical authorization replay.
- GET /hyflo/api/v1/me/permissions?companyId=<positive-id> returns CompanyPermissionsResponse {companyId, permissionCodes} sorted unique exact definitions only for the requesting principal within an eligible company with at least one effective direct permission. It is an entitlement view, not a promise that every listed operation is implemented or a downstream resource is authorized. Missing/no valid permission or unavailable company returns generic 403 without existence metadata. No caller-supplied actor. GET /hyflo/api/v1/identity/user-statuses?companyId=<positive-id> requires exact identity-catalog.read on that eligible company and returns a deterministic code-sorted list of UserStatusResponse {id, code, displayName}, existing persisted catalog values only. The catalog is shared reference data; company-scoped authority authorizes this specific read, not ownership of the shared catalog or permission to edit it. No new classification seeds or REST CRUD.
- Add explicit AuthorizationController and mapper; use existing safe AuthenticationProblems and response conventions (400 INVALID_REQUEST for missing/nonpositive/malformed companyId, 401 for absent/invalid live authentication, 403 ACCESS_DENIED for missing grant/permission or ineligible company, 503 IDENTITY_UNAVAILABLE for failed persistence/decision). No internal exception or existence disclosure, no-store for protected responses. Admitted GET routes only, preserving all previous login/health/me/docs behavior and deny-all remainder. No business access solely because the route is authenticated; application must gate read before accessing catalog. No blanket /identity/** rule, no roles in SecurityContext or permission claim changes. No generic CRUD.
- Explicit repository additions only: candidate direct grants by user/company and role permission associations for selected role IDs, user status ordered read, Organization eligibility fetch. No generic CrudRepository expansion, model/table changes, new dependencies, migrations, configuration changes or existing scaffold-method test relaxation. Existing authentication, bootstrap and scope contracts remain intact. EffectiveAuthorizationService and IdentityCatalogService are focused workflow orchestration, not replacements for entity-specific skeletons.
- Do not duplicate Audit ownership or extend AuthenticationEvent (login events only). This read stage supplies safe request-local decisions and correlation in errors; no durable AuthorizationDecision or audit persistence is claimed. Audit/evidence for sensitive mutations and security-history access must be registered with their respective packages. No actual production operation, industrial resource authority or deployment.

Implementation commit: feat(identity): enforce effective company-scoped direct permissions

Exact create:
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/EffectiveAuthorizationService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/IdentityCatalogService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/api/controller/AuthorizationController.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/api/response/CompanyPermissionsResponse.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/api/response/UserStatusResponse.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/api/mapper/AuthorizationMapper.java`
- `src/test/java/dz/sh/hyflo/IdentityAuthorizationTest.java`
- `src/test/java/dz/sh/hyflo/IdentityAuthorizationIT.java`

Exact update:
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/UserRoleGrantRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/RolePermissionRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/UserStatusRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/infrastructure/persistence/repository/OrganizationUnitRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/organization/application/service/OrganizationUnitScopeService.java`
- `src/main/java/dz/sh/hyflo/platform/security/IdentitySecurityConfiguration.java`
- `docs/ARCHITECTURE.md`
- `docs/MODULES.md`

Delete: none.

Acceptance:
- Retain all existing 13 unit and 12 real-MySQL integration tests and mandatory CI evidence. New IdentityAuthorizationTest exercises exact-match/duplicate/temporal/scope resolution and verifies that denied/mismatched requests never call company owner or catalog reads (including repository/owner failures fail closed). New IdentityAuthorizationIT runs a separate isolated MySQL 8.4 servlet context, actual bootstrap + login and actual HTTP calls, closes context cleanly.
- Positive: bootstrap admin with exact company GLOBAL grant returns the initial 15 unique sorted permission definitions and can read persisted four UserStatus rows; direct grants can add distinct exact permissions through synthetic fixtures, duplicates collapse; a valid direct grant in another company permits only that company; no need for employee link. No authentication secrets, grants from other companies, actor internals or unrelated catalog data in DTO.
- Negative: anonymous/invalid or revoked session, suspended/disabled user or provider; no grant, wrong company, non-company, unknown, inactive/expired/future/retired type company even with a synthetic grant; wrong permission, case mismatch/malformed code-pair, disabled role, grant future, validTo/revokedAt equality, expired/revoked grant; group-only membership/grant and non-GLOBAL scope never allow. Verify exact code equality independent of DB collation, current permission link removal and role/grant revocation immediately affect subsequent requests with same JWT. Missing, malformed and nonpositive company selector gets safe 400. Unauthorized/nonexistent/ineligible target results have same 403 code/detail; test denied paths never read catalog/owner when no applicable candidate. No admin CRUD, arbitrary authorization oracle or engineering route opens; authenticated existing OpenAPI read still succeeds and runtime OpenAPI contains only newly admitted business paths plus prior contracts.
- Verify transaction scope, same-company query criteria and owner contract, no stale cache, database failure denies without returning partial catalog/entitlement data. Test data is synthetic and any intentionally inconsistent rows are isolated fixtures; no production data assumptions. No extra mandatory test skips, weakening existing gates, or deployment.

Registered design choices for owner review: direct grants only in IDN-03 (inherited evaluation IDN-10), GLOBAL company-only effective read; own permissions and one actual protected catalog read; 403 indistinguishable denial; UTC date/snapshot semantics; unsupported target scopes deny. IDN-04 remains the proposed next package after IDN-03 acceptance (refresh rotation and session revocation). This registration does not execute IDN-03 or IDN-04.

### IDN-03 acceptance — 2026-11-25

Status: COMPLETED — implementation SHA 456a934a378cbee2e14b5299693f9e509a649051, [HyFloAPI Build #37 attempt 1](https://github.com/CHOUABBIA-AMINE/HyFloAPI/actions/runs/45591762398) success. Verify job 139127488083: Surefire 17/17, Failsafe 13/13 (HyFloApiIT 10, IdentityAuthenticationIT 1, IdentityBootstrapIT 1, IdentityAuthorizationIT 1), zero failures/errors/skips. Mandatory test/JaCoCo/SBOM checks, verification artifact and JAR upload passed; optional security scan skipped. Accept the registered direct company-level read scope only; no resource coverage, inherited grants or protected writes. Prior entries preserved.

## IDN-04 — Refresh rotation and own-session revocation

Status: IN PROGRESS — selected by owner Next (IDN-004 denotes the existing IDN-04 candidate), 2026-11-25. Base 456a934a378cbee2e14b5299693f9e509a649051; Build #37 passed. Dependencies: accepted IDN-01/02/03. One task, no split; reuse existing LoginSession, RefreshToken, AuthenticationEvent and stateless access JWT. No new tables, columns, schema changes, migration, dependencies, external identity, admin session management, or production deployment.

Registration commit: docs(identity): accept direct authorization and register session lifecycle

Implementation commit: feat(identity): rotate refresh tokens and revoke own sessions

Semantics and scope:
- POST /hyflo/api/v1/auth/refresh with explicit JSON RefreshRequest {refreshToken}, no access token required (client must omit Authorization when access expired; existing filter behavior unchanged). Accept only canonical unpadded base64url encoding of 32 bytes, no raw value in logs/DTO toString, persist SHA-256 hex only. Apply existing bounded global and peer-IP authentication limiter budget (shared with login) before database lookup; do not retain token/digest as a limiter key. Preserve login username bucket semantics and existing safe 429 error code for compatibility. Unknown valid-shaped token is generic 401, invalid shape 400; no owner or session disclosure.
- Rotation within one READ_COMMITTED write transaction: use scalar digest lookup for session ID and scalar session owner ID, then pessimistic User -> LoginSession -> RefreshToken locks in that order, check exact digest, owner/session consistency, live eligible LOCAL account and half-open session interval with server time sampled after locks. Never trust a caller actor/session/company ID. Consume old token, insert successor digest, issue signed access JWT with original session expiry (no sliding extension), append REFRESH_SUCCEEDED evidence and return existing TokenResponse only after transaction commits. Keep old consumed rows for replay detection; existing access JWTs remain valid until expiry or session revocation. No new session on rotation. Concurrent login uses the existing User lock; future account lifecycle tasks must follow the same lock ordering; no claim of global atomicity with unimplemented external account/provider changes.
- Strict replay policy: when a consumed known token is presented for a currently active eligible session, revoke that entire session and append one REFRESH_REPLAY event in the same transaction, then return generic 401 as a normal rejected outcome (not an exception that rolls back revocation). Concurrent duplicate refresh yields at most one success; replay may immediately invalidate the successful response's session. Clients must serialize refresh; no grace window or idempotent token reissue. Inactive/expired/unknown requests create no new records, no replay event duplication. Any write/signing/evidence failure rolls back and returns safe 503; no partial credentials or success response.
- POST /hyflo/api/v1/auth/logout with valid live bearer and no target selector. Require CurrentIdentity from authenticated context, recheck same owner and current eligible session under User -> LoginSession locks, revoke current session and append existing LOGOUT event atomically. Return 204/no-store; subsequent bearer and all session refresh tokens denied. An already revoked/expired session or invalid bearer returns 401; other sessions remain unaffected. No cross-user or all-session revocation.
- Keep AuthenticationEvent existing physical schema and stable login codes; add technical REFRESH_SUCCEEDED and REFRESH_REPLAY event constants requiring attributable user; use server correlation IDs, no token data in evidence. Preserve existing DTOs, JWT claim structure, authorization rules, repository/service skeletons and deny-all remainder. No generic CRUD or new permissions. No CSRF change, cookies or server HTTP session. No assumption of distributed rate limiting or production TLS.
- Update architecture/module documentation with the actual implemented boundary only, no claim of global write authorization.

Exact implementation allowlist:

Create:
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/api/request/RefreshRequest.java`

Update:
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/application/service/AuthenticationService.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/api/controller/AuthenticationController.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/domain/model/AuthenticationEvent.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/UserRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/LoginSessionRepository.java`
- `src/main/java/dz/sh/hyflo/modules/foundation/identity/infrastructure/persistence/repository/RefreshTokenRepository.java`
- `src/main/java/dz/sh/hyflo/platform/security/LoginAttemptLimiter.java`
- `src/main/java/dz/sh/hyflo/platform/security/IdentitySecurityConfiguration.java`
- `src/test/java/dz/sh/hyflo/IdentityAuthenticationTest.java`
- `src/test/java/dz/sh/hyflo/IdentityAuthenticationIT.java`
- `docs/ARCHITECTURE.md`
- `docs/MODULES.md`

Delete: none.

Acceptance:
- Retain existing 17 unit and 13 real MySQL integration methods, extend existing IdentityAuthenticationTest and IdentityAuthenticationIT (same disposable MySQL 8.4 context; preserve existing checks). Unit: canonical token shape/redacted DTO, global/IP shared budget, existing username limit preserved, finite event codes. Actual HTTP: initial login -> rotate without bearer -> same user/session, distinct successor/digest, unchanged session expiry, original token consumed timestamp, one success event; access JWT bounded by session expiry, no plaintext/digest leakage beyond intended opaque response, no cookies/cache. Unknown/malformed/missing, invalid bearer, suspended/disabled user/provider, expired/revoked sessions denied; no unintended record changes. Verify runtime OpenAPI includes exactly the two admitted authentication additions and prior routes remain protected.
- Real MySQL concurrent duplicate refresh (two simultaneous HTTP requests): one 200 and one 401, exactly one successor and one replay event, revoked parent; all old/new access and refresh credentials denied afterwards. Concurrent refresh/logout: serialized parent mutation, no active session/successful post-revocation refresh, no duplicate successor or logout event, previously issued access denied after commit. Independent session survives. Explicit rollback trigger on evidence insert: rotation preserves unconsumed old token and no successor, replay failure preserves session and no replay event, logout failure preserves live session, all return 503; retry after trigger removal succeeds or denies according to policy. Exercise own-session logout and no arbitrary user/session target; no new authorization surface.
- Exact-SHA HyFloAPI Build mandatory with no skips/failures, real MySQL and retained test/SBOM/JAR evidence. Source inspection is not runtime proof. Optional security scan separate. No acceptance recorded before CI.
- Next candidate after acceptance: IDN-05 (account-management ownership/authorization to be separately registered; do not execute here).

Owner-review design choices: strict session revocation on replay including duplicate concurrent requests; fixed original session lifetime, existing access JWT validity until expiry/revocation, authenticated current-session logout only, shared login/refresh global and IP budget with existing error code. These are bounded implementation decisions, not previously explicit owner security policy or industrial acceptance.

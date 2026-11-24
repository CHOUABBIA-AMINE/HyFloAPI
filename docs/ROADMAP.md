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

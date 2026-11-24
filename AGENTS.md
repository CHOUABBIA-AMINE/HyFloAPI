# HyFloAPI Agent Instructions

**Status:** ACTIVE — owner-selected initial governance, 2026-11-24.
**Scope:** HyFloAPI only. HidraAPI remains independent and retains its own architecture and roadmap.

## 1. Project identity and authority

| Field | Decision |
|---|---|
| Product | HyFlo — Hydrocarbon Flow Management Platform |
| Purpose | Development-only functional validation of hydrocarbon transportation workflows |
| Package root | `dz.sh.hyflo` |
| Architecture | Single Maven modular monolith, Modular Layered Architecture |
| Java / Spring Boot | Java 25 / Spring Boot 4.2.0 |
| Maven | Wrapper 3.3.4, distribution 3.10.2 |
| Persistence | MySQL, Spring Data JPA/Hibernate, one JPA business model |
| Authentication | LOCAL only; preserve server-side authorization |
| Schema | Hibernate update in dev, create-drop in isolated test, validate by default; Flyway deferred |
| Task authority | [docs/ROADMAP.md](docs/ROADMAP.md) |

Use the owner-approved scope, this file, [architecture](docs/ARCHITECTURE.md) and [module ownership](docs/MODULES.md). The roadmap is the sole active task register. Current source at an exact SHA, configuration, tests, generated OpenAPI and exact-SHA CI establish implementation reality. Resolve conflicting instructions explicitly. External project docs and old HyFlo commits are references, not permission to import code or advance tasks. Never claim package presence proves a working module.

## 2. Task selection and publication

- `Next` selects one eligible registered task after refreshing live main, its parent/tree, relevant history, and exact-SHA CI. `Fail` repairs the responsible failed task first. No automatic advancement.
- Read applicable source, configuration, workflows and tests. Before edits, register the exact file allowlist (create/update/delete), dependencies, acceptance and commit message in the sole roadmap. A candidate with an incomplete envelope is not executable.
- Execute one whole task; no A/B/C split without explicit owner approval. At most one approval commit when needed, one documentation commit, and one implementation commit per task. An in-scope CI repair may require a separate corrective commit; do not disguise it as a new feature or routine extra approval. Record why.
- Publish only to existing `main`, with expected-head guard and non-force fast-forward. No branches, PRs, history rewrites, releases, tags or deployment unless explicitly authorized. If main advances, inspect and reconcile before retrying.
- Verify changed paths, parent, tree, blobs and message. Preserve unrelated files. Confirm applicable CI created/queued/started on the published SHA and stop; do not wait for completion. If not triggered, report the actual limitation and Actions > HyFloAPI Build > Run workflow > main steps.
- Mark acceptance only from actual evidence; a pending or skipped database check is not a pass. No invented source, tests, CI, endpoints, schemas or release evidence.

## 3. Architecture and naming

Use lowercase packages and exact registered set/module names, including `operation`, `assets`, `documents`, `leakdetection`. Do not silently rename. Keep `HyFloApi.java` at the root for default component scanning. Use `bootstrap`, `kernel`, `platform`, `modules.<set>.<module>`. Do not introduce generic dumping grounds named common, core, shared, utils or helpers.

Inside a business module use api/controller, request, response, mapper; application/service; domain/model; infrastructure/persistence/repository. One JPA-annotated business model is intentional. Do not impose Hidra's separate persistence entity, repository port, adapter or persistence mapper. Application may use its own Spring Data repository directly. Domain owns invariants and does not import API, application, repository or platform implementations. No automatic module-level enumeration, exception or domain service packages. Keep services in application/service and exceptions centralized. Introduce extra packages only for an approved concrete requirement.

Kernel uses Java standard library only; no Spring, JPA, Hibernate, Jackson, platform or modules. Platform provides technical mechanisms, not business records. Controllers never access repositories. Module-to-module access uses explicitly published owner service contracts and IDs/DTOs, never private repositories, entities or controllers. Register concrete contracts and avoid cyclic module dependencies. Avoid cross-module JPA aggregate coupling.

Use explicit REST request/response DTOs, Bean Validation and MapStruct where appropriate. Do not serialize JPA entities. Use Lombok judiciously: prefer getters and required constructors, avoid blanket setters, @Data, recursive toString/equals/hashCode, and unrestricted builders on mutable entities. Enforce invariants via named model operations, not arbitrary setter chains.

Use the existing HyFlo project/product header convention for new Java files; accurate package, type, description and layer, truthful authorship and dates. Preserve existing attribution and creation dates when editing. Use descriptive JavaDoc in package-info.java as in the accepted skeleton. Do not retrofit headers across unrelated files. Do not create placeholders or empty packages outside an expressly selected structural task.

### Physical database naming — mandatory

Preserve the historical HyFlo numeric physical identifiers. Logical Java class/field names remain descriptive; explicit JPA names are mandatory for all persistent columns (including IDs, versions and join columns), tables, unique constraints and foreign keys.

| Object | Format | Example |
|---|---|---|
| Entity table | T_NN_NN_NN | T_00_02_02 (User) |
| Primary key | F_00 | User.id |
| Business column / FK column | F_NN | F_01, F_08 |
| Technical optimistic-lock version | A_01 (historical GenericModel convention) | User.version |
| Unique constraint | <table>_UK_NN | T_00_02_02_UK_01 |
| Foreign key | <table>_FK_NN | T_00_02_02_FK_02 |
| Historical many-to-many join table | R_TNNNNNN_TNNNNNN | R_T000202_T000203 |

Use uppercase physical identifiers, two-digit numeric components, explicit referencedColumnName=F_00 for entity-ID joins, and explicit constraint names. Preserve established IDs and field meaning; never recycle retired table/column/constraint numbers or allocate colliding identifiers. New identifiers must be checked against current and historical source, registered in the architecture inventory, and not silently renumbered. Preserve identifier case via the configured Hibernate standard physical naming strategy. Test-only probe tables are not business-schema names. Where a referenced module is not implemented yet, a scalar ID is not evidence of an actual FK; the owner integration must add the proper constraint when selected.

Business classifications/statuses/providers use classes and finite database-managed reference rows, not Java enum declarations or @Enumerated. JPA annotation enums (e.g., FetchType and GenerationType) are framework APIs, not business classifications. Keep immutable stable codes and controlled transitions independent of labels. Seed approved reference values only in a separately selected task; synthetic test fixtures may create rows now. Server-side translation is the target; a single displayName field is not a completed localization workflow.

## 4. Data, localization and security

- Database-owned business classifications use stable unique codes, referential integrity, lifecycle and localized labels; no business enum catalogs on either client or server. Internal finite technical constants and hard safety/transition rules are not configurable by editing a label. Reference owns shared catalogs; module-specific types stay with their owner.
- Server resolves requested locale and deterministic fallback. Return stable IDs/codes and localized labels; labels never drive rules, queries or authorization. Centralize generic exceptions in kernel.exception and HTTP handling/message resolution in platform.exception and platform.localization. No raw exception, SQL, stack trace or secret disclosure.
- Use local authentication only; no LDAP, AD, OIDC or external identity provider without approval. Identity owns accounts, hashes, roles, permissions and grants; platform.security enforces technical mechanisms. Derive actor from authenticated context, not caller-provided headers. Check company/resource authority before existence, counts, exports, history or writes. Default deny, least privilege, protected data and independent approval where required.
- Transactions belong in application services. Use DB constraints and concurrency controls as appropriate, preserve decision and engineering revision history, and record units, timestamps and source evidence. Never silently substitute current inputs for historical revisions. Never expose real credentials or production data; externalize DB and signing secrets, rotate any previously exposed real value. Use synthetic fixtures only.
- Dev Hibernate update is permitted for disposable data; test create-drop only on isolated MySQL containers. Default validate is safe. No Flyway or migration directories until separately selected production preparation, before first production deployment. No destructive reset of a non-disposable database.
- Gas-first GZ2 synthetic pilot, own engine future steady/transient direction, versioned inputs and accountable human decisions. No live SCADA access, autonomous control, industrial calibration, production deployment or numerical solver activation inferred or authorized. Distinguish evidence, analysis, recommendation, decision and observed outcome.

## 5. Verification and reporting

Run applicable checks with available tools; do not provision a full local toolchain merely to duplicate hosted CI. Commands from repo root: `./mvnw test`, `./mvnw clean verify` (Windows: mvnw.cmd). Java 25 and Docker are required for full runtime verification. Surefire unit tests, Failsafe real MySQL IT, JaCoCo and CycloneDX remain enabled; do not weaken gates or accept zero tests or silent skips. Architecture enforcement is planned, not already present.

Use one focused functional scenario per new workflow: exercise actual entry point, authorization (positive/negative), persistence/readback, applicable rollback/concurrency and invariant failures. Reuse existing coverage; add separate cases where a guarantee cannot be established by the scenario. Run existing exact-SHA CI as overall acceptance gate. Optional OWASP scan requires NVD_API_KEY; ordinary CI does not prove scan acceptance. Generated OpenAPI reflects runtime only; do not fabricate endpoints or snapshots.

Record status as PLANNED, IN PROGRESS, BLOCKED, COMPLETED, DEFERRED or SKIPPED. Keep previous evidence append-only in ROADMAP.md. Distinguish implemented, CI-passed, software accepted, engineering qualified and operationally accepted. A CI-passed development fixture is not SITE or production acceptance.

Final task report: stage/ID, exact created/updated/deleted scope, implementation, validation and limitations, commit SHA/link, exact-SHA CI state/link, remaining acceptance gate and next registered task without executing it. Do not make a version bump per task; releases require separately authorized milestones and exact-SHA green CI.

## 6. Reference provenance

Adapted from [HidraAPI AGENTS.md](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/AGENTS.md) and [its active functional roadmap](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/roadmap/ROADMAP.md), with HyFlo-specific decisions documented in [ARCHITECTURE.md](docs/ARCHITECTURE.md) and [MODULES.md](docs/MODULES.md). Hidra-specific task IDs, Java 21, PostgreSQL, Flyway, strict ports/adapters, enterprise identity and production operations are not imported as HyFlo requirements.

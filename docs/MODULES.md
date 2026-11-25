# HyFloAPI Functional Module Register

**Status:** ADOPTED package classification and ownership design, 2026-11-24.
**Implementation reality:** all 25 roots and four internal layers exist as package-info documentation only at [e513487](https://github.com/CHOUABBIA-AMINE/HyFloAPI/commit/e5134870a11469a77eb5bf0e1d81fb2455ef3e74); no business lifecycle is implemented. This register describes intended ownership, not a current entity or API inventory.

## 1. Six functional sets

| Ring | Set | Modules | Count |
|---|---|---|---:|
| C1 | Foundation | infra, identity, organization, reference | 4 |
| C2 | Coordination | configuration, audit, workflow, notification, integration | 5 |
| C3 | Engineering | topology, assets, party | 3 |
| C4 | Operation | telemetry, monitoring, planning, custody, documents, reporting | 6 |
| C5 | Safety | alarm, incident, integrity, leakdetection, hse | 5 |
| C6 | Intelligence | analytics, risk, simulation | 3 |
| | Total | | 26 |

The exact Java package pattern is `dz.sh.hyflo.modules.<set>.<module>`. Set names are lowercase singular. Preserve existing assets/documents spellings; no rename has been selected. Each root follows [ARCHITECTURE.md](ARCHITECTURE.md). A ring is not a dependency graph or deployment boundary.

## 2. Module responsibility and exclusions

### C1 — Foundation

| Module/package suffix | Authoritative responsibilities | Ownership boundary |
|---|---|---|
| [foundation/infra](../src/main/java/dz/sh/hyflo/modules/foundation/infra) | Shared file identity, metadata and storage references. | Identity, Party, Topology and Documents refer to files by stable ID. |
| [foundation/identity](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/foundation/identity) | Local users, credentials, principals, roles, permissions, grants and authorization evidence. | Organization supplies employee/company scope; platform.security implements enforcement. No external identity provider. |
| [foundation/organization](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/foundation/organization) | Organizational units (including companies classified by OrganizationUnitType code COMPANY), employees, positions, assignments, hierarchy and responsibilities/operational scope. | Owns internal organization; uses Reference for shared geography, not identity credentials or external party profiles. |
| [foundation/reference](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/foundation/reference) | Shared countries/geography, units, languages, common catalogs and localized reference labels. | HyFlo-specific additional module; owns only shared catalogs. Module-specific classifications remain in their owner; platform.localization resolves application messages. |

### C2 — Coordination

| Module/package suffix | Authoritative responsibilities | Ownership boundary |
|---|---|---|
| [coordination/configuration](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/coordination/configuration) | Typed business settings, definitions, values, effective versions and feature flags. | No secrets or Spring wiring; settings cannot silently change historical engineering inputs. |
| [coordination/audit](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/coordination/audit) | Audit events, access records, before/after evidence, actor/time and audit export. | Workflow owns process transitions; audit does not authorize or mutate owner records. |
| [coordination/workflow](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/coordination/workflow) | Workflow definitions, steps, assignments, tasks, controlled transitions, decisions and state history. | Records participant and exact revision; owning module enforces final business invariant; audit retains cross-cutting evidence. |
| [coordination/notification](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/coordination/notification) | Templates, notification requests, messages, recipients, delivery attempts and acknowledgments. | Delivery status is not workflow approval or alarm acknowledgment; no external delivery claim without proof. |
| [coordination/integration](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/coordination/integration) | External-system registration, exchanges, import/export jobs and rejected/dead-letter records. | Technical connectivity belongs to platform.integration; owners validate ingested business data. No live OT assumption. |

### C3 — Engineering

| Module/package suffix | Authoritative responsibilities | Ownership boundary |
|---|---|---|
| [engineering/topology](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/engineering/topology) | Pipeline systems, pipelines, segments, nodes, supply/delivery connectivity and versioned hydraulic network definition. | Owns network identity and connectivity; references Party supplier/receiver and Assets physical installation IDs. No duplicate equipment master. |
| [engineering/assets](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/engineering/assets) | Physical installations/stations, equipment, specifications, lifecycle, maintainable assets, condition records and work orders. | HyFlo-specific ownership of physical asset records; topology owns network projection, Integrity owns defect assessment, no direct foreign work-order creation. |
| [engineering/party](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/engineering/party) | External suppliers, customers, shippers, contractors, party identity/profile and role assignments. | Distinct from operator organization/employee identity; references from topology and custody identify accountable counterparties. |

### C4 — Operation

| Module/package suffix | Authoritative responsibilities | Ownership boundary |
|---|---|---|
| [operation/telemetry](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/operation/telemetry) | Sources, points, effective bindings, raw readings, qualified readings, units, timestamps and data quality. | Topology/assets provide source context; preserve raw provenance and qualification. No monitoring rule ownership. |
| [operation/monitoring](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/operation/monitoring) | Operating rules, limits/evaluations, plan-versus-actual deviation evidence. | Consume approved targets and trusted observations; do not overwrite telemetry or automatically actuate equipment. |
| [operation/planning](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/operation/planning) | Planning periods, operational plans, revisions, targets and nominations. | Owns authorized targets and historical versions; Workflow approves exact revision, Monitoring consumes accepted target. |
| [operation/custody](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/operation/custody) | Custody measurement periods, transfer tickets, quantities, discrepancy and reconciliation records. | References metering/party and evidence; does not replace raw telemetry. Classified Operation, not Safety. |
| [operation/documents](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/operation/documents) | Documents, versions and governed target links. | Owns document revisions and access scope. References Foundation Infra file IDs and Workflow decisions. |
| [operation/reporting](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/operation/reporting) | Report definitions, requests, runs and generated output-artifact metadata. | Uses scoped owner evidence; Analytics owns computed insights and Documents owns controlled document versions. |

### C5 — Safety

| Module/package suffix | Authoritative responsibilities | Ownership boundary |
|---|---|---|
| [safety/alarm](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/safety/alarm) | Alarm lifecycle, severity, acknowledgment, closure, shelving and suppression evidence. | Consumes admitted monitoring or leak evidence; deduplicate and preserve actor authority; no direct OT control. |
| [safety/incident](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/safety/incident) | Incident lifecycle, related cases, response actions, investigation and closure evidence. | Not every alarm is an incident; explicit escalation and authorized closure. |
| [safety/integrity](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/safety/integrity) | Integrity programs, assessments, inspection evidence, pipeline defects, degradation and integrity cases. | References asset/topology identity; Assets owns maintenance work orders, Integrity owns assessment and recommendations. |
| [safety/leakdetection](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/safety/leakdetection) | Suspected leak candidates, localization evidence, detection cases and escalation links. | Consumes eligible telemetry/monitoring and topology context; no certified detection or autonomous response inferred. |
| [safety/hse](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/safety/hse) | HSE cases, corrective/preventive actions, closure, compliance and permit-to-work records. | Owns HSE lifecycle, not general asset records or quantitative risk model; no safety-system certification. |

### C6 — Intelligence

| Module/package suffix | Authoritative responsibilities | Ownership boundary |
|---|---|---|
| [intelligence/analytics](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/intelligence/analytics) | Curated datasets, metric/projection runs, trends, insights and digital-twin readiness evidence. | Consumes qualified owner data; does not mutate measurements, plans or asset truth; disclose assumptions and quality. |
| [intelligence/risk](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/intelligence/risk) | Risk registers, assessments, matrix cells, evidence links, likelihood/consequence and prioritization. | Version method and evidence; HSE owns cases, Integrity owns defects; no invented risk thresholds. |
| [intelligence/simulation](https://github.com/CHOUABBIA-AMINE/HyFloAPI/tree/e5134870a11469a77eb5bf0e1d81fb2455ef3e74/src/main/java/dz/sh/hyflo/modules/intelligence/simulation) | Versioned models/scenarios, run provenance, optimization candidates and human-facing recommendations. | Scenario preparation initially only; internal numerical engine and external solver adapters deferred until explicit selection. No live control. |


## 3. High-risk ownership decisions

| Boundary | Decision |
|---|---|
| Identity / Organization | Account identity and credentials versus employment, scope and organizational responsibilities. Users need not be employees; grants must be explicitly scoped. |
| Reference / Configuration | Catalog identity and translations versus typed effective business settings. Changing labels never changes behavior; changing setting versions never rewrites history. |
| Organization / Party | Operator's organizational responsibility versus external commercial/operational actor. Do not duplicate a company merely to represent a party role without explicit identity mapping. |
| Topology / Assets | Topology owns connectivity and network/pipe records; Assets owns physical station and equipment technical identity. Use stable links rather than two independent masters. Exact ownership of any historical field must be checked against its meaning before import. |
| Telemetry / Monitoring | Raw/qualified observation versus rule evaluation/deviation. Monitoring must cite actual qualified evidence and applicable approved target/limit. |
| Audit / Workflow | General action/decision provenance versus process definition and controlled state transition; Workflow must record who decided. |
| Alarm / LeakDetection / Incident | Alarm lifecycle versus suspected leak case versus actual incident investigation; explicit escalation, no implicit equivalence. |
| Assets / Integrity / HSE | Physical asset and maintenance versus condition/defect assessment versus HSE case/permit. |
| Custody / Telemetry | Governed transfer accounting versus source measurement; retain references and uncertainty, not competing raw truth. |
| Documents / Reporting / Analytics | Controlled document versions versus report execution/output metadata versus dataset/metric/insight. |
| Risk / Simulation | Risk evaluation versus scenario, run and recommendation; neither owns operational control. |

## 4. Candidate collaboration sequence

These are proposed consumers, not implemented exported contracts or a universal dependency allowlist.

| Consumer | Owner prerequisites / contract intent |
|---|---|
| Identity | Organization scope/employee eligibility, Reference language |
| Topology | Organization ownership, Party supply/delivery counterparty, Assets physical references |
| Assets | Organization ownership, Party manufacturer/supplier, topology linkage by stable ID |
| Planning | Topology/Assets eligible resource, Party nomination counterparty, Workflow approval |
| Telemetry | Topology/Assets point association, Reference unit, Integration optional ingestion |
| Monitoring | Planning target, Telemetry qualified observation, engineering baseline |
| Alarm / LeakDetection | Monitoring evaluation, eligible observation and topology; authorized escalation to Incident |
| Integrity / HSE | Assets and Topology references; Workflow decisions and Incident links where applicable |
| Custody | Party, Telemetry and governed measurement period |
| Reporting | Approved owner reads, Documents artifact link, Analytics metrics where applicable |
| Analytics / Risk | Approved owner evidence and versioned engineering context |
| Simulation | Published topology/asset inputs, qualified measurements, planning targets and explicit scenario inputs |
| Workflow / Audit / Notification | Explicit target/actor contracts; avoid cyclic service graphs and private repository access |

FDN-SCOPE-001 admits the Organization-owned OrganizationUnitScopeService.requireCompanyUnit(long organizationUnitId) contract to Identity application only. It returns normally for a persisted unit whose type code is exactly COMPANY and throws the same IllegalArgumentException for missing, invalid or non-company IDs. No JPA entities are exchanged, no HTTP exposure; the invoking use case must independently authorize the caller before using this internal check. Identity's AuthorizationScopeService validates the boundary with that contract. Full protected grant operations and resource-ownership evaluation remain deferred.

IDN-01 admits OrganizationUnitScopeService.requireEligibleCompanyUnit(long organizationUnitId, LocalDate effectiveDate) for the trusted Identity initialization transaction only. Organization locks/reads its own unit and checks exact COMPANY type, active type, ACTIVE status and half-open date validity; no entity crosses the module boundary. The non-HTTP deployment operator command is the trust boundary, not an authenticated User. It fails closed for missing or ineligible units and never provisions Organization data. Identity owns the atomic initialization transaction and the narrowly approved disabled SERVICE attribution exception. No normal grant operation may use this exception.

IDN-02 admits Identity-owned SessionAuthenticationService.resolve(long userId, long sessionId, Instant now) to platform.security after cryptographic verification. It returns an immutable CurrentIdentity (own user ID, username, session ID) only for a live session of an active eligible LOCAL account, otherwise empty; database failure denies access. No JPA entity, repository, credential or grant is exchanged. The contract is a per-request authentication check, not effective permission or company/resource authorization. Platform security owns signing, verification, technical throttling and request enforcement; Identity owns the login transaction and security evidence. No other module or endpoint gains authority through this contract.

IDN-03 admits Organization-owned OrganizationUnitScopeService.isEligibleCompanyUnit(long organizationUnitId, LocalDate effectiveDate) to Identity's direct company read authorization only. It must join the existing authorization transaction, returns boolean for exact COMPANY classification, active type, ACTIVE status and half-open effective dates; no entity crosses the boundary. Identity calls it only after finding an applicable exact permission (or at least one effective permission for the caller's own entitlement view). No company existence disclosure, resource coverage claim or bootstrap mutation. EffectiveAuthorizationService.requireCompanyPermission(CurrentIdentity principal, long companyId, String requiredPermission) is the internal Identity read gate: the trusted principal must match the authenticated context and a live session, and the caller selects a fixed company-level permission. It denies or fails closed; it is not a public authorization oracle or a grant-management contract. IdentityCatalogService keeps the gate and read in one transaction. No additional owner consumer admitted for engineering resources or writes.

IDN-05 admits Organization-owned OrganizationUnitScopeService.lockEligibleCompanyForIdentityWrite(long organizationUnitId, LocalDate effectiveDate) to the already-authorized Identity account administration write transaction. It holds the authoritative company/type/status lock through commit and returns a boolean, not an Organization entity. Identity must resolve exact direct GLOBAL permission first and re-evaluate temporal authority after target lock acquisition. Identity owns each User's single immutable accountable company (scalar owningOrganizationUnitId), separate from multi-company operational grants. Its bounded user.read and user.create/activate/suspend/reactivate/disable workflows are company-owned; the reserved bootstrap attribution account is not administrable via HTTP. Company-local grants in other companies confer no authority over this account unless the selector equals its owning company. UserLifecycleEvent records the account transition, actor, target, reason, time, correlation and ownership in the same transaction; general Audit remains owner of cross-cutting audit and exports. Credential initialization/reset, grant administration, owner transfer and organizational employee linking are not admitted here.

IDN-06 extends Identity-owned UserAdministrationService with company-owned credential initialization and reset. It reuses IDN-05 locked authority, target owner and live actor/session checks, exact credential.initialize/credential.reset permissions and platform's existing IdentityPasswordEncoder. LocalCredential owns supported hash recognition and named replacement; Identity serializes password replacement and all-session revocation under the target User lock and appends UserLifecycleEvent with unchanged account status. No cross-module JPA models, new tables, credential read surface, self-service recovery, forced change, owner transfer or secret delivery.

IDN-07 extends Identity-owned UserAdministrationService with account-wide administrative live-session revocation for an owned account. The existing login-session.revoke capability is a company-scoped write, not a session discovery/read permission. It uses IDN-05 locked authority and User -> LoginSession serialization, and records SESSIONS_REVOKED in UserLifecycleEvent. No cross-module contract or schema changes; no secret, count, session listing or individual session selection. Subsequent eligible login remains possible.

IDN-08 extends Identity's company-scoped direct role grant workflow with bounded GLOBAL grant consultation, finite same-role delegation and exact selected grant revocation. UserRoleGrant owns half-open interval coverage and overlap logic; actor and recipient User locks serialize assignment/revocation with existing authorization. Administrative roles may be delegated only to accounts owned by the selected company; foreign recipients are restricted to explicitly classified catalog-read roles. Bootstrap-issued grant is protected, no self-mutation, no cascading revoke or cross-company account lifecycle change. Existing grant actor/time evidence is preserved without new schema. Role/permission definitions and resource/group scope changes remain separate.

A real contract is admitted per task with exact owner, consumer, inputs, result, authorization, scope and failure behavior. Never expose an unrestricted generic entity API. Owner module remains authoritative and validates mutations.

## 5. Source basis and intentional adaptations

Reference source: HidraAPI commit 007d2d246b244deb4afe9258ab66d19dc7ff2d6c, [domain ownership](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/domain/DOMAIN_OWNERSHIP.md), [bounded contexts](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/architecture/BOUNDED_CONTEXT_MAP.md), and per-module responsibility sections:
- [identity](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/identity.md)
- [organization](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/organization.md)
- [configuration](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/configuration.md)
- [audit](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/audit.md)
- [workflow](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/workflow.md)
- [notification](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/notification.md)
- [integration](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/integration.md)
- [topology](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/topology.md)
- [assets](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/assets.md)
- [party](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/party.md)
- [telemetry](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/telemetry.md)
- [monitoring](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/monitoring.md)
- [planning](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/planning.md)
- [custody](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/custody.md)
- [documents](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/documents.md)
- [reporting](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/reporting.md)
- [alarm](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/alarm.md)
- [incident](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/incident.md)
- [integrity](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/integrity.md)
- [leakdetection](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/leakdetection.md)
- [hse](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/hse.md)
- [analytics](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/analytics.md)
- [risk](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/risk.md)
- [simulation](https://github.com/CHOUABBIA-AMINE/HidraAPI/blob/007d2d246b244deb4afe9258ab66d19dc7ff2d6c/doc/modules/simulation.md)

HyFlo-specific decisions: new Reference module for shared server-localized catalogs (Hidra places administrative geography in Organization); Topology/Assets boundary assigns physical station/equipment records to Assets rather than copying Hidra's Topology physical identity ownership; Custody is in Operation; local-only Identity; physical set nesting and simplified layered internals. Source-derived ownership anchors are not automatic class/table/endpoint prescriptions. Historical HyFlo data model reuse, units, nullability, constraints and exact field ownership must be reviewed before selected implementation. No unsupported operational values are invented.

IDN-04 extends the Identity-owned AuthenticationService with refresh rotation and own-session logout using the same persisted LoginSession and RefreshToken models and existing platform token signing and live bearer verification. Refresh is a credential exchange, not a company permission check; logout derives the target exclusively from CurrentIdentity and revalidates under lock. No new cross-module contracts or foreign entities. Technical REFRESH_SUCCEEDED, REFRESH_REPLAY and LOGOUT events retain user/provider/correlation/time without token secrets. Strict replay revocation, original session expiry, transaction rollback and concurrency acceptance follow ROADMAP.md.


IDN-09 extends Identity-owned IdentityCatalogService and AuthorizationController with protected shared role, permission and role-bundle definition reads. Reuses the existing EffectiveAuthorizationService company gate before any definition query; bundle consultation requires both exact role.read and permission.read. No cross-module ownership changes, repository exposure, grant/member/actor projection, new permission, or mutation contract. Structural permission validity is not proof of route availability or effective access.


IDN-10 extends Identity-owned AuthenticationService and AuthenticationController with self-service local password change (original W18). The live CurrentIdentity is the exclusive subject; no selected company or administrative grant confers access to another credential. LocalCredential's existing named replacement and LoginSession revocation operations retain ownership and temporal invariants under the User serialization lock. Existing AuthenticationEvent stores attributable password-change success/failure evidence. No cross-module contract, role/grant mutation, new catalog or secret exposure.


IDN-11 extends Identity-owned AuthenticationService, AuthenticationController and LoginSessionRepository for own-session consultation and selective revocation (W16/W17). CurrentIdentity remains the exclusive subject; an administrative grant never authorizes selecting a foreign account's session here. AuthenticationEvent links selective revocation to the exact owned target LoginSession with the same revocation timestamp. No new cross-module contract, permission, account or grant mutation. Existing administrative all-session revocation and current-session logout are unchanged.


IDN-12 extends the existing Identity-owned UserAdministrationService, UserAdministrationController and UserRepository account list for W06. Optional literal username prefix, email equality, status and type filters intersect within the selected immutable owning company; existing direct GLOBAL user.read and eligible company contract are unchanged. No additional Organization contract, write operation, grant or employee discovery.

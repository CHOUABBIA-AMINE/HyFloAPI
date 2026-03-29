# HyFloAPI — Architecture Reference

> Last updated: 2026-03-29 — Frontend contract freeze (Commits 1–17)

---

## Top-Level Domains

| Domain | Root Package | Responsibility |
|---|---|---|
| `system` | `dz.sh.trc.hyflo.system` | Security, audit, notification, user/role/setting |
| `general` | `dz.sh.trc.hyflo.general` | Organisation, localization, employees |
| `network` | `dz.sh.trc.hyflo.network` | Pipeline network topology and assets |
| `flow` | `dz.sh.trc.hyflo.flow` | Flow monitoring domain (readings, events, operations, forecasts, workflow, intelligence) |
| `crisis` | `dz.sh.trc.hyflo.crisis` | Incident management domain (incidents, severities, impact analysis) |

> **`crisis` is an official top-level domain as of v2.** It is structurally equivalent to `flow` and `network`.

---

## Module Overview

### Flow

| Module | Package | Responsibility |
|---|---|---|
| `flow.type` | `dz.sh.trc.hyflo.flow.type` | Reference classifications (event type, operation type …) |
| `flow.common` | `dz.sh.trc.hyflo.flow.common` | Shared reference data (status, slot, threshold, data source) |
| `flow.core` | `dz.sh.trc.hyflo.flow.core` | Core reading entities and CRUD |
| `flow.workflow` | `dz.sh.trc.hyflo.flow.workflow` | Approval/rejection lifecycle, slot coverage |
| `flow.intelligence` | `dz.sh.trc.hyflo.flow.intelligence` | Anomaly, alert, forecast, monitoring analytics |

### Network

| Module | Package | Responsibility |
|---|---|---|
| `network.type` | `dz.sh.trc.hyflo.network.type` | Network reference classifications |
| `network.common` | `dz.sh.trc.hyflo.network.common` | Shared network reference data |
| `network.core` | `dz.sh.trc.hyflo.network.core` | Pipeline, segment, station, terminal, equipment |

### Crisis

| Module | Package | Responsibility |
|---|---|---|
| `crisis.common` | `dz.sh.trc.hyflo.crisis.common` | Severity reference data (`IncidentSeverity`) |
| `crisis.core` | `dz.sh.trc.hyflo.crisis.core` | Incident entity and impact analysis (`Incident`, `IncidentImpact`) |

#### Crisis API scope (v2 current)

| Capability | Status |
|---|---|
| Read incidents (`GET /crisis/incidents`) | ✅ Ready |
| Read incident by ID (`GET /crisis/incidents/{id}`) | ✅ Ready |
| Read severities (`GET /crisis/severities`) | ✅ Ready |
| Create / update / delete incidents | ⏳ Deferred (write API phase not started) |
| Incident workflow (declare → resolve → close) | ⏳ Deferred |
| Declared-by employee enrichment | ⏳ Deferred (no Employee FK on Incident yet) |

---

## Authority Format — Canonical Contract

> **The canonical authority format is `DOMAIN:ACTION`. No other format is valid.**

### Rules

1. Authority names use colon (`:`) as the domain–action separator.
2. Underscores (`_`) are allowed only inside a domain or action segment (e.g. `FLOW_READING:READ` is a granular permission, not a coarse authority).
3. Legacy underscore-only authorities (`FLOW_READ`, `FLOW_WRITE`) were removed in Commit 1. Do not reintroduce them.
4. Coarse JWT authorities (used in `@PreAuthorize`) follow `DOMAIN:ACTION`.
5. Granular ABAC permissions (stored in `t_00_02_04`) follow `RESOURCE:ACTION` (e.g. `FLOW_READING:MANAGE`). These are a separate tier evaluated inside services — they do not appear in `@PreAuthorize`.

### Coarse Authority Reference

| Authority | Scope | Used by |
|---|---|---|
| `FLOW:READ` | Read flow readings, events, operations, derived data, slots | `FlowReadingV2Controller`, `DerivedFlowReadingController`, `SlotCoverageController` |
| `FLOW:WRITE` | Create / update flow readings and operations | `FlowReadingV2Controller`, `FlowOperationV2Controller` |
| `FLOW:DELETE` | Delete flow readings and operations | `FlowReadingV2Controller`, `FlowOperationV2Controller` |
| `FLOW:SUBMIT` | Submit a reading into workflow | `FlowReadingV2Controller` |
| `FLOW:VALIDATE` | Approve or reject workflow transitions | `ReadingWorkflowV2Controller`, `FlowOperationV2Controller` |
| `FLOW:AUDIT` | Query workflow instance history | `WorkflowInstanceQueryController` |
| `THRESHOLD:READ` | Read threshold definitions | `FlowThresholdV2Controller` |
| `THRESHOLD:WRITE` | Create / update / activate / deactivate thresholds | `FlowThresholdV2Controller` |
| `CRISIS:READ` | Read incidents and severities | `IncidentController`, `IncidentSeverityController` |
| `NETWORK:READ` | Read network assets (pipeline, segment, station …) | `PipelineController`, `StationController` … |
| `NETWORK:WRITE` | Create / update network assets | `PipelineController`, `StationController` … |

---

## Workflow Controller Pattern

> **Workflow controllers intentionally do NOT extend `GenericController`.** This is correct and must not be changed.

### Rationale

`GenericController` provides standard CRUD (GET / POST / PUT / DELETE) endpoints.
Workflow controllers expose **domain transitions only** — approve, reject, submit, resolve — which have no valid CRUD representation.
Extending `GenericController` in a workflow context would expose illegal operations (e.g. DELETE on a workflow instance).

### Canonical workflow controllers

| Controller | Base Path | Operations |
|---|---|---|
| `ReadingWorkflowV2Controller` | `/api/v2/flow/workflow` | `approve`, `reject` |
| `FlowOperationV2Controller` (workflow part) | `/api/v2/flow/operations/{id}` | `approve`, `reject` |
| `WorkflowInstanceQueryController` | `/api/v2/flow/workflow/instances` | query / audit only |

Future workflow controllers (e.g. for crisis incident lifecycle) must follow the same pattern.

---

## Slot Coverage Placement

`SlotCoverageController` lives in `flow.workflow` and serves `/api/v2/flow/slot-coverage`.

**Why workflow, not intelligence:**
Slot coverage is a governance check (are all mandatory slots recorded for the current period?) used directly in the validation workflow. It is not a historical analytics query.
When a dedicated `flow.workflow.monitoring` subpackage is created it may be relocated there, but it must NOT move into `flow.intelligence`.

---

## Naming Conventions

| Layer | Pattern | Example |
|---|---|---|
| Service interface | `*Service` or `I*Service` | `FlowEventQueryService`, `IAlertStatusService` |
| Service impl | `*ServiceImpl` | `FlowEventQueryServiceImpl` |
| Facade interface | `*Facade` | `FlowAlertFacade`, `FlowEventFacade` |
| Facade impl | `*FacadeImpl` | `FlowAlertFacadeImpl` |
| Repository | `*Repository` | `FlowReadingRepository` |
| DTO (read) | `*ReadDTO` | `FlowEventReadDTO`, `IncidentReadDTO` |
| DTO (command) | `*CommandDTO` or in `dto.command/` | `FlowOperationCommandDTO`, `ReadingSubmitRequestDTO` |
| Entity table | `T_xx_xx_xx` | `T_05_01_01` |
| Entity PK field | `F_00` (always, from `GenericModel`) | — |
| Entity column | `F_nn` | `F_01`, `F_07` |
| Named FK | `T_xx_xx_xx_FK_nn` | `T_05_01_01_FK_01` |
| Named UK | `T_xx_xx_xx_UK_nn` | `T_05_00_01_UK_01` |

---

## Facade Ownership Rules

> **Contract and implementation must live in the owning module.**

| Facade Interface | Owner Module | Impl Location |
|---|---|---|
| `FlowAlertFacade` | `flow.intelligence.facade` | `flow.intelligence.facade.impl` |
| `FlowReadingFacade` | `flow.intelligence.facade` | `flow.intelligence.facade.impl` |
| `PipelineFacade` | `flow.intelligence.facade` | `flow.intelligence.facade.impl` |
| `FlowThresholdFacade` | `flow.common.facade` | `flow.common.facade.impl` |
| `FlowEventFacade` | `flow.core.facade` | `flow.core.facade.impl` |

Facade interfaces must NOT be placed in `facade.impl`. Interfaces belong in `facade/`; only `*FacadeImpl` classes belong in `facade/impl/`.

---

## Service Pattern Rules

- All services MUST have an interface in `service/`
- All implementations MUST be in `service/impl/`
- `@Service` annotation belongs ONLY on impl classes
- Controllers inject service interfaces, NOT impl classes

---

## DTO Package Rules

- Command DTOs: `*.dto.command`
- Read DTOs: `*.dto` root or `*.dto` named `*ReadDTO`
- Workflow-specific command DTOs: `flow.workflow.dto.command`
- Analytics / aggregation DTOs (dashboards, time series): `flow.intelligence.dto`

---

## Module Boundary Rules

- `flow.intelligence` must NOT import `flow.core` entities directly
- `crisis.core` must NOT import `flow.core` entities directly (use network FK via `PipelineSegment`)
- Cross-module data access goes through Facade interfaces only
- Facade return types are always DTOs — never entity types

---

## Orphan Entity Resolution Policy

Any entity without a repository, service, and controller is considered an orphan and must be:
1. Given a full chain (repo + service interface + impl + controller), OR
2. Deleted if provably unused (no FK references, no business need)

`ReadingSourceNature` — resolved in Commit 10 with full chain.

---

## Endpoint Base Paths

| Domain | Base Prefix | Example |
|---|---|---|
| Flow | `/api/v2/flow/...` | `/api/v2/flow/readings`, `/api/v2/flow/workflow` |
| Network | `/network/core/...` | `/network/core/pipeline` |
| Crisis | `/crisis/...` | `/crisis/incidents`, `/crisis/severities` |
| General | `/general/...` | `/general/organization/employee` |
| System | `/system/...` | `/system/security/user` |

Network endpoints use `/network/core/...` (not `/api/v2/...`) by established convention. This is intentional and documented.

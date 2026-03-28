# HyFloAPI — Architecture Reference

> Last updated: 2026-03-28 — Refactoring stabilization (Commits 1–13)

---

## Module Overview

| Module | Package | Responsibility |
|---|---|---|
| `flow.common` | `dz.sh.trc.hyflo.flow.common` | Reference data (status, slot, threshold, data source) |
| `flow.core` | `dz.sh.trc.hyflo.flow.core` | Core reading entities and CRUD |
| `flow.workflow` | `dz.sh.trc.hyflo.flow.workflow` | Approval/rejection workflow, slot coverage |
| `flow.intelligence` | `dz.sh.trc.hyflo.flow.intelligence` | Anomaly, alert, forecast, monitoring analytics |
| `network.core` | `dz.sh.trc.hyflo.network.core` | Pipeline, segment, station, terminal |
| `general.organization` | `dz.sh.trc.hyflo.general.organization` | Employee, structure |

---

## Naming Conventions

| Layer | Pattern | Example |
|---|---|---|
| Service interface | `*Service` or `I*Service` | `FlowEventQueryService`, `IAlertStatusService` |
| Service impl | `*ServiceImpl` | `FlowEventQueryServiceImpl` |
| Facade interface | `*Facade` | `FlowAlertFacade`, `FlowEventFacade` |
| Facade impl | `*FacadeImpl` | `FlowAlertFacadeImpl` |
| Repository | `*Repository` | `FlowReadingRepository` |
| DTO (read) | `*ReadDTO` | `FlowEventReadDTO` |
| DTO (command) | `*CommandDTO` | `FlowReadingCommandDTO` |
| DTO (workflow) | in `dto.command/` | `ReadingSubmitRequestDTO` |

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

---

## Module Boundary Rules

- `flow.intelligence` must NOT import `flow.core` entities directly
- Cross-module data access goes through Facade interfaces only
- Facade return types are always DTOs — never entity types

---

## Orphan Entity Resolution Policy

Any entity without a repository, service, and controller is considered an orphan and must be:
1. Given a full chain (repo + service interface + impl + controller), OR
2. Deleted if provably unused (no FK references, no business need)

`ReadingSourceNature` — resolved in Commit 10 with full chain.

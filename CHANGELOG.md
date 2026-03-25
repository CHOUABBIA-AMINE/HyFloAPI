# HyFlo API — Changelog

---

## [Phase 3] — Service Layer v2 (2026-03-25)

Phase 3 introduces the complete v2 service architecture across all modules.
All new services implement typed Command/Query interfaces.
No controller changes in this phase — backward compatibility fully preserved.

### Commits 16–26

#### Commit 16 — FlowReadingMapper (static mapping utility)
- Introduced `FlowReadingMapper` with `toReadDto()` and `toEntity()` static methods
- Maps `FlowReading` entity → `FlowReadingReadDto` (v2 read DTO)
- Maps `FlowReadingCommandDto` → `FlowReading` entity
- Eliminates `FlowReadingDTO.fromEntity()` legacy coupling in new service paths

#### Commits 17–18 — Repository additions
- `FlowReadingRepository`: Phase 3 query support methods added
  (slot-based, workflow linkage, latest-by-pipeline)
- `DerivedFlowReadingRepository`: derived reading query methods added
  (deleteBySourceReadingId, idempotency check, exact slot+segment lookup)

#### Commits 19 — SegmentDistributionService
- `SegmentDistributionService` interface: `generateDerivedReadings()` contract
- `SegmentDistributionServiceImpl`: proportional volume distribution across pipeline segments
  - Uses `distributionWeight` field on `PipelineSegment` for allocation
  - Idempotent: deletes existing derived readings before regeneration
  - Returns `List<DerivedFlowReadingReadDto>`

#### Commits 16 (continued) — FlowReadingCommandService / FlowReadingQueryService
- `FlowReadingCommandService`: typed write contract
- `FlowReadingQueryService`: typed read contract
- `FlowReadingCommandServiceImpl`: create/update/delete using FlowReadingMapper
- `FlowReadingQueryServiceImpl`: paginated, filtered, slot-based queries

#### Commit 19 (continued) — DerivedFlowReadingCommandService / DerivedFlowReadingQueryService
- `DerivedFlowReadingCommandService`: typed write contract for derived readings
- `DerivedFlowReadingQueryService`: typed read contract for derived readings
- `DerivedFlowReadingCommandServiceImpl`: create/update/delete/rebuild
- `DerivedFlowReadingQueryServiceImpl`: segment-based, date-range, source-based queries

#### Commit 20 — ReadableTargetValidationService
- `ReadableTargetValidationService` interface: contract for allowed target types
- `ReadableTargetValidationServiceImpl`: enforces PIPELINE, PIPELINE_SEGMENT, STATION, TERMINAL targets
  - Cross-pipeline segment guard
  - At-most-one secondary target rule
- `FlowReadingCommandDto`: patched with optional `pipelineSegmentId`, `stationId`, `terminalId` fields

#### Commits 21–22 — ReadingWorkflowService refactor
- Renamed `validate()` → `approve()` (aligns with APPROVED status code)
- Replaced `FlowReadingDTO.fromEntity()` with `FlowReadingMapper.toReadDto()`
- Added `WorkflowInstanceRepository` injection — WorkflowInstance becomes source of truth
- Added `guardAgainstInvalidTransition()` for double-approve / approve-after-approved protection
- `SegmentDistributionService.generateDerivedReadings()` triggered inside `approve()` (Commit 22)
  Non-fatal: generation failure logged as ERROR, approval remains authoritative
- Deprecated `validate()` stub retained for backward compat (Phase 4 migration target)

#### Commit 23 — Network module service layer
- `PipelineCommandService` / `PipelineQueryService` interfaces
- `PipelineCommandServiceImpl` / `PipelineQueryServiceImpl` implementations
- `PipelineSegmentCommandService` / `PipelineSegmentQueryService` interfaces
- `PipelineSegmentCommandServiceImpl` / `PipelineSegmentQueryServiceImpl` implementations
- All use Phase 2 mappers (`PipelineMapper`, `PipelineSegmentMapper`)

#### Commit 24 — Organization and Security module service layer
- `EmployeeCommandService` / `EmployeeQueryService` interfaces
- `EmployeeCommandServiceImpl` / `EmployeeQueryServiceImpl` implementations
- `UserCommandService` / `UserQueryService` interfaces
- `UserCommandServiceImpl` / `UserQueryServiceImpl` implementations
- `deactivateEmployee()` and `deactivateUser()` soft-disable operations

#### Commit 25 — WorkflowInstanceRepository
- Authoritative repository for `WorkflowInstance` state
- `findByFlowReadingId()`: reading → workflow instance resolution
- `findByCurrentStateCodeAndUpdatedAtAfter()`: state + time-window (alert support)
- `existsByFlowReadingIdAndCurrentStateCode()`: idempotency guard
- `findLatestByFlowReadingId()`: re-open scenario support
- `countByCurrentStateCode()`: dashboard KPI aggregation

#### Commit 26 — CHANGELOG update (this commit)

---

## [Phase 3] Summary

**All modules now have typed Command/Query service interfaces.**
**WorkflowInstance is established as source of truth for reading lifecycle.**
**Derived reading generation is integrated into the approval workflow.**
**All controllers remain unchanged — Phase 4 will migrate them to v2 services.**

---

## [Phase 2] — DTO & Mapper Layer (2026-03-24)

Phase 2 introduced typed read/write DTOs and static mappers across all modules.
See previous changelog entries for details.

---

## [Phase 1] — Architecture Audit (2026-03-24)

Phase 1 documented architectural weaknesses, DTO issues, service coupling,
and AI readiness gaps. See forensic audit document for full details.

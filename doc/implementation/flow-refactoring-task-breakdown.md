# Flow Module Refactoring - Task Breakdown

**Project:** Flow Module Architecture Refactoring

**Start Date:** February 9, 2026

**Target Completion:** April 30, 2026 (12 weeks)

**Project Manager:** [TBD]

**Tech Lead:** [TBD]

---

## Project Overview

**Objective:** Eliminate duplication between `flow/core` and `flow/intelligence` modules by establishing clear separation of concerns.

**Scope:**
- 23 files to create
- 15 files to update
- 12 files to delete
- ~500 lines of duplicated code to eliminate

**Success Criteria:**
- Zero duplicated business logic
- Test coverage ≥ 80%
- All monitoring queries execute in < 200ms
- Zero production incidents
- Frontend successfully migrated

---

## Phase 1: Preparation (Week 1)

**Duration:** 1 week

**Goal:** Document decisions and communicate changes

### Task 1.1: Create Architecture Decision Record

**Assignee:** Architecture Team

**Priority:** P0 (Critical)

**Estimated Hours:** 8

**Status:** ✅ COMPLETED

**Deliverables:**
- [x] ADR-001 document created
- [x] Stakeholder review completed
- [x] Approvals obtained

**File:** `doc/architecture/adr/ADR-001-flow-module-refactoring.md`

---

### Task 1.2: Create Migration Guide

**Assignee:** Backend Team Lead

**Priority:** P0 (Critical)

**Estimated Hours:** 12

**Status:** ✅ COMPLETED

**Deliverables:**
- [x] Migration guide with examples
- [x] Before/after code snippets
- [x] Common pitfalls documented
- [x] FAQ section

**File:** `docs/migration/flow-api-migration-guide.md`

---

### Task 1.3: Create Task Breakdown

**Assignee:** Project Manager

**Priority:** P0 (Critical)

**Estimated Hours:** 6

**Status:** ✅ COMPLETED

**Deliverables:**
- [x] Detailed task list with estimates
- [x] Resource allocation plan
- [x] Risk assessment
- [x] Timeline with milestones

**File:** `doc/implementation/flow-refactoring-task-breakdown.md`

---

### Task 1.4: Team Communication

**Assignee:** Engineering Manager

**Priority:** P0 (Critical)

**Estimated Hours:** 4

**Status:** ⏳ TODO

**Deliverables:**
- [ ] Kickoff meeting scheduled
- [ ] All teams notified (backend, frontend, QA, DevOps)
- [ ] Slack channel created (#flow-api-migration)
- [ ] Weekly sync meetings scheduled

**Actions:**
1. Schedule kickoff meeting with all stakeholders
2. Present ADR and migration plan
3. Create dedicated Slack channel
4. Set up recurring weekly syncs

---

### Task 1.5: Environment Setup

**Assignee:** DevOps Team

**Priority:** P1 (High)

**Estimated Hours:** 8

**Status:** ⏳ TODO

**Deliverables:**
- [ ] Feature flags configured
- [ ] Monitoring dashboards created
- [ ] Logging enhanced for migration tracking
- [ ] Rollback procedures documented

**Configuration Changes:**

```yaml
# application.yml
flow:
  intelligence:
    enabled: true
    use-new-endpoints: false  # Will flip to true in Phase 5
  core:
    monitoring:
      deprecated-endpoints-enabled: true
      log-deprecation-warnings: true
```

**Monitoring Dashboards:**
- Deprecated endpoint usage metrics
- New endpoint adoption rate
- Error rates (old vs new)
- Performance comparison (response times)

---

## Phase 2: Create Repository Interfaces (Week 2)

**Duration:** 1 week

**Goal:** Create new repository interfaces for intelligence module

### Task 2.1: Create PipelineStatisticsRepository

**Assignee:** Backend Developer 1

**Priority:** P0 (Critical)

**Estimated Hours:** 12

**Status:** ⏳ TODO

**Dependencies:** None

**Deliverables:**
- [ ] Repository interface created
- [ ] SQL queries migrated from FlowReadingRepository
- [ ] Unit tests written
- [ ] Integration tests written
- [ ] Performance benchmarks run

**File:** `src/main/java/dz/hydatis/hyfloapi/flow/intelligence/repository/PipelineStatisticsRepository.java`

**Methods to Implement:**

```java
@Repository
public interface PipelineStatisticsRepository extends JpaRepository<FlowReading, Long> {
    
    @Query(value = "SELECT fr.reading_date, COUNT(DISTINCT p.id), ...")
    List<Object[]> getDailyCompletionStatistics(
        @Param("structureId") Long structureId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query(value = "SELECT e.id, CONCAT(e.first_name_lt, ' ', e.last_name_lt), ...")
    List<Object[]> getValidatorWorkloadDistribution(
        @Param("structureId") Long structureId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query(value = "WITH date_range AS (...) SELECT pl.id, pl.code, ...")
    List<Object[]> getPipelineCoverageByDateRange(
        @Param("structureId") Long structureId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
```

**Testing:**
- Query execution < 200ms for 1000 pipelines
- Correct aggregation results
- Handles null values gracefully
- Date range edge cases

**Acceptance Criteria:**
- [ ] All queries return correct results
- [ ] Performance benchmarks pass
- [ ] Test coverage ≥ 80%
- [ ] Code review approved

---

### Task 2.2: Create SlotCoverageRepository

**Assignee:** Backend Developer 2

**Priority:** P0 (Critical)

**Estimated Hours:** 12

**Status:** ⏳ TODO

**Dependencies:** None

**Deliverables:**
- [ ] Repository interface created
- [ ] SQL queries migrated and optimized
- [ ] Unit tests written
- [ ] Integration tests written
- [ ] Performance benchmarks run

**File:** `src/main/java/dz/hydatis/hyfloapi/flow/intelligence/repository/SlotCoverageRepository.java`

**Methods to Implement:**

```java
@Repository
public interface SlotCoverageRepository extends JpaRepository<FlowReading, Long> {
    
    @Query("SELECT p.id, p.code, p.name, fr.id, COALESCE(vs.code, 'NOT_RECORDED'), ...")
    List<SlotCoverageProjection> findSlotCoverage(
        @Param("readingDate") LocalDate readingDate,
        @Param("slotId") Long slotId,
        @Param("structureId") Long structureId
    );
    
    @Query("SELECT COUNT(DISTINCT p.id) = COUNT(DISTINCT CASE WHEN vs.code = 'APPROVED' ...")
    Boolean isSlotComplete(
        @Param("readingDate") LocalDate readingDate,
        @Param("slotId") Long slotId,
        @Param("structureId") Long structureId
    );
    
    @Query(value = "SELECT COUNT(*) as recorded, SUM(CASE WHEN status = 'APPROVED' ...) ...")
    List<Object[]> getSlotStatistics(
        @Param("readingDate") LocalDate readingDate,
        @Param("slotId") Long slotId,
        @Param("structureId") Long structureId
    );
}
```

**Testing:**
- All 12 slots calculated correctly
- Overdue detection accurate
- Structure-wide aggregation correct
- Edge cases (missing pipelines, no data)

**Acceptance Criteria:**
- [ ] Slot coverage calculation matches existing logic
- [ ] Query performance < 150ms
- [ ] Test coverage ≥ 80%
- [ ] Code review approved

---

### Task 2.3: Create TimeSeriesRepository

**Assignee:** Backend Developer 3

**Priority:** P0 (Critical)

**Estimated Hours:** 10

**Status:** ⏳ TODO

**Dependencies:** None

**Deliverables:**
- [ ] Repository interface created
- [ ] SQL queries migrated
- [ ] Unit tests written
- [ ] Integration tests written
- [ ] Performance benchmarks run

**File:** `src/main/java/dz/hydatis/hyfloapi/flow/intelligence/repository/TimeSeriesRepository.java`

**Methods to Implement:**

```java
@Repository
public interface TimeSeriesRepository extends JpaRepository<FlowReading, Long> {
    
    @Query(value = "SELECT CASE WHEN :groupBy = 'HOUR' ... END as period, COUNT(*) ...")
    List<Object[]> getSubmissionTrends(
        @Param("structureId") Long structureId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("groupBy") String groupBy
    );
    
    List<FlowReading> findByPipelineIdAndReadingDateBetweenOrderByReadingDateAscRecordedAtAsc(
        Long pipelineId,
        LocalDate startDate,
        LocalDate endDate
    );
}
```

**Testing:**
- Correct grouping by HOUR, DAY, WEEK, MONTH
- Time-series ordering correct
- Date range boundaries handled
- Large datasets (1000+ records)

**Acceptance Criteria:**
- [ ] All grouping options work correctly
- [ ] Query performance < 200ms
- [ ] Test coverage ≥ 80%
- [ ] Code review approved

---

### Task 2.4: Split FlowReadingRepository

**Assignee:** Backend Developer 1

**Priority:** P1 (High)

**Estimated Hours:** 8

**Status:** ⏳ TODO

**Dependencies:** Task 2.1, 2.2, 2.3 completed

**Deliverables:**
- [ ] FlowReadingQueryRepository created
- [ ] FlowReadingCommandRepository created
- [ ] Methods moved from FlowReadingRepository
- [ ] All references updated
- [ ] Tests updated

**Files:**
- `src/main/java/dz/hydatis/hyfloapi/flow/core/repository/FlowReadingQueryRepository.java`
- `src/main/java/dz/hydatis/hyfloapi/flow/core/repository/FlowReadingCommandRepository.java`

**Migration Strategy:**

```java
// FlowReadingQueryRepository - Read-only queries
@Repository
public interface FlowReadingQueryRepository extends JpaRepository<FlowReading, Long> {
    List<FlowReading> findPendingValidations(Long structureId, Pageable pageable);
    List<FlowReading> findByPipelineAndTimeRange(Long pipelineId, LocalDateTime start, LocalDateTime end);
}

// FlowReadingCommandRepository - Write operations with locking
@Repository
public interface FlowReadingCommandRepository extends JpaRepository<FlowReading, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<FlowReading> findByIdForUpdate(Long id);
}
```

**Acceptance Criteria:**
- [ ] All queries categorized correctly (read vs write)
- [ ] No broken references
- [ ] All tests passing
- [ ] Code review approved

---

## Phase 3: Create Service Classes (Week 3-4)

**Duration:** 2 weeks

**Goal:** Create new service classes for intelligence module

### Task 3.1: Create SlotCoverageService

**Assignee:** Backend Developer 2

**Priority:** P0 (Critical)

**Estimated Hours:** 16

**Status:** ⏳ TODO

**Dependencies:** Task 2.2 (SlotCoverageRepository)

**Deliverables:**
- [ ] Service class created
- [ ] Business logic migrated from PipelineIntelligenceService
- [ ] Unit tests written (mock repository)
- [ ] Integration tests written
- [ ] Validation against old implementation

**File:** `src/main/java/dz/hydatis/hyfloapi/flow/intelligence/service/SlotCoverageService.java`

**Key Methods:**

```java
@Service
@RequiredArgsConstructor
public class SlotCoverageService {
    
    private final SlotCoverageRepository slotCoverageRepository;
    private final ReadingSlotRepository readingSlotRepository;
    
    public List<SlotStatusDTO> getSlotCoverage(Long pipelineId, LocalDate date) {
        // Replace in-memory calculation with repository query
    }
    
    public List<SlotCoverageDTO> getStructureSlotCoverage(
        Long structureId, LocalDate date, Long slotId) {
        // Structure-wide aggregation
    }
    
    public boolean isSlotComplete(Long structureId, LocalDate date, Long slotId) {
        return slotCoverageRepository.isSlotComplete(date, slotId, structureId);
    }
    
    public SlotStatistics calculateSlotStatistics(Long pipelineId, LocalDate date) {
        // Use repository query instead of Java calculation
    }
}
```

**Testing:**
- Compare results with old implementation
- Edge cases (missing data, null values)
- Performance comparison (should be faster)
- Concurrent access

**Acceptance Criteria:**
- [ ] Results match old implementation 100%
- [ ] Performance improved or equal
- [ ] Test coverage ≥ 85%
- [ ] Code review approved

---

### Task 3.2: Create StatisticsService

**Assignee:** Backend Developer 1

**Priority:** P0 (Critical)

**Estimated Hours:** 16

**Status:** ⏳ TODO

**Dependencies:** Task 2.1 (PipelineStatisticsRepository)

**Deliverables:**
- [ ] Service class created
- [ ] All statistical calculations centralized
- [ ] Unit tests written
- [ ] Integration tests written
- [ ] Performance benchmarks

**File:** `src/main/java/dz/hydatis/hyfloapi/flow/intelligence/service/StatisticsService.java`

**Key Methods:**

```java
@Service
@RequiredArgsConstructor
public class StatisticsService {
    
    private final PipelineStatisticsRepository statisticsRepository;
    
    public List<DailyCompletionStatisticsDTO> getDailyStatistics(
        Long structureId, LocalDate startDate, LocalDate endDate) {
        // Use repository query
    }
    
    public List<ValidatorWorkloadDTO> getValidatorWorkload(
        Long structureId, LocalDate startDate, LocalDate endDate) {
        // Use repository query
    }
    
    public List<PipelineCoverageDetailDTO> getPipelineCoverage(
        Long structureId, LocalDate startDate, LocalDate endDate) {
        // Use repository query
    }
    
    public StatisticalSummaryDTO calculateMeasurementStatistics(List<BigDecimal> values) {
        // Statistical calculations (min, max, avg, median, std dev)
    }
}
```

**Testing:**
- Statistical accuracy (validate formulas)
- Large datasets (10,000+ readings)
- Edge cases (empty datasets, single value)
- Compare with old implementation

**Acceptance Criteria:**
- [ ] All statistics accurate
- [ ] Performance < 200ms
- [ ] Test coverage ≥ 85%
- [ ] Code review approved

---

### Task 3.3: Create TimeSeriesService

**Assignee:** Backend Developer 3

**Priority:** P0 (Critical)

**Estimated Hours:** 14

**Status:** ⏳ TODO

**Dependencies:** Task 2.3 (TimeSeriesRepository)

**Deliverables:**
- [ ] Service class created
- [ ] Time-series logic migrated
- [ ] Unit tests written
- [ ] Integration tests written
- [ ] Performance validation

**File:** `src/main/java/dz/hydatis/hyfloapi/flow/intelligence/service/TimeSeriesService.java`

**Key Methods:**

```java
@Service
@RequiredArgsConstructor
public class TimeSeriesService {
    
    private final TimeSeriesRepository timeSeriesRepository;
    
    public List<SubmissionTrendDTO> getSubmissionTrends(
        Long structureId, LocalDate startDate, LocalDate endDate, String groupBy) {
        // Use repository query
    }
    
    public ReadingsTimeSeriesDTO getReadingsTimeSeries(
        Long pipelineId, LocalDate startDate, LocalDate endDate, String measurementType) {
        // Build time series with statistical analysis
    }
    
    public List<TimeSeriesDataPointDTO> buildTimeSeriesDataPoints(
        List<FlowReading> readings, String measurementType) {
        // Transform readings into time-series data points
    }
}
```

**Testing:**
- Correct time-series ordering
- All grouping options (HOUR, DAY, WEEK, MONTH)
- Statistical calculations over time periods
- Missing data handling (gaps in time series)

**Acceptance Criteria:**
- [ ] Time-series data accurate
- [ ] Performance < 250ms
- [ ] Test coverage ≥ 85%
- [ ] Code review approved

---

### Task 3.4: Create FlowReadingQueryService

**Assignee:** Backend Developer 1

**Priority:** P1 (High)

**Estimated Hours:** 8

**Status:** ⏳ TODO

**Dependencies:** Task 2.4 (Repository split)

**Deliverables:**
- [ ] Common query service created
- [ ] Shared queries extracted
- [ ] Used by both core and intelligence
- [ ] Unit tests written

**File:** `src/main/java/dz/hydatis/hyfloapi/flow/common/service/FlowReadingQueryService.java`

**Key Methods:**

```java
@Service
@RequiredArgsConstructor
public class FlowReadingQueryService {
    
    private final FlowReadingRepository flowReadingRepository;
    
    public List<FlowReading> findByPipelineIdAndReadingDate(
        Long pipelineId, LocalDate readingDate) {
        return flowReadingRepository.findByPipelineIdAndReadingDate(pipelineId, readingDate);
    }
    
    public Optional<FlowReading> findLatestByPipeline(Long pipelineId) {
        return flowReadingRepository.findTopByPipelineIdOrderByRecordedAtDesc(pipelineId);
    }
}
```

**Acceptance Criteria:**
- [ ] Used by both modules
- [ ] No circular dependencies
- [ ] Test coverage ≥ 80%
- [ ] Code review approved

---

## Phase 4: Create Unified DTOs (Week 4)

**Duration:** 1 week (parallel with Phase 3)

**Goal:** Create unified DTO classes in common module

### Task 4.1: Create Common DTO Package

**Assignee:** Backend Developer 2

**Priority:** P0 (Critical)

**Estimated Hours:** 20

**Status:** ⏳ TODO

**Dependencies:** None (can start in parallel)

**Deliverables:**
- [ ] 7 new DTO classes created
- [ ] Mapper utilities created
- [ ] Unit tests for DTOs
- [ ] Serialization tests
- [ ] Swagger annotations added

**Files to Create:**

1. `SlotCoverageDTO.java` - Replaces SlotStatusDTO + SlotCoverageResponseDTO
2. `TimeSeriesDTO.java` - Replaces ReadingsTimeSeriesDTO + SubmissionTrendDTO
3. `OperationalStatisticsDTO.java` - Replaces DailyCompletionStatisticsDTO
4. `MeasurementStatisticsDTO.java` - Replaces StatisticalSummaryDTO
5. `WorkloadDTO.java` - Replaces ValidatorWorkloadDTO
6. `PipelineMonitoringDTO.java` - Replaces PipelineOverviewDTO
7. `StructureOverviewDTO.java` - New structure-wide dashboard

**Path:** `src/main/java/dz/hydatis/hyfloapi/flow/common/dto/monitoring/`

**Testing:**
- JSON serialization/deserialization
- Null handling
- Validation annotations
- Swagger documentation generation

**Acceptance Criteria:**
- [ ] All DTOs created with complete fields
- [ ] Swagger docs generated correctly
- [ ] Serialization tests pass
- [ ] Code review approved

---

## Phase 5: Refactor Intelligence Module (Week 5-6)

**Duration:** 2 weeks

**Goal:** Update intelligence module to use new services and DTOs

### Task 5.1: Refactor PipelineIntelligenceService

**Assignee:** Backend Developer 2

**Priority:** P0 (Critical)

**Estimated Hours:** 20

**Status:** ⏳ TODO

**Dependencies:** Tasks 3.1, 3.2, 3.3, 4.1

**Deliverables:**
- [ ] Service refactored to use new dependencies
- [ ] In-memory calculations removed
- [ ] Uses SlotCoverageService, StatisticsService, TimeSeriesService
- [ ] Tests updated
- [ ] Performance validated

**File:** `src/main/java/dz/hydatis/hyfloapi/flow/intelligence/service/PipelineIntelligenceService.java`

**Changes:**

```java
// BEFORE - In-memory calculation
private SlotStatistics getSlotStatistics(Long pipelineId, LocalDate date) {
    var readings = flowReadingRepository.findByPipelineIdAndReadingDate(pipelineId, date);
    // ... 50 lines of Java aggregation logic
}

// AFTER - Use repository query
private SlotStatistics getSlotStatistics(Long pipelineId, LocalDate date) {
    return slotCoverageService.calculateSlotStatistics(pipelineId, date);
}
```

**Testing:**
- Compare results before/after refactoring
- Performance should improve
- No behavior changes
- All existing tests still pass

**Acceptance Criteria:**
- [ ] No in-memory aggregation logic remains
- [ ] All tests passing
- [ ] Performance improved
- [ ] Code review approved

---

### Task 5.2: Add Structure-Wide Endpoints

**Assignee:** Backend Developer 1

**Priority:** P0 (Critical)

**Estimated Hours:** 16

**Status:** ⏳ TODO

**Dependencies:** Tasks 3.1, 3.2, 3.3, 4.1

**Deliverables:**
- [ ] 7 new endpoints added to PipelineIntelligenceController
- [ ] Swagger documentation complete
- [ ] Integration tests written
- [ ] Postman collection updated

**File:** `src/main/java/dz/hydatis/hyfloapi/flow/intelligence/controller/PipelineIntelligenceController.java`

**New Endpoints:**

```java
@GetMapping("/structures/{structureId}/overview")
public ResponseEntity<StructureOverviewDTO> getStructureOverview(
    @PathVariable Long structureId,
    @RequestParam(required = false) LocalDate referenceDate)

@GetMapping("/structures/{structureId}/slot-coverage")
public ResponseEntity<List<SlotCoverageDTO>> getStructureSlotCoverage(
    @PathVariable Long structureId,
    @RequestParam(required = false) LocalDate date,
    @RequestParam(required = false) Long slotId)

@GetMapping("/structures/{structureId}/statistics")
public ResponseEntity<List<OperationalStatisticsDTO>> getStatistics(
    @PathVariable Long structureId,
    @RequestParam LocalDate startDate,
    @RequestParam LocalDate endDate)

@GetMapping("/structures/{structureId}/workload")
public ResponseEntity<List<WorkloadDTO>> getWorkload(
    @PathVariable Long structureId,
    @RequestParam LocalDate startDate,
    @RequestParam LocalDate endDate)

@GetMapping("/structures/{structureId}/trends")
public ResponseEntity<TimeSeriesDTO> getTrends(
    @PathVariable Long structureId,
    @RequestParam LocalDate startDate,
    @RequestParam LocalDate endDate,
    @RequestParam(defaultValue = "DAY") String groupBy)

@GetMapping("/structures/{structureId}/coverage")
public ResponseEntity<List<PipelineMonitoringDTO>> getCoverage(
    @PathVariable Long structureId,
    @RequestParam LocalDate startDate,
    @RequestParam LocalDate endDate)

@GetMapping("/structures/{structureId}/overdue")
public ResponseEntity<Page<FlowReadingDTO>> getOverdueReadings(
    @PathVariable Long structureId,
    @RequestParam(required = false) LocalDate asOfDate,
    Pageable pageable)
```

**Testing:**
- End-to-end tests for each endpoint
- Error handling (404, 400, 500)
- Pagination
- Input validation

**Acceptance Criteria:**
- [ ] All 7 endpoints working
- [ ] Swagger docs complete
- [ ] Integration tests passing
- [ ] Code review approved

---

## Phase 6: Clean Up Core Module (Week 7)

**Duration:** 1 week

**Goal:** Remove monitoring logic from core module

### Task 6.1: Add Deprecation Warnings

**Assignee:** Backend Developer 3

**Priority:** P0 (Critical)

**Estimated Hours:** 8

**Status:** ⏳ TODO

**Dependencies:** Task 5.2 (New endpoints working)

**Deliverables:**
- [ ] @Deprecated annotations added
- [ ] Deprecation warnings in logs
- [ ] HTTP headers with Sunset date
- [ ] Swagger docs updated

**File:** `src/main/java/dz/hydatis/hyfloapi/flow/core/controller/FlowReadingController.java`

**Changes:**

```java
@Deprecated(since = "2.1", forRemoval = true)
@GetMapping("/monitoring/daily-statistics")
public ResponseEntity<List<DailyCompletionStatisticsDTO>> getDailyStatistics(
    @RequestParam Long structureId,
    @RequestParam LocalDate startDate,
    @RequestParam LocalDate endDate) {
    
    log.warn("DEPRECATED ENDPOINT CALLED: /monitoring/daily-statistics. " +
             "Use /api/v1/flow/intelligence/structures/{id}/statistics instead. " +
             "This endpoint will be removed on 2026-05-09.");
    
    // Add deprecation headers
    HttpHeaders headers = new HttpHeaders();
    headers.add("Deprecation", "true");
    headers.add("Sunset", "Sat, 09 May 2026 00:00:00 GMT");
    headers.add("Link", "</api/v1/flow/intelligence/structures/{id}/statistics>; rel=\"successor-version\"");
    
    // ... existing implementation
}
```

**Acceptance Criteria:**
- [ ] All deprecated endpoints have warnings
- [ ] Sunset headers added
- [ ] Swagger shows deprecation notice
- [ ] Logging works correctly

---

### Task 6.2: Remove Monitoring Methods from FlowReadingService

**Assignee:** Backend Developer 1

**Priority:** P1 (High) - After grace period

**Estimated Hours:** 6

**Status:** ⏳ BLOCKED (Wait for grace period)

**Dependencies:** All frontend apps migrated

**Deliverables:**
- [ ] Monitoring methods removed
- [ ] Only CRUD methods remain
- [ ] Tests updated
- [ ] References removed

**File:** `src/main/java/dz/hydatis/hyfloapi/flow/core/service/FlowReadingService.java`

**Methods to Remove:**
- `findOverdueReadingsByStructure()`
- `getDailyCompletionStatistics()`
- `getValidatorWorkloadDistribution()`
- `getSubmissionTrends()`
- `getPipelineCoverageByDateRange()`

**Acceptance Criteria:**
- [ ] Service only has CRUD methods
- [ ] All tests passing
- [ ] No broken references

---

### Task 6.3: Remove Deprecated Endpoints

**Assignee:** Backend Developer 3

**Priority:** P1 (High) - After grace period

**Estimated Hours:** 4

**Status:** ⏳ BLOCKED (Wait for grace period)

**Dependencies:** Frontend migration complete, usage metrics show zero traffic

**Deliverables:**
- [ ] Deprecated endpoints removed
- [ ] Tests removed
- [ ] Swagger docs updated

**File:** `src/main/java/dz/hydatis/hyfloapi/flow/core/controller/FlowReadingController.java`

**Endpoints to Remove:**
- `GET /monitoring/overdue-readings`
- `GET /monitoring/daily-statistics`
- `GET /monitoring/validator-workload`
- `GET /monitoring/submission-trends`
- `GET /monitoring/pipeline-coverage`
- `POST /slot-coverage`

**Acceptance Criteria:**
- [ ] All deprecated endpoints removed
- [ ] 404 for old URLs
- [ ] Tests cleaned up

---

### Task 6.4: Remove Queries from FlowReadingRepository

**Assignee:** Backend Developer 2

**Priority:** P1 (High) - After grace period

**Estimated Hours:** 4

**Status:** ⏳ BLOCKED (Wait for Task 6.2, 6.3)

**Dependencies:** Tasks 6.2, 6.3 complete

**Deliverables:**
- [ ] Monitoring queries removed
- [ ] Only core queries remain
- [ ] Tests updated

**File:** `src/main/java/dz/hydatis/hyfloapi/flow/core/repository/FlowReadingRepository.java`

**Queries to Remove:**
- `findOverdueReadingsByStructure()`
- `getDailyCompletionStatistics()`
- `getValidatorWorkloadDistribution()`
- `getSubmissionTrends()`
- `getPipelineCoverageByDateRange()`
- `findSlotCoverage()`
- `isSlotComplete()`

**Acceptance Criteria:**
- [ ] Only CRUD queries remain
- [ ] No broken references
- [ ] All tests passing

---

## Phase 7: Migration & Validation (Week 8-12)

**Duration:** 4 weeks

**Goal:** Migrate frontend apps and validate complete system

### Task 7.1: Frontend Migration

**Assignee:** Frontend Team

**Priority:** P0 (Critical)

**Estimated Hours:** 60 (3 developers × 20 hours)

**Status:** ⏳ TODO

**Dependencies:** Task 5.2 (New endpoints available)

**Deliverables:**
- [ ] All deprecated endpoint calls replaced
- [ ] New DTO structures handled
- [ ] HTTP methods updated (POST → GET)
- [ ] Tests updated
- [ ] User acceptance testing passed

**Applications to Update:**
1. HyFloWEB (main dashboard)
2. Mobile app (if applicable)
3. Reports module
4. Admin panel

**Progress Tracking:**
- [ ] Slot coverage widget migrated
- [ ] Daily statistics dashboard migrated
- [ ] Validator workload page migrated
- [ ] Trends charts migrated
- [ ] Pipeline coverage page migrated
- [ ] Overdue readings list migrated

**Testing:**
- Visual regression testing
- End-to-end testing
- Performance testing (page load times)
- Cross-browser testing

**Acceptance Criteria:**
- [ ] Zero calls to deprecated endpoints
- [ ] All functionality working
- [ ] User acceptance passed
- [ ] Performance equal or better

---

### Task 7.2: Parallel Validation

**Assignee:** QA Team

**Priority:** P0 (Critical)

**Estimated Hours:** 40

**Status:** ⏳ TODO

**Dependencies:** Tasks 5.2, 7.1

**Deliverables:**
- [ ] Automated comparison tests
- [ ] Results validation report
- [ ] Discrepancy investigation
- [ ] Sign-off on consistency

**Validation Strategy:**

```javascript
// Compare old vs new API results
for (const testCase of testCases) {
  const oldResult = await callOldAPI(testCase);
  const newResult = await callNewAPI(testCase);
  
  const isConsistent = deepCompare(oldResult, newResult);
  
  if (!isConsistent) {
    logDiscrepancy(testCase, oldResult, newResult);
  }
}
```

**Test Coverage:**
- 100 sample structures
- 30 days of historical data
- All endpoint combinations
- Edge cases (missing data, null values)

**Acceptance Criteria:**
- [ ] 100% consistency achieved
- [ ] All discrepancies resolved
- [ ] Validation report approved

---

### Task 7.3: Performance Testing

**Assignee:** DevOps Team + QA

**Priority:** P0 (Critical)

**Estimated Hours:** 24

**Status:** ⏳ TODO

**Dependencies:** Task 7.1

**Deliverables:**
- [ ] Load testing completed
- [ ] Performance benchmarks compared
- [ ] Bottlenecks identified and fixed
- [ ] Performance report

**Performance Targets:**

| Endpoint | Target (95th percentile) | Current Baseline |
|----------|-------------------------|------------------|
| Slot coverage | < 150ms | ~200ms |
| Daily statistics | < 200ms | ~300ms |
| Validator workload | < 200ms | ~250ms |
| Submission trends | < 250ms | ~350ms |
| Pipeline coverage | < 200ms | ~400ms |

**Load Testing:**
- 100 concurrent users
- 1000 requests/minute
- 1 hour duration
- Monitor:
  - Response times
  - Error rates
  - Database CPU/Memory
  - Application CPU/Memory

**Acceptance Criteria:**
- [ ] All targets met
- [ ] No performance regression
- [ ] Database indexes optimized
- [ ] Caching strategy implemented if needed

---

### Task 7.4: Delete Deprecated Code

**Assignee:** Backend Team

**Priority:** P2 (Low) - After grace period

**Estimated Hours:** 12

**Status:** ⏳ BLOCKED (Wait for grace period end)

**Dependencies:** All frontend apps migrated, zero traffic to old endpoints

**Deliverables:**
- [ ] 10 deprecated DTO files deleted
- [ ] Deprecated endpoints removed
- [ ] Deprecated methods removed
- [ ] Tests cleaned up
- [ ] Documentation updated

**Files to Delete:**

1. `SlotStatusDTO.java`
2. `SlotCoverageResponseDTO.java`
3. `SlotCoverageRequestDTO.java`
4. `StatisticalSummaryDTO.java`
5. `ReadingsTimeSeriesDTO.java`
6. `SubmissionTrendDTO.java`
7. `DailyCompletionStatisticsDTO.java`
8. `ValidatorWorkloadDTO.java`
9. `PipelineOverviewDTO.java`
10. `PipelineCoverageDetailDTO.java`

**Verification:**
- [ ] No references to deleted files
- [ ] All imports removed
- [ ] All tests passing
- [ ] Code coverage maintained

**Acceptance Criteria:**
- [ ] All deprecated code removed
- [ ] Build succeeds
- [ ] All tests passing
- [ ] No warnings or errors

---

### Task 7.5: Final Documentation

**Assignee:** Tech Writer + Backend Lead

**Priority:** P1 (High)

**Estimated Hours:** 16

**Status:** ⏳ TODO

**Dependencies:** All previous tasks complete

**Deliverables:**
- [ ] Architecture diagrams updated
- [ ] API documentation complete
- [ ] Developer guide updated
- [ ] Postman collection published
- [ ] Wiki pages updated

**Documents to Update:**

1. Architecture diagrams (module structure)
2. API documentation (Swagger + manual docs)
3. Developer onboarding guide
4. Code contribution guidelines
5. Troubleshooting guide

**Acceptance Criteria:**
- [ ] All documentation accurate
- [ ] No references to old endpoints
- [ ] Examples use new APIs
- [ ] Team review approved

---

## Resource Allocation

### Team Structure

**Backend Team:**
- Backend Developer 1: 120 hours (3 weeks full-time)
- Backend Developer 2: 140 hours (3.5 weeks full-time)
- Backend Developer 3: 100 hours (2.5 weeks full-time)
- Backend Team Lead: 40 hours (code reviews, guidance)

**Frontend Team:**
- Frontend Developer 1: 20 hours
- Frontend Developer 2: 20 hours
- Frontend Developer 3: 20 hours

**QA Team:**
- QA Engineer 1: 40 hours (parallel validation)
- QA Engineer 2: 24 hours (performance testing)

**DevOps Team:**
- DevOps Engineer: 16 hours (environment setup, monitoring)

**Management:**
- Project Manager: 20 hours (coordination, tracking)
- Engineering Manager: 10 hours (stakeholder communication)

**Total Effort:** ~570 hours (~14 weeks for 1 person, ~6 weeks with team)

---

## Risk Register

| Risk | Probability | Impact | Mitigation | Owner |
|------|------------|--------|------------|-------|
| Frontend migration delays | High | High | Parallel work, clear migration guide, office hours | Frontend Lead |
| Performance regression | Medium | High | Benchmark before/after, load testing, caching | Backend Lead |
| Inconsistent results | Medium | Critical | Parallel validation, automated comparison | QA Lead |
| Scope creep | Medium | Medium | Strict phase boundaries, no new features | PM |
| Team availability | High | Medium | Buffer time in estimates, backup resources | Engineering Manager |
| Production incidents | Low | Critical | Feature flags, rollback plan, monitoring | DevOps Lead |
| Communication gaps | Medium | Medium | Weekly syncs, Slack channel, documentation | PM |

---

## Success Metrics

### Code Quality

- [ ] Zero duplicated business logic
- [ ] Test coverage ≥ 80% for all new code
- [ ] SonarQube quality gate passed (A rating)
- [ ] Zero critical bugs
- [ ] Code review approval rate ≥ 95%

### Performance

- [ ] All queries execute in < 200ms (95th percentile)
- [ ] Database CPU usage unchanged or improved
- [ ] No N+1 query problems
- [ ] Cache hit rate ≥ 80% (if caching added)

### API Consistency

- [ ] All intelligence endpoints follow /api/v1/flow/intelligence pattern
- [ ] Consistent response formats
- [ ] Proper HTTP status codes
- [ ] Complete Swagger documentation

### Migration Success

- [ ] Frontend successfully migrated
- [ ] Zero production incidents
- [ ] Deprecated endpoint usage = 0
- [ ] User satisfaction maintained

### Team Success

- [ ] All milestones met on time
- [ ] No team member burnout
- [ ] Knowledge sharing sessions completed
- [ ] Documentation complete

---

## Milestones

| Milestone | Target Date | Status | Dependencies |
|-----------|------------|--------|-------------|
| **M1: Phase 1 Complete** | Week 1 (Feb 16) | ⏳ TODO | ADR, Migration Guide, This Document |
| **M2: Repositories Created** | Week 2 (Feb 23) | ⏳ TODO | M1 |
| **M3: Services Created** | Week 4 (Mar 9) | ⏳ TODO | M2 |
| **M4: Intelligence Updated** | Week 6 (Mar 23) | ⏳ TODO | M3 |
| **M5: Deprecation Warnings** | Week 7 (Mar 30) | ⏳ TODO | M4 |
| **M6: Frontend Migration** | Week 10 (Apr 20) | ⏳ TODO | M5 |
| **M7: Validation Complete** | Week 11 (Apr 27) | ⏳ TODO | M6 |
| **M8: Final Cleanup** | Week 12 (Apr 30) | ⏳ TODO | M7 |

---

## Weekly Sync Agenda

**When:** Every Tuesday, 2-3 PM

**Attendees:** All team leads + PM

**Agenda:**

1. **Progress Review** (15 min)
   - Tasks completed since last sync
   - Blockers and issues
   - Milestone status

2. **Risk Review** (10 min)
   - New risks identified
   - Risk mitigation progress
   - Escalations needed

3. **Coordination** (15 min)
   - Cross-team dependencies
   - Upcoming tasks
   - Resource needs

4. **Metrics Review** (10 min)
   - Deprecated endpoint usage
   - New endpoint adoption
   - Error rates
   - Performance benchmarks

5. **Next Week Planning** (10 min)
   - Assignments for upcoming week
   - Any adjustments needed

---

## Contact Information

**Project Channels:**
- Slack: `#flow-api-migration`
- Email: `flow-refactoring@example.com`
- Jira Board: `FLOW-REFACTOR`

**Office Hours:**
- Tuesdays 2-3 PM: Migration Q&A
- Thursdays 10-11 AM: Pair programming support

**Escalation Path:**
1. Task Owner
2. Team Lead
3. Engineering Manager
4. CTO

---

**Document Version:** 1.0

**Last Updated:** February 9, 2026

**Next Review:** February 16, 2026 (after M1)

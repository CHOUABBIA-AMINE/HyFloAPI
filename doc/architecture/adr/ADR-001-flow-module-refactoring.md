# ADR-001: Flow Module Architecture Refactoring

## Status

**ACCEPTED** - February 9, 2026

## Context

### Current Situation

The Flow module contains significant architectural duplication between two parallel subsystems:

- **`flow/core`** - Originally intended for CRUD operations and basic reading management
- **`flow/intelligence`** - Created for operational intelligence and analytics

### Identified Problems

1. **Critical Code Duplication**
   - Slot coverage calculation implemented independently in both modules
   - Both use identical business rules (12 slots, overdue detection, status counting)
   - Core uses SQL aggregation, Intelligence uses in-memory Java computation
   - Results may diverge due to different implementation approaches

2. **Responsibility Overlap**
   - Core module contains extensive monitoring and statistics logic
   - Intelligence module duplicates monitoring functionality from core
   - Unclear ownership: developers confused about where to add new features
   - Both modules compute same KPIs and operational metrics

3. **API Confusion**
   - Two parallel monitoring APIs exposing similar data
   - Inconsistent endpoint patterns (POST vs GET for same operations)
   - Different URL structures for same business capabilities
   - Frontend teams must choose between redundant endpoints

4. **Maintenance Burden**
   - Changes to business rules require updates in two places
   - ~500 lines of duplicated logic across modules
   - Risk of inconsistent results if one implementation updated but not the other
   - Technical debt accumulating with each new feature

5. **Performance Inconsistency**
   - Core uses efficient database aggregation
   - Intelligence loads data and aggregates in-memory
   - Same metrics computed with different performance characteristics
   - Potential for scalability issues as data grows

### Business Impact

- **Development Velocity**: Slower feature development due to duplicate implementations
- **Quality Risk**: Inconsistent results between core and intelligence endpoints
- **Maintenance Cost**: Higher cost to maintain two parallel implementations
- **Developer Experience**: Confusion about module boundaries and responsibilities

## Decision

We will **refactor the Flow module** to establish clear separation of concerns:

### Responsibility Split

**`flow/core` owns:**
- CRUD operations (Create, Read, Update, Delete)
- Reading submission and data persistence
- Validation workflow (state transitions: draft → submitted → approved/rejected)
- Basic data access queries
- Event publishing for domain events

**`flow/intelligence` owns:**
- All analytics and KPI computations
- Slot coverage calculation and monitoring
- Statistics aggregation (daily, weekly, monthly)
- Time-series analysis and trends
- Overdue detection and monitoring alerts
- Completion rate calculations
- Validator workload analysis

**`flow/common` provides:**
- Shared DTOs (unified representations)
- Shared repositories (read-only queries used by both)
- Common domain entities

### Architectural Principles

1. **Single Responsibility**: Each module owns one clear responsibility
2. **Single Source of Truth**: Each business capability implemented once
3. **Clear Dependencies**: Intelligence depends on Core, never the reverse
4. **State vs Analysis**: Core changes state, Intelligence analyzes state

### Decision Rule

**Simple heuristic for future features:**
- Does it **change state**? → Place in `core`
- Does it **analyze state**? → Place in `intelligence`

## Implementation Strategy

### Phase 1: Preparation (Week 1)
- Document architecture decision (this ADR)
- Create migration guide for API consumers
- Add deprecation warnings to old endpoints
- Communicate changes to all stakeholders

### Phase 2: Create New Repositories (Week 2)
- `PipelineStatisticsRepository` - KPI aggregation queries
- `SlotCoverageRepository` - Slot monitoring queries
- `TimeSeriesRepository` - Historical analysis queries
- Split `FlowReadingRepository` into Query and Command repositories

### Phase 3: Create New Services (Week 3)
- `SlotCoverageService` - Centralized slot coverage logic
- `StatisticsService` - All statistical calculations
- `TimeSeriesService` - Trend analysis
- `FlowReadingQueryService` - Shared queries

### Phase 4: Create Unified DTOs (Week 3)
- `SlotCoverageDTO` - Replaces SlotStatusDTO + SlotCoverageResponseDTO
- `TimeSeriesDTO` - Replaces ReadingsTimeSeriesDTO + SubmissionTrendDTO
- `OperationalStatisticsDTO` - Replaces DailyCompletionStatisticsDTO
- `MeasurementStatisticsDTO` - Replaces StatisticalSummaryDTO
- `WorkloadDTO` - Replaces ValidatorWorkloadDTO
- `PipelineMonitoringDTO` - Unified dashboard view
- `StructureOverviewDTO` - Structure-wide monitoring

### Phase 5: Refactor Intelligence Module (Week 4)
- Update `PipelineIntelligenceService` to use new services
- Remove in-memory aggregation logic
- Use repository queries instead of Java calculations
- Add new structure-wide endpoints to controller

### Phase 6: Clean Up Core Module (Week 5)
- Remove monitoring logic from `FlowReadingService`
- Remove monitoring endpoints from `FlowReadingController`
- Remove analytics queries from `FlowReadingRepository`
- Keep only workflow and CRUD operations

### Phase 7: Migration & Validation (Week 6-8)
- Update frontend applications to use new APIs
- Run parallel implementations and compare results
- Performance testing and optimization
- Delete deprecated code after grace period

### Rollout Strategy

**Feature Flags:**
```yaml
flow:
  intelligence:
    enabled: true
    use-new-endpoints: true
  core:
    monitoring:
      deprecated-endpoints-enabled: true  # Grace period
```

**Gradual Migration:**
1. Both old and new endpoints active for 2 release cycles
2. Deprecation warnings in old endpoints
3. Monitor usage metrics
4. Remove old endpoints when usage drops to zero

**Rollback Plan:**
- Feature flags allow instant rollback
- No database schema changes (safe)
- Backup of original files before changes

## Consequences

### Positive

1. **Clear Architecture**
   - Obvious module boundaries
   - Single responsibility per module
   - Predictable place to add new features

2. **Single Source of Truth**
   - One implementation of slot coverage
   - One set of statistical calculations
   - Consistent results across system

3. **Improved Maintainability**
   - Changes in one place only
   - ~500 lines of code eliminated
   - Reduced cognitive load

4. **Better Performance**
   - All statistics use SQL aggregation
   - No in-memory calculations
   - Better database index utilization

5. **Cleaner API**
   - Logical URL structure
   - Consistent endpoint patterns
   - Self-documenting paths

6. **Easier Testing**
   - Clear service boundaries
   - Easier to mock dependencies
   - More focused unit tests

### Negative

1. **Breaking Changes**
   - API consumers must update endpoint URLs
   - Response DTOs have different structures
   - HTTP methods change (POST → GET for some)
   - **Mitigation**: 2-cycle grace period, detailed migration guide

2. **Implementation Effort**
   - ~6-8 weeks of development time
   - 50+ file operations (create, update, delete)
   - Comprehensive testing required
   - **Mitigation**: Phased approach, feature flags

3. **Team Coordination**
   - Backend, frontend, and QA teams must coordinate
   - Multiple code reviews required
   - Communication overhead
   - **Mitigation**: Clear migration guide, regular sync meetings

4. **Risk During Migration**
   - Potential for bugs during transition
   - Two parallel implementations temporarily
   - More complex codebase during migration
   - **Mitigation**: Feature flags, parallel validation, rollback plan

5. **Documentation Updates**
   - API docs must be updated
   - Architecture diagrams need refresh
   - Developer guides require rewrite
   - **Mitigation**: Documentation as part of each phase

### Risks & Mitigation

| Risk | Probability | Impact | Mitigation |
|------|------------|--------|------------|
| Breaking frontend | High | High | Grace period, migration guide, parallel endpoints |
| Performance regression | Medium | High | Benchmark before/after, load testing |
| Inconsistent results | Medium | Critical | Parallel validation, integration tests |
| Team coordination | High | Medium | Regular syncs, clear ownership |
| Scope creep | Medium | Medium | Strict phase boundaries, no new features |

## Validation Metrics

### Success Criteria

1. **Code Quality**
   - [ ] Zero duplicated business logic
   - [ ] Test coverage ≥ 80% for all new code
   - [ ] All SonarQube quality gates passed

2. **Performance**
   - [ ] Statistics queries execute in < 200ms (95th percentile)
   - [ ] No N+1 query problems
   - [ ] Database CPU usage unchanged or improved

3. **API Consistency**
   - [ ] All intelligence endpoints follow same URL pattern
   - [ ] Consistent response formats
   - [ ] Proper HTTP status codes

4. **Migration Success**
   - [ ] Frontend successfully migrated to new endpoints
   - [ ] Zero production incidents related to refactoring
   - [ ] Deprecated endpoint usage drops to zero

5. **Documentation**
   - [ ] ADR completed and approved
   - [ ] Migration guide published
   - [ ] API documentation updated
   - [ ] Architecture diagrams updated

## Alternatives Considered

### Alternative 1: Keep Current Structure

**Description**: Accept duplication as technical debt, document module boundaries

**Pros:**
- No breaking changes
- Zero implementation effort
- No risk during migration

**Cons:**
- Duplication continues to grow
- Maintenance burden increases
- Developer confusion persists
- Risk of divergent results

**Decision**: Rejected - Technical debt compounds over time

### Alternative 2: Merge Into Single Module

**Description**: Combine core and intelligence into unified flow module

**Pros:**
- No duplication possible
- Single source of truth
- Simpler dependency graph

**Cons:**
- Loses separation of concerns
- Large, monolithic module
- Difficult to navigate
- Violates single responsibility principle

**Decision**: Rejected - Loses architectural clarity

### Alternative 3: Extract Common Module

**Description**: Create `flow/monitoring` module for shared monitoring logic

**Pros:**
- Clear ownership of monitoring logic
- Both modules depend on it
- Reusable across other modules

**Cons:**
- Adds complexity (3 modules instead of 2)
- Intelligence becomes thin wrapper
- Doesn't align with "Intelligence = Analytics" principle

**Decision**: Rejected - Intelligence should own analytics directly

### Alternative 4: Microservices Split

**Description**: Split into separate microservices (Core Service, Intelligence Service)

**Pros:**
- Complete isolation
- Independent deployment
- Technology flexibility

**Cons:**
- Massive overhead (network calls, distributed tracing, etc.)
- Overengineering for current scale
- Requires infrastructure changes
- Higher operational complexity

**Decision**: Rejected - Overkill for current needs

## References

- [Flow Module Architecture Analysis](../analysis/flow-module-duplication-analysis.md)
- [Migration Guide](../../migration/flow-api-migration-guide.md)
- [Implementation Plan](../implementation/flow-refactoring-implementation-plan.md)
- [Clean Architecture Principles](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)

## Decision Log

| Date | Author | Decision | Rationale |
|------|--------|----------|----------|
| 2026-02-07 | Architecture Team | Initial analysis completed | Identified critical duplication |
| 2026-02-09 | Tech Leads | ADR approved | Consensus on refactoring approach |
| 2026-02-09 | Engineering Manager | Phase 1 approved | Begin documentation phase |

## Stakeholders

**Approvers:**
- [ ] Technical Lead - Backend
- [ ] Technical Lead - Frontend
- [ ] Engineering Manager
- [ ] Product Owner

**Reviewers:**
- [ ] Backend Development Team
- [ ] Frontend Development Team
- [ ] QA Team
- [ ] DevOps Team

**Informed:**
- [ ] Product Management
- [ ] Customer Support
- [ ] Documentation Team

---

**Next Steps:**
1. ✅ Create this ADR
2. ✅ Create migration guide
3. ✅ Create detailed task breakdown
4. 🔲 Present to stakeholders
5. 🔲 Get approvals
6. 🔲 Begin Phase 2 implementation

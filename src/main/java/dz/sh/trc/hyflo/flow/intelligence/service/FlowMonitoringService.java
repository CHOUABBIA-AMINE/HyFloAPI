/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowMonitoringService
 * 	@CreatedOn	: 02-10-2026
 * 	@UpdatedOn	: 02-10-2026 - Refactored to use DateTimeUtils utility
 * 	@UpdatedOn	: 02-10-2026 - Phase 1 refactoring: Eliminated direct repository access
 * 	@UpdatedOn	: 02-10-2026 - Phase 4 refactoring: Added date range validation
 * 	@UpdatedOn	: 02-10-2026 - Phase 2: Facades return DTOs, no conversion needed
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Intelligence
 *
 * 	@Description: Service for operational monitoring and analytics.
 * 	              Extracted from FlowReadingService to separate concerns.
 *
 * 	@Refactoring: Phase 1 - Removed direct FlowReadingRepository dependency.
 * 	              Now uses FlowReadingFacade and IntelligenceQueryRepository exclusively.
 * 	              
 * 	              Benefits:
 * 	              - Enforces module boundaries (no direct core repository access)
 * 	              - Improves testability (mock facade instead of repository)
 * 	              - Clear separation: facade for queries, repository for analytics
 * 	
 * 	@Refactoring: Phase 4 - Performance Optimization:
 * 	              - Added date range validation (max 90 days)
 * 	              - Prevents performance issues with large result sets
 * 	              - Validates input parameters before executing expensive queries
 *
 * 	@Refactoring: Phase 2 - Facades now return DTOs directly:
 * 	              - Removed .map(FlowReadingDTO::fromEntity) calls
 * 	              - Facades handle entity→DTO conversion
 * 	              - Service works exclusively with DTOs
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.DailyCompletionStatisticsDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.PipelineCoverageDetailDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.SubmissionTrendDTO;
import dz.sh.trc.hyflo.flow.intelligence.dto.analytics.ValidatorWorkloadDTO;
import dz.sh.trc.hyflo.flow.intelligence.facade.IFlowReadingFacade;
import dz.sh.trc.hyflo.flow.intelligence.repository.IntelligenceQueryRepository;
import dz.sh.trc.hyflo.flow.intelligence.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service providing operational monitoring and analytics for flow readings.
 * 
 * This service handles:
 * - Pending validations tracking (via facade)
 * - Overdue readings detection (via facade)
 * - Daily completion statistics (via IntelligenceQueryRepository)
 * - Validator workload distribution (via IntelligenceQueryRepository)
 * - Submission trend analysis (via IntelligenceQueryRepository)
 * - Pipeline coverage reporting (via IntelligenceQueryRepository)
 * 
 * Architecture Pattern:
 * - Uses FlowReadingFacade for simple queries (enforces module boundary)
 * - Uses IntelligenceQueryRepository for complex analytics (native SQL)
 * - NO direct access to FlowReadingRepository (core module)
 * - NO entity dependencies (works exclusively with DTOs)
 * 
 * Performance:
 * - Date range validation enforced (max 90 days for analytics)
 * - JOIN FETCH used in repository queries to prevent N+1 issues
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowMonitoringService {

    // ========== DEPENDENCIES (Phase 1 Refactored) ==========
    
    /**
     * Facade for accessing core flow reading queries
     * REFACTORED (Phase 1): Now using facade instead of direct FlowReadingRepository access
     * REFACTORED (Phase 2): Facade now returns DTOs instead of entities
     */
    private final IFlowReadingFacade flowReadingFacade;
    
    /**
     * Repository for complex analytics queries
     * NEW: Dedicated repository for intelligence-specific queries
     */
    private final IntelligenceQueryRepository intelligenceQueryRepository;

    // ... rest of class unchanged ...
}

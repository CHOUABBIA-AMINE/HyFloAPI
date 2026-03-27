/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingQueryService
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service (Query)
 *  @Package    : Flow / Core
 *
 *  @Description: Query contract for FlowReading read operations.
 *                Returns only FlowReadingReadDTO — never raw entities.
 *                All filtering and pagination belongs here.
 *                No write operations permitted in implementations.
 *
 *  Phase 3 — Commit 16
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDTO;

/**
 * Query contract for FlowReading read operations.
 *
 * Returns only FlowReadingReadDTO — never raw entities.
 * All filtering and pagination belongs here.
 * No write operations permitted in implementations.
 *
 * Phase 3 — Commit 16
 */
public interface FlowReadingQueryService {

    FlowReadingReadDTO getById(Long id);

    Page<FlowReadingReadDTO> getAll(Pageable pageable);

    Page<FlowReadingReadDTO> search(String query, Pageable pageable);

    List<FlowReadingReadDTO> getByPipeline(Long pipelineId);

    List<FlowReadingReadDTO> getByDateRange(LocalDate from, LocalDate to);

    List<FlowReadingReadDTO> getByPipelineAndDateRange(Long pipelineId, LocalDate from, LocalDate to);

    List<FlowReadingReadDTO> getByPipelineAndSlot(Long pipelineId, Long slotId);

    List<FlowReadingReadDTO> getByValidationStatus(Long validationStatusId);

    /**
     * Return the most recent readings for a pipeline, ordered by readingDate DESC.
     * Used for operational dashboards and quick status overview.
     */
    List<FlowReadingReadDTO> getLatestByPipeline(Long pipelineId, int limit);
}

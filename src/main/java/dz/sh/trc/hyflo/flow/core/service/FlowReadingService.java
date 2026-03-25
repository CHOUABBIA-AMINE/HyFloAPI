/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowReadingService
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-25-2026 — Commit 17: converted to transitional delegator (Phase 3)
 *
 *  @Type       : Class
 *  @Layer      : Service (Transitional — Legacy Compatibility)
 *  @Package    : Flow / Core
 *
 *  @Description: Transitional delegator retained for controller compilation compatibility.
 *                All read logic delegates to FlowReadingQueryService.
 *                All write operations throw UnsupportedOperationException.
 *                To be removed in Phase 4 during controller migration.
 *
 *  @Deprecated since v2, forRemoval = true
 *  TODO(Phase 4): remove after controller migration to FlowReadingCommandService / FlowReadingQueryService
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingReadDto;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Transitional delegator — retained for controller compilation compatibility.
 *
 * All read logic delegates to FlowReadingQueryService.
 * All write operations throw UnsupportedOperationException —
 * use FlowReadingCommandService instead.
 *
 * @deprecated since v2 — to be removed in Phase 4 controller migration.
 * TODO(Phase 4): migrate controller to FlowReadingCommandService / FlowReadingQueryService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Deprecated(since = "v2", forRemoval = true)
public class FlowReadingService extends GenericService<FlowReading, FlowReadingReadDto, Long> {

    private final FlowReadingRepository flowReadingRepository;
    private final FlowReadingQueryService flowReadingQueryService;

    @Override
    protected JpaRepository<FlowReading, Long> getRepository() {
        return flowReadingRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowReading";
    }

    @Override
    protected FlowReadingReadDto toDTO(FlowReading entity) {
        return flowReadingQueryService.getById(entity.getId());
    }

    @Override
    protected FlowReading toEntity(FlowReadingReadDto dto) {
        throw new UnsupportedOperationException(
                "Use FlowReadingCommandService.createReading() for write operations.");
    }

    @Override
    protected void updateEntityFromDTO(FlowReading entity, FlowReadingReadDto dto) {
        throw new UnsupportedOperationException(
                "Use FlowReadingCommandService.updateReading() for update operations.");
    }

    @Override
    protected Page<FlowReadingReadDto> searchByQuery(String query, Pageable pageable) {
        return flowReadingQueryService.search(query, pageable);
    }

    // =====================================================================
    // Transitional delegates — controllers can call these until Phase 4
    // =====================================================================

    public List<FlowReadingReadDto> getByPipelineId(Long pipelineId) {
        log.debug("[TRANSITIONAL] getByPipelineId delegating to FlowReadingQueryService");
        return flowReadingQueryService.getByPipeline(pipelineId);
    }

    public List<FlowReadingReadDto> getByDateRange(LocalDate from, LocalDate to) {
        log.debug("[TRANSITIONAL] getByDateRange delegating to FlowReadingQueryService");
        return flowReadingQueryService.getByDateRange(from, to);
    }

    public List<FlowReadingReadDto> getByPipelineAndDateRange(
            Long pipelineId, LocalDate from, LocalDate to) {
        log.debug("[TRANSITIONAL] getByPipelineAndDateRange delegating to FlowReadingQueryService");
        return flowReadingQueryService.getByPipelineAndDateRange(pipelineId, from, to);
    }
}

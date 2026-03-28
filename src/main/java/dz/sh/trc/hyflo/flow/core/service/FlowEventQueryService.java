/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowEventQueryService
 *  @CreatedOn  : 03-26-2026
 *  @UpdatedOn  : 03-28-2026 — refactor: converted from concrete class to interface
 *
 *  @Type       : Interface
 *  @Layer      : Service (Query)
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import dz.sh.trc.hyflo.flow.core.dto.FlowEventReadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface FlowEventQueryService {

    FlowEventReadDTO findById(Long id);

    Page<FlowEventReadDTO> findAll(Pageable pageable);

    List<FlowEventReadDTO> findAll();

    List<FlowEventReadDTO> findByInfrastructure(Long infrastructureId);

    List<FlowEventReadDTO> findBySeverity(Long severityId);

    List<FlowEventReadDTO> findByStatus(Long statusId);

    List<FlowEventReadDTO> findByImpactOnFlow(Boolean impactOnFlow);

    List<FlowEventReadDTO> findByTimeRange(LocalDateTime start, LocalDateTime end);

    Page<FlowEventReadDTO> findByInfrastructureAndTimeRange(
            Long infrastructureId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<FlowEventReadDTO> findBySeverityAndTimeRange(
            Long severityId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<FlowEventReadDTO> findByStatusAndTimeRange(
            Long statusId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<FlowEventReadDTO> findWithImpactOnFlowByTimeRange(
            LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<FlowEventReadDTO> globalSearch(String searchTerm, Pageable pageable);
}

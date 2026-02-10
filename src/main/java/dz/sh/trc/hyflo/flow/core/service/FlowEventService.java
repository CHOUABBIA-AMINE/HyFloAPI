/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowEventService
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.flow.core.dto.entity.FlowEventDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowEvent;
import dz.sh.trc.hyflo.flow.core.repository.FlowEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowEventService extends GenericService<FlowEvent, FlowEventDTO, Long> {

    private final FlowEventRepository flowEventRepository;

    @Override
    protected JpaRepository<FlowEvent, Long> getRepository() {
        return flowEventRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowEvent";
    }

    @Override
    protected FlowEventDTO toDTO(FlowEvent entity) {
        return FlowEventDTO.fromEntity(entity);
    }

    @Override
    protected FlowEvent toEntity(FlowEventDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FlowEvent entity, FlowEventDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public FlowEventDTO create(FlowEventDTO dto) {
        log.info("Creating flow event: title={}, infrastructureId={}", 
                 dto.getTitle(), dto.getInfrastructureId());
        return super.create(dto);
    }

    public List<FlowEventDTO> getAll() {
        log.debug("Getting all flow events without pagination");
        return flowEventRepository.findAll().stream()
                .map(FlowEventDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowEventDTO> findByInfrastructure(Long infrastructureId) {
        log.debug("Finding flow events by infrastructure id: {}", infrastructureId);
        return flowEventRepository.findByInfrastructureId(infrastructureId).stream()
                .map(FlowEventDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowEventDTO> findBySeverity(Long severityId) {
        log.debug("Finding flow events by severity id: {}", severityId);
        return flowEventRepository.findBySeverityId(severityId).stream()
                .map(FlowEventDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowEventDTO> findByStatus(Long statusId) {
        log.debug("Finding flow events by status id: {}", statusId);
        return flowEventRepository.findByStatusId(statusId).stream()
                .map(FlowEventDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowEventDTO> findByImpactOnFlow(Boolean impactOnFlow) {
        log.debug("Finding flow events by impact on flow: {}", impactOnFlow);
        return flowEventRepository.findByImpactOnFlow(impactOnFlow).stream()
                .map(FlowEventDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowEventDTO> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Finding flow events by time range: {} to {}", startTime, endTime);
        return flowEventRepository.findByEventTimestampBetween(startTime, endTime).stream()
                .map(FlowEventDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<FlowEventDTO> findByInfrastructureAndTimeRange(
            Long infrastructureId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        log.debug("Finding flow events by infrastructure {} and time range: {} to {}", 
                  infrastructureId, startTime, endTime);
        return executeQuery(p -> flowEventRepository.findByInfrastructureAndTimeRange(
                infrastructureId, startTime, endTime, p), pageable);
    }

    public Page<FlowEventDTO> findBySeverityAndTimeRange(
            Long severityId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        log.debug("Finding flow events by severity {} and time range: {} to {}", 
                  severityId, startTime, endTime);
        return executeQuery(p -> flowEventRepository.findBySeverityAndTimeRange(
                severityId, startTime, endTime, p), pageable);
    }

    public Page<FlowEventDTO> findByStatusAndTimeRange(
            Long statusId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        log.debug("Finding flow events by status {} and time range: {} to {}", 
                  statusId, startTime, endTime);
        return executeQuery(p -> flowEventRepository.findByStatusAndTimeRange(
                statusId, startTime, endTime, p), pageable);
    }

    public Page<FlowEventDTO> findWithImpactOnFlowByTimeRange(
            LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        log.debug("Finding flow events with impact on flow by time range: {} to {}", 
                  startTime, endTime);
        return executeQuery(p -> flowEventRepository.findWithImpactOnFlowByTimeRange(
                startTime, endTime, p), pageable);
    }

    public Page<FlowEventDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for flow events with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> flowEventRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}

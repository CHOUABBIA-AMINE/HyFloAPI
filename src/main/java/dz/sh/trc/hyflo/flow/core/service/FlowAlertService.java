/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowAlertService
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
import dz.sh.trc.hyflo.flow.core.dto.FlowAlertDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowAlert;
import dz.sh.trc.hyflo.flow.core.repository.FlowAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowAlertService extends GenericService<FlowAlert, FlowAlertDTO, Long> {

    private final FlowAlertRepository flowAlertRepository;

    @Override
    protected JpaRepository<FlowAlert, Long> getRepository() {
        return flowAlertRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowAlert";
    }

    @Override
    protected FlowAlertDTO toDTO(FlowAlert entity) {
        return FlowAlertDTO.fromEntity(entity);
    }

    @Override
    protected FlowAlert toEntity(FlowAlertDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FlowAlert entity, FlowAlertDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public FlowAlertDTO create(FlowAlertDTO dto) {
        log.info("Creating flow alert: thresholdId={}, alertTimestamp={}", 
                 dto.getThresholdId(), dto.getAlertTimestamp());
        return super.create(dto);
    }

    public List<FlowAlertDTO> getAll() {
        log.debug("Getting all flow alerts without pagination");
        return flowAlertRepository.findAll().stream()
                .map(FlowAlertDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowAlertDTO> findByThreshold(Long thresholdId) {
        log.debug("Finding flow alerts by threshold id: {}", thresholdId);
        return flowAlertRepository.findByThresholdId(thresholdId).stream()
                .map(FlowAlertDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowAlertDTO> findByFlowReading(Long flowReadingId) {
        log.debug("Finding flow alerts by flow reading id: {}", flowReadingId);
        return flowAlertRepository.findByFlowReadingId(flowReadingId).stream()
                .map(FlowAlertDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowAlertDTO> findByStatus(Long statusId) {
        log.debug("Finding flow alerts by status id: {}", statusId);
        return flowAlertRepository.findByStatusId(statusId).stream()
                .map(FlowAlertDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowAlertDTO> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Finding flow alerts by time range: {} to {}", startTime, endTime);
        return flowAlertRepository.findByAlertTimestampBetween(startTime, endTime).stream()
                .map(FlowAlertDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowAlertDTO> findByNotificationSent(Boolean sent) {
        log.debug("Finding flow alerts by notification sent: {}", sent);
        return flowAlertRepository.findByNotificationSent(sent).stream()
                .map(FlowAlertDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<FlowAlertDTO> findByStatusAndTimeRange(
            Long statusId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        log.debug("Finding flow alerts by status {} and time range: {} to {}", 
                  statusId, startTime, endTime);
        return executeQuery(p -> flowAlertRepository.findByStatusAndTimeRange(
                statusId, startTime, endTime, p), pageable);
    }

    public Page<FlowAlertDTO> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        log.debug("Finding flow alerts by pipeline {} and time range: {} to {}", 
                  pipelineId, startTime, endTime);
        return executeQuery(p -> flowAlertRepository.findByPipelineAndTimeRange(
                pipelineId, startTime, endTime, p), pageable);
    }

    public Page<FlowAlertDTO> findUnacknowledged(Pageable pageable) {
        log.debug("Finding unacknowledged flow alerts");
        return executeQuery(p -> flowAlertRepository.findUnacknowledged(p), pageable);
    }

    public Page<FlowAlertDTO> findUnresolved(Pageable pageable) {
        log.debug("Finding unresolved flow alerts");
        return executeQuery(p -> flowAlertRepository.findUnresolved(p), pageable);
    }

    public Page<FlowAlertDTO> findUnresolvedByPipeline(Long pipelineId, Pageable pageable) {
        log.debug("Finding unresolved flow alerts by pipeline: {}", pipelineId);
        return executeQuery(p -> flowAlertRepository.findUnresolvedByPipeline(pipelineId, p), pageable);
    }
}

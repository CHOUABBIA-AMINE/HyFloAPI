/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowReadingService
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
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.flow.core.dto.FlowReadingDTO;
import dz.sh.trc.hyflo.flow.core.model.FlowReading;
import dz.sh.trc.hyflo.flow.core.repository.FlowReadingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowReadingService extends GenericService<FlowReading, FlowReadingDTO, Long> {

    private final FlowReadingRepository flowReadingRepository;

    @Override
    protected JpaRepository<FlowReading, Long> getRepository() {
        return flowReadingRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowReading";
    }

    @Override
    protected FlowReadingDTO toDTO(FlowReading entity) {
        return FlowReadingDTO.fromEntity(entity);
    }

    @Override
    protected FlowReading toEntity(FlowReadingDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FlowReading entity, FlowReadingDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public FlowReadingDTO create(FlowReadingDTO dto) {
        log.info("Creating flow reading: pipelineId={}, recordedAt={}", 
                 dto.getPipelineId(), dto.getRecordedAt());
        
        if (flowReadingRepository.existsByPipelineIdAndRecordedAt(
                dto.getPipelineId(), dto.getRecordedAt())) {
            throw new BusinessValidationException(
                "Flow reading for this pipeline and timestamp already exists");
        }
        
        return super.create(dto);
    }

    public List<FlowReadingDTO> getAll() {
        log.debug("Getting all flow readings without pagination");
        return flowReadingRepository.findAll().stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowReadingDTO> findByPipeline(Long pipelineId) {
        log.debug("Finding flow readings by pipeline id: {}", pipelineId);
        return flowReadingRepository.findByPipelineId(pipelineId).stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowReadingDTO> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Finding flow readings by time range: {} to {}", startTime, endTime);
        return flowReadingRepository.findByRecordedAtBetween(startTime, endTime).stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowReadingDTO> findByPipelineAndTimeRange(
            Long pipelineId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Finding flow readings by pipeline {} and time range: {} to {}", 
                  pipelineId, startTime, endTime);
        return flowReadingRepository.findByPipelineIdAndRecordedAtBetween(
                pipelineId, startTime, endTime).stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FlowReadingDTO> findByValidationStatus(Long validationStatusId) {
        log.debug("Finding flow readings by validation status id: {}", validationStatusId);
        return flowReadingRepository.findByValidationStatusId(validationStatusId).stream()
                .map(FlowReadingDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<FlowReadingDTO> findByPipelineAndTimeRangePaginated(
            Long pipelineId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        log.debug("Finding flow readings (paginated) by pipeline {} and time range: {} to {}", 
                  pipelineId, startTime, endTime);
        return executeQuery(p -> flowReadingRepository.findByPipelineAndTimeRange(
                pipelineId, startTime, endTime, p), pageable);
    }

    public Page<FlowReadingDTO> findByPipelineAndValidationStatus(
            Long pipelineId, Long statusId, Pageable pageable) {
        log.debug("Finding flow readings by pipeline {} and validation status {}", 
                  pipelineId, statusId);
        return executeQuery(p -> flowReadingRepository.findByPipelineAndValidationStatus(
                pipelineId, statusId, p), pageable);
    }

    public Page<FlowReadingDTO> findLatestByPipeline(Long pipelineId, Pageable pageable) {
        log.debug("Finding latest flow readings for pipeline: {}", pipelineId);
        return executeQuery(p -> flowReadingRepository.findLatestByPipeline(pipelineId, p), pageable);
    }
}

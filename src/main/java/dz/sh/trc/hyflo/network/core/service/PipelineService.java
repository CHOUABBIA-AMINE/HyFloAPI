/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PipelineService
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Network / Service
 *	@Package	: Network / Service
 *
 **/

package dz.sh.trc.hyflo.network.core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.common.repository.OperationalStatusRepository;
import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.network.core.model.Facility;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import dz.sh.trc.hyflo.network.core.model.PipelineSystem;
import dz.sh.trc.hyflo.network.core.repository.FacilityRepository;
import dz.sh.trc.hyflo.network.core.repository.PipelineRepository;
import dz.sh.trc.hyflo.network.core.repository.PipelineSystemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PipelineService extends GenericService<Pipeline, PipelineDTO, Long> {

    private final PipelineRepository pipelineRepository;
    private final OperationalStatusRepository operationalStatusRepository;
    private final PipelineSystemRepository pipelineSystemRepository;
    private final FacilityRepository facilityRepository;

    @Override
    protected JpaRepository<Pipeline, Long> getRepository() {
        return pipelineRepository;
    }

    @Override
    protected String getEntityName() {
        return "Pipeline";
    }

    @Override
    protected PipelineDTO toDTO(Pipeline entity) {
        return PipelineDTO.fromEntity(entity);
    }

    @Override
    protected Pipeline toEntity(PipelineDTO dto) {
        Pipeline entity = dto.toEntity();
        
        if (dto.getOperationalStatusId() != null) {
            OperationalStatus status = operationalStatusRepository.findById(dto.getOperationalStatusId())
                    .orElseThrow(() -> new RuntimeException("Operational status not found with id: " + dto.getOperationalStatusId()));
            entity.setOperationalStatus(status);
        }
        
        if (dto.getPipelineSystemId() != null) {
            PipelineSystem system = pipelineSystemRepository.findById(dto.getPipelineSystemId())
                    .orElseThrow(() -> new RuntimeException("Pipeline system not found with id: " + dto.getPipelineSystemId()));
            entity.setPipelineSystem(system);
        }
        
        if (dto.getDepartureFacilityId() != null) {
            Facility facility = facilityRepository.findById(dto.getDepartureFacilityId())
                    .orElseThrow(() -> new RuntimeException("Departure terminal not found with id: " + dto.getDepartureFacilityId()));
            entity.setDepartureFacility(facility);
        }
        
        if (dto.getArrivalFacilityId() != null) {
            Facility facility = facilityRepository.findById(dto.getArrivalFacilityId())
                    .orElseThrow(() -> new RuntimeException("Arrival terminal not found with id: " + dto.getArrivalFacilityId()));
            entity.setArrivalFacility(facility);
        }
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Pipeline entity, PipelineDTO dto) {
        dto.updateEntity(entity);
        
        if (dto.getOperationalStatusId() != null) {
            OperationalStatus status = operationalStatusRepository.findById(dto.getOperationalStatusId())
                    .orElseThrow(() -> new RuntimeException("Operational status not found with id: " + dto.getOperationalStatusId()));
            entity.setOperationalStatus(status);
        }
        
        if (dto.getPipelineSystemId() != null) {
            PipelineSystem system = pipelineSystemRepository.findById(dto.getPipelineSystemId())
                    .orElseThrow(() -> new RuntimeException("Pipeline system not found with id: " + dto.getPipelineSystemId()));
            entity.setPipelineSystem(system);
        }
        
        if (dto.getDepartureFacilityId() != null) {
            Facility facility = facilityRepository.findById(dto.getDepartureFacilityId())
                    .orElseThrow(() -> new RuntimeException("Departure terminal not found with id: " + dto.getDepartureFacilityId()));
            entity.setDepartureFacility(facility);
        }
        
        if (dto.getArrivalFacilityId() != null) {
            Facility facility = facilityRepository.findById(dto.getArrivalFacilityId())
                    .orElseThrow(() -> new RuntimeException("Arrival terminal not found with id: " + dto.getArrivalFacilityId()));
            entity.setArrivalFacility(facility);
        }
    }

    @Override
    @Transactional
    public PipelineDTO create(PipelineDTO dto) {
        log.info("Creating pipeline: code={}", dto.getCode());
        
        if (pipelineRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Pipeline with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public PipelineDTO update(Long id, PipelineDTO dto) {
        log.info("Updating pipeline with ID: {}", id);
        
        if (pipelineRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Pipeline with code '" + dto.getCode() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<PipelineDTO> getAll() {
        log.debug("Getting all pipelines without pagination");
        return pipelineRepository.findAll().stream()
                .map(PipelineDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<PipelineDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for pipelines with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> pipelineRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    public List<PipelineDTO> findByPipelineSystem(Long systemId) {
        log.debug("Finding pipelines by pipeline system id: {}", systemId);
        return pipelineRepository.findByPipelineSystemId(systemId).stream()
                .map(PipelineDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

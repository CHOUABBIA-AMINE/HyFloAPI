/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FlowPressureService
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Service
 *	@Layer		: Network / Service
 *	@Package	: Network / Service
 *
 **/

package dz.sh.trc.hyflo.network.flow.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.network.flow.dto.FlowPressureDTO;
import dz.sh.trc.hyflo.network.flow.model.FlowPressure;
import dz.sh.trc.hyflo.network.flow.repository.FlowPressureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowPressureService extends GenericService<FlowPressure, FlowPressureDTO, Long> {

    private final FlowPressureRepository flowRepository;

    @Override
    protected JpaRepository<FlowPressure, Long> getRepository() {
        return flowRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowPressure";
    }

    @Override
    protected FlowPressureDTO toDTO(FlowPressure entity) {
        return FlowPressureDTO.fromEntity(entity);
    }

    @Override
    protected FlowPressure toEntity(FlowPressureDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FlowPressure entity, FlowPressureDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public FlowPressureDTO create(FlowPressureDTO dto) {
        log.info("Creating FlowPressure: UK={}", dto.getMeasurementDate());
        
        /*if (flowRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("FlowPressure with code '" + dto.getCode() + "' already exists");
        }*/
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public FlowPressureDTO update(Long id, FlowPressureDTO dto) {
        log.info("Updating terminal with ID: {}", id);
        
        /*if (flowRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("FlowPressure with code '" + dto.getCode() + "' already exists");
        }*/
        
        return super.update(id, dto);
    }

    /*public Page<FlowPressureDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for terminals with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> flowRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }*/
}

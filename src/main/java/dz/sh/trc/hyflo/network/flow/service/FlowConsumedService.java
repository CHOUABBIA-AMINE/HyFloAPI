/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowConsumedService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.network.flow.dto.FlowConsumedDTO;
import dz.sh.trc.hyflo.network.flow.model.FlowConsumed;
import dz.sh.trc.hyflo.network.flow.repository.FlowConsumedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowConsumedService extends GenericService<FlowConsumed, FlowConsumedDTO, Long> {

    private final FlowConsumedRepository flowRepository;

    @Override
    protected JpaRepository<FlowConsumed, Long> getRepository() {
        return flowRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowConsumed";
    }

    @Override
    protected FlowConsumedDTO toDTO(FlowConsumed entity) {
        return FlowConsumedDTO.fromEntity(entity);
    }

    @Override
    protected FlowConsumed toEntity(FlowConsumedDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FlowConsumed entity, FlowConsumedDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public FlowConsumedDTO create(FlowConsumedDTO dto) {
        log.info("Creating FlowConsumed: UK={}", dto.getMeasurementDate());
        
        /*if (flowRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("FlowConsumed with code '" + dto.getCode() + "' already exists");
        }*/
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public FlowConsumedDTO update(Long id, FlowConsumedDTO dto) {
        log.info("Updating terminal with ID: {}", id);
        
        /*if (flowRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("FlowConsumed with code '" + dto.getCode() + "' already exists");
        }*/
        
        return super.update(id, dto);
    }

    /*public Page<FlowConsumedDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for terminals with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> flowRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }*/
}

/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowProducedService
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
import dz.sh.trc.hyflo.network.flow.dto.FlowProducedDTO;
import dz.sh.trc.hyflo.network.flow.model.FlowProduced;
import dz.sh.trc.hyflo.network.flow.repository.FlowProducedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowProducedService extends GenericService<FlowProduced, FlowProducedDTO, Long> {

    private final FlowProducedRepository flowRepository;

    @Override
    protected JpaRepository<FlowProduced, Long> getRepository() {
        return flowRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowProduced";
    }

    @Override
    protected FlowProducedDTO toDTO(FlowProduced entity) {
        return FlowProducedDTO.fromEntity(entity);
    }

    @Override
    protected FlowProduced toEntity(FlowProducedDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FlowProduced entity, FlowProducedDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public FlowProducedDTO create(FlowProducedDTO dto) {
        log.info("Creating FlowProduced: UK={}", dto.getMeasurementDate());
        
        /*if (flowRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("FlowProduced with code '" + dto.getCode() + "' already exists");
        }*/
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public FlowProducedDTO update(Long id, FlowProducedDTO dto) {
        log.info("Updating terminal with ID: {}", id);
        
        /*if (flowRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("FlowProduced with code '" + dto.getCode() + "' already exists");
        }*/
        
        return super.update(id, dto);
    }

    /*public Page<FlowProducedDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for terminals with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> flowRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }*/
}

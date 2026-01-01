/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FlowTransportedService
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Service
 *	@Layer		: Network / Service
 *	@Package	: Network / Service
 *
 **/

package dz.mdn.iaas.network.flow.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.mdn.iaas.configuration.template.GenericService;
import dz.mdn.iaas.network.flow.dto.FlowTransportedDTO;
import dz.mdn.iaas.network.flow.model.FlowTransported;
import dz.mdn.iaas.network.flow.repository.FlowTransportedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowTransportedService extends GenericService<FlowTransported, FlowTransportedDTO, Long> {

    private final FlowTransportedRepository flowRepository;

    @Override
    protected JpaRepository<FlowTransported, Long> getRepository() {
        return flowRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowTransported";
    }

    @Override
    protected FlowTransportedDTO toDTO(FlowTransported entity) {
        return FlowTransportedDTO.fromEntity(entity);
    }

    @Override
    protected FlowTransported toEntity(FlowTransportedDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FlowTransported entity, FlowTransportedDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public FlowTransportedDTO create(FlowTransportedDTO dto) {
        log.info("Creating FlowTransported: UK={}", dto.getMeasurementDate());
        
        /*if (flowRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("FlowTransported with code '" + dto.getCode() + "' already exists");
        }*/
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public FlowTransportedDTO update(Long id, FlowTransportedDTO dto) {
        log.info("Updating terminal with ID: {}", id);
        
        /*if (flowRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("FlowTransported with code '" + dto.getCode() + "' already exists");
        }*/
        
        return super.update(id, dto);
    }

    /*public Page<FlowTransportedDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for terminals with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> flowRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }*/
}

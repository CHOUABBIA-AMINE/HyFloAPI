/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FlowVolumeService
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
import dz.mdn.iaas.network.flow.dto.FlowVolumeDTO;
import dz.mdn.iaas.network.flow.model.FlowVolume;
import dz.mdn.iaas.network.flow.repository.FlowVolumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlowVolumeService extends GenericService<FlowVolume, FlowVolumeDTO, Long> {

    private final FlowVolumeRepository flowRepository;

    @Override
    protected JpaRepository<FlowVolume, Long> getRepository() {
        return flowRepository;
    }

    @Override
    protected String getEntityName() {
        return "FlowVolume";
    }

    @Override
    protected FlowVolumeDTO toDTO(FlowVolume entity) {
        return FlowVolumeDTO.fromEntity(entity);
    }

    @Override
    protected FlowVolume toEntity(FlowVolumeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FlowVolume entity, FlowVolumeDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public FlowVolumeDTO create(FlowVolumeDTO dto) {
        log.info("Creating FlowVolume: UK={}", dto.getMeasurementDate());
        
        /*if (flowRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("FlowVolume with code '" + dto.getCode() + "' already exists");
        }*/
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public FlowVolumeDTO update(Long id, FlowVolumeDTO dto) {
        log.info("Updating terminal with ID: {}", id);
        
        /*if (flowRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("FlowVolume with code '" + dto.getCode() + "' already exists");
        }*/
        
        return super.update(id, dto);
    }

    /*public Page<FlowVolumeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for terminals with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> flowRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }*/
}

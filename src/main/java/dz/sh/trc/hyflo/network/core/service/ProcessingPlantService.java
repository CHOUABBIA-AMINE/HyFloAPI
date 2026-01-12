/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProcessingPlantService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.network.core.dto.ProcessingPlantDTO;
import dz.sh.trc.hyflo.network.core.model.ProcessingPlant;
import dz.sh.trc.hyflo.network.core.repository.ProcessingPlantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProcessingPlantService extends GenericService<ProcessingPlant, ProcessingPlantDTO, Long> {

    private final ProcessingPlantRepository processingPlantRepository;

    @Override
    protected JpaRepository<ProcessingPlant, Long> getRepository() {
        return processingPlantRepository;
    }

    @Override
    protected String getEntityName() {
        return "ProcessingPlant";
    }

    @Override
    protected ProcessingPlantDTO toDTO(ProcessingPlant entity) {
        return ProcessingPlantDTO.fromEntity(entity);
    }

    @Override
    protected ProcessingPlant toEntity(ProcessingPlantDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ProcessingPlant entity, ProcessingPlantDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ProcessingPlantDTO create(ProcessingPlantDTO dto) {
        log.info("Creating hydrocarbon plant: code={}", dto.getCode());
        
        if (processingPlantRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Hydrocarbon plant with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public ProcessingPlantDTO update(Long id, ProcessingPlantDTO dto) {
        log.info("Updating hydrocarbon plant with ID: {}", id);
        
        if (processingPlantRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Hydrocarbon plant with code '" + dto.getCode() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public Page<ProcessingPlantDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for hydrocarbon plants with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> processingPlantRepository.searchByAnyPlant(searchTerm.trim(), p), pageable);
    }
}

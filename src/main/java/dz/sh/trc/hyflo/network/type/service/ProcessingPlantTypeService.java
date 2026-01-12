/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProcessingPlantTypeService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.network.type.dto.ProcessingPlantTypeDTO;
import dz.sh.trc.hyflo.network.type.model.ProcessingPlantType;
import dz.sh.trc.hyflo.network.type.repository.ProcessingPlantTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProcessingPlantTypeService extends GenericService<ProcessingPlantType, ProcessingPlantTypeDTO, Long> {

    private final ProcessingPlantTypeRepository processingPlantTypeRepository;

    @Override
    protected JpaRepository<ProcessingPlantType, Long> getRepository() {
        return processingPlantTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "ProcessingPlantType";
    }

    @Override
    protected ProcessingPlantTypeDTO toDTO(ProcessingPlantType entity) {
        return ProcessingPlantTypeDTO.fromEntity(entity);
    }

    @Override
    protected ProcessingPlantType toEntity(ProcessingPlantTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ProcessingPlantType entity, ProcessingPlantTypeDTO dto) {
        dto.updateEntity(entity);
    }

    public Page<ProcessingPlantTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for hydrocarbon plant types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> processingPlantTypeRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}

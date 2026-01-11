/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: HydrocarbonPlantService
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
import dz.sh.trc.hyflo.network.core.dto.HydrocarbonPlantDTO;
import dz.sh.trc.hyflo.network.core.model.HydrocarbonPlant;
import dz.sh.trc.hyflo.network.core.repository.HydrocarbonPlantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class HydrocarbonPlantService extends GenericService<HydrocarbonPlant, HydrocarbonPlantDTO, Long> {

    private final HydrocarbonPlantRepository hydrocarbonPlantRepository;

    @Override
    protected JpaRepository<HydrocarbonPlant, Long> getRepository() {
        return hydrocarbonPlantRepository;
    }

    @Override
    protected String getEntityName() {
        return "HydrocarbonPlant";
    }

    @Override
    protected HydrocarbonPlantDTO toDTO(HydrocarbonPlant entity) {
        return HydrocarbonPlantDTO.fromEntity(entity);
    }

    @Override
    protected HydrocarbonPlant toEntity(HydrocarbonPlantDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(HydrocarbonPlant entity, HydrocarbonPlantDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public HydrocarbonPlantDTO create(HydrocarbonPlantDTO dto) {
        log.info("Creating hydrocarbon field: code={}", dto.getCode());
        
        if (hydrocarbonPlantRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Hydrocarbon field with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public HydrocarbonPlantDTO update(Long id, HydrocarbonPlantDTO dto) {
        log.info("Updating hydrocarbon field with ID: {}", id);
        
        if (hydrocarbonPlantRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Hydrocarbon field with code '" + dto.getCode() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public Page<HydrocarbonPlantDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for hydrocarbon fields with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> hydrocarbonPlantRepository.searchByAnyPlant(searchTerm.trim(), p), pageable);
    }
}

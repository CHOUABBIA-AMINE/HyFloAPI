/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: HydrocarbonFieldService
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Service
 *	@Layer		: Network / Service
 *	@Package	: Network / Service
 *
 **/

package dz.mdn.iaas.network.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.mdn.iaas.configuration.template.GenericService;
import dz.mdn.iaas.exception.BusinessValidationException;
import dz.mdn.iaas.network.core.dto.HydrocarbonFieldDTO;
import dz.mdn.iaas.network.core.model.HydrocarbonField;
import dz.mdn.iaas.network.core.repository.HydrocarbonFieldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class HydrocarbonFieldService extends GenericService<HydrocarbonField, HydrocarbonFieldDTO, Long> {

    private final HydrocarbonFieldRepository hydrocarbonFieldRepository;

    @Override
    protected JpaRepository<HydrocarbonField, Long> getRepository() {
        return hydrocarbonFieldRepository;
    }

    @Override
    protected String getEntityName() {
        return "HydrocarbonField";
    }

    @Override
    protected HydrocarbonFieldDTO toDTO(HydrocarbonField entity) {
        return HydrocarbonFieldDTO.fromEntity(entity);
    }

    @Override
    protected HydrocarbonField toEntity(HydrocarbonFieldDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(HydrocarbonField entity, HydrocarbonFieldDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public HydrocarbonFieldDTO create(HydrocarbonFieldDTO dto) {
        log.info("Creating hydrocarbon field: code={}", dto.getCode());
        
        if (hydrocarbonFieldRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Hydrocarbon field with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public HydrocarbonFieldDTO update(Long id, HydrocarbonFieldDTO dto) {
        log.info("Updating hydrocarbon field with ID: {}", id);
        
        if (hydrocarbonFieldRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Hydrocarbon field with code '" + dto.getCode() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public Page<HydrocarbonFieldDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for hydrocarbon fields with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> hydrocarbonFieldRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}

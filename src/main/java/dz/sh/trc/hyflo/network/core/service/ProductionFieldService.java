/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProductionFieldService
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
import dz.sh.trc.hyflo.network.core.dto.ProductionFieldDTO;
import dz.sh.trc.hyflo.network.core.model.ProductionField;
import dz.sh.trc.hyflo.network.core.repository.ProductionFieldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductionFieldService extends GenericService<ProductionField, ProductionFieldDTO, Long> {

    private final ProductionFieldRepository hydrocarbonFieldRepository;

    @Override
    protected JpaRepository<ProductionField, Long> getRepository() {
        return hydrocarbonFieldRepository;
    }

    @Override
    protected String getEntityName() {
        return "ProductionField";
    }

    @Override
    protected ProductionFieldDTO toDTO(ProductionField entity) {
        return ProductionFieldDTO.fromEntity(entity);
    }

    @Override
    protected ProductionField toEntity(ProductionFieldDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ProductionField entity, ProductionFieldDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ProductionFieldDTO create(ProductionFieldDTO dto) {
        log.info("Creating hydrocarbon field: code={}", dto.getCode());
        
        if (hydrocarbonFieldRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Hydrocarbon field with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public ProductionFieldDTO update(Long id, ProductionFieldDTO dto) {
        log.info("Updating hydrocarbon field with ID: {}", id);
        
        if (hydrocarbonFieldRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Hydrocarbon field with code '" + dto.getCode() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public Page<ProductionFieldDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for hydrocarbon fields with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> hydrocarbonFieldRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}

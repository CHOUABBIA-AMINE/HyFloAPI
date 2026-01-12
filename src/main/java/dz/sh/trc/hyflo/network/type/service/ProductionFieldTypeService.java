/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProductionFieldTypeService
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
import dz.sh.trc.hyflo.network.type.dto.ProductionFieldTypeDTO;
import dz.sh.trc.hyflo.network.type.model.ProductionFieldType;
import dz.sh.trc.hyflo.network.type.repository.ProductionFieldTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductionFieldTypeService extends GenericService<ProductionFieldType, ProductionFieldTypeDTO, Long> {

    private final ProductionFieldTypeRepository productionFieldTypeRepository;

    @Override
    protected JpaRepository<ProductionFieldType, Long> getRepository() {
        return productionFieldTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "ProductionFieldType";
    }

    @Override
    protected ProductionFieldTypeDTO toDTO(ProductionFieldType entity) {
        return ProductionFieldTypeDTO.fromEntity(entity);
    }

    @Override
    protected ProductionFieldType toEntity(ProductionFieldTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ProductionFieldType entity, ProductionFieldTypeDTO dto) {
        dto.updateEntity(entity);
    }

    public Page<ProductionFieldTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for hydrocarbon field types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> productionFieldTypeRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}

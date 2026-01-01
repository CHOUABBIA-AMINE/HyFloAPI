/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: HydrocarbonFieldTypeService
 *	@CreatedOn	: 12-19-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Service
 *	@Layer		: Network / Service
 *	@Package	: Network / Type / Service
 *
 **/

package dz.mdn.iaas.network.type.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.mdn.iaas.configuration.template.GenericService;
import dz.mdn.iaas.network.type.dto.HydrocarbonFieldTypeDTO;
import dz.mdn.iaas.network.type.model.HydrocarbonFieldType;
import dz.mdn.iaas.network.type.repository.HydrocarbonFieldTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class HydrocarbonFieldTypeService extends GenericService<HydrocarbonFieldType, HydrocarbonFieldTypeDTO, Long> {

    private final HydrocarbonFieldTypeRepository hydrocarbonFieldTypeRepository;

    @Override
    protected JpaRepository<HydrocarbonFieldType, Long> getRepository() {
        return hydrocarbonFieldTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "HydrocarbonFieldType";
    }

    @Override
    protected HydrocarbonFieldTypeDTO toDTO(HydrocarbonFieldType entity) {
        return HydrocarbonFieldTypeDTO.fromEntity(entity);
    }

    @Override
    protected HydrocarbonFieldType toEntity(HydrocarbonFieldTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(HydrocarbonFieldType entity, HydrocarbonFieldTypeDTO dto) {
        dto.updateEntity(entity);
    }

    public Page<HydrocarbonFieldTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for hydrocarbon field types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> hydrocarbonFieldTypeRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}

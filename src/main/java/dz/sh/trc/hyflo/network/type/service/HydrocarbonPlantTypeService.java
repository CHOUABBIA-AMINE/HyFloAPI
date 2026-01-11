/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: HydrocarbonPlantTypeService
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
import dz.sh.trc.hyflo.network.type.dto.HydrocarbonPlantTypeDTO;
import dz.sh.trc.hyflo.network.type.model.HydrocarbonPlantType;
import dz.sh.trc.hyflo.network.type.repository.HydrocarbonPlantTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class HydrocarbonPlantTypeService extends GenericService<HydrocarbonPlantType, HydrocarbonPlantTypeDTO, Long> {

    private final HydrocarbonPlantTypeRepository hydrocarbonPlantTypeRepository;

    @Override
    protected JpaRepository<HydrocarbonPlantType, Long> getRepository() {
        return hydrocarbonPlantTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "HydrocarbonPlantType";
    }

    @Override
    protected HydrocarbonPlantTypeDTO toDTO(HydrocarbonPlantType entity) {
        return HydrocarbonPlantTypeDTO.fromEntity(entity);
    }

    @Override
    protected HydrocarbonPlantType toEntity(HydrocarbonPlantTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(HydrocarbonPlantType entity, HydrocarbonPlantTypeDTO dto) {
        dto.updateEntity(entity);
    }

    public Page<HydrocarbonPlantTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for hydrocarbon field types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> hydrocarbonPlantTypeRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}

/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: StationTypeService
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
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
import dz.sh.trc.hyflo.network.type.dto.StationTypeDTO;
import dz.sh.trc.hyflo.network.type.model.StationType;
import dz.sh.trc.hyflo.network.type.repository.StationTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StationTypeService extends GenericService<StationType, StationTypeDTO, Long> {

    private final StationTypeRepository stationTypeRepository;

    @Override
    protected JpaRepository<StationType, Long> getRepository() {
        return stationTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "StationType";
    }

    @Override
    protected StationTypeDTO toDTO(StationType entity) {
        return StationTypeDTO.fromEntity(entity);
    }

    @Override
    protected StationType toEntity(StationTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(StationType entity, StationTypeDTO dto) {
        dto.updateEntity(entity);
    }

    public Page<StationTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for station types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> stationTypeRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}

/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: StationService
 *	@CreatedOn	: 12-19-2025
 *	@UpdatedOn	: 12-19-2025
 *
 *	@Type		: Service
 *	@Layer		: Network / Service
 *	@Package	: Network / Service
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
import dz.sh.trc.hyflo.network.core.dto.StationDTO;
import dz.sh.trc.hyflo.network.core.model.Station;
import dz.sh.trc.hyflo.network.core.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StationService extends GenericService<Station, StationDTO, Long> {

    private final StationRepository stationRepository;

    @Override
    protected JpaRepository<Station, Long> getRepository() {
        return stationRepository;
    }

    @Override
    protected String getEntityName() {
        return "Station";
    }

    @Override
    protected StationDTO toDTO(Station entity) {
        return StationDTO.fromEntity(entity);
    }

    @Override
    protected Station toEntity(StationDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Station entity, StationDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public StationDTO create(StationDTO dto) {
        log.info("Creating station: code={}", dto.getCode());
        
        if (stationRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Station with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public StationDTO update(Long id, StationDTO dto) {
        log.info("Updating station with ID: {}", id);
        
        if (stationRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Station with code '" + dto.getCode() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public Page<StationDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for stations with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> stationRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}

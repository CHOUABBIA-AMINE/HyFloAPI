/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: LocationService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.general.localization.dto.LocationDTO;
import dz.sh.trc.hyflo.general.localization.model.Location;
import dz.sh.trc.hyflo.general.localization.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LocationService extends GenericService<Location, LocationDTO, Long> {

    private final LocationRepository locationRepository;

    @Override
    protected JpaRepository<Location, Long> getRepository() {
        return locationRepository;
    }

    @Override
    protected String getEntityName() {
        return "Location";
    }

    @Override
    protected LocationDTO toDTO(Location entity) {
        return LocationDTO.fromEntity(entity);
    }

    @Override
    protected Location toEntity(LocationDTO dto) {
        Location entity = dto.toEntity();
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Location entity, LocationDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public LocationDTO create(LocationDTO dto) {
        log.info("Creating location: designationFr={}", dto.getDesignationFr());
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public LocationDTO update(Long id, LocationDTO dto) {
        log.info("Updating location with ID: {}", id);
        
        return super.update(id, dto);
    }

    public List<LocationDTO> getAll() {
        log.debug("Getting all locations without pagination");
        return locationRepository.findAll().stream()
                .map(LocationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<LocationDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for locations with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> locationRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    public List<LocationDTO> findByLocality(Long localityId) {
        log.debug("Finding locations by locality id: {}", localityId);
        return locationRepository.findByLocalityId(localityId).stream()
                .map(LocationDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

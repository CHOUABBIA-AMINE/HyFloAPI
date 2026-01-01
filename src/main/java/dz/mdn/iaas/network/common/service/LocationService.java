/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: LocationService
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Network / Service
 *	@Package	: Network / Service
 *
 **/

package dz.mdn.iaas.network.common.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.mdn.iaas.common.administration.model.Locality;
import dz.mdn.iaas.common.administration.repository.LocalityRepository;
import dz.mdn.iaas.configuration.template.GenericService;
import dz.mdn.iaas.exception.BusinessValidationException;
import dz.mdn.iaas.network.common.dto.LocationDTO;
import dz.mdn.iaas.network.common.model.Location;
import dz.mdn.iaas.network.common.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LocationService extends GenericService<Location, LocationDTO, Long> {

    private final LocationRepository locationRepository;
    private final LocalityRepository localityRepository;

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
        if (dto.getLocalityId() != null) {
            Locality locality = localityRepository.findById(dto.getLocalityId())
                    .orElseThrow(() -> new RuntimeException("Locality not found with id: " + dto.getLocalityId()));
            entity.setLocality(locality);
        }
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Location entity, LocationDTO dto) {
        dto.updateEntity(entity);
        if (dto.getLocalityId() != null) {
            Locality locality = localityRepository.findById(dto.getLocalityId())
                    .orElseThrow(() -> new RuntimeException("Locality not found with id: " + dto.getLocalityId()));
            entity.setLocality(locality);
        }
    }

    @Override
    @Transactional
    public LocationDTO create(LocationDTO dto) {
        log.info("Creating location: code={}", dto.getCode());
        
        if (locationRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Location with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public LocationDTO update(Long id, LocationDTO dto) {
        log.info("Updating location with ID: {}", id);
        
        if (locationRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Location with code '" + dto.getCode() + "' already exists");
        }
        
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

/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RegionService
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

import dz.mdn.iaas.configuration.template.GenericService;
import dz.mdn.iaas.exception.BusinessValidationException;
import dz.mdn.iaas.network.common.dto.RegionDTO;
import dz.mdn.iaas.network.common.model.Region;
import dz.mdn.iaas.network.common.model.Zone;
import dz.mdn.iaas.network.common.repository.RegionRepository;
import dz.mdn.iaas.network.common.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RegionService extends GenericService<Region, RegionDTO, Long> {

    private final RegionRepository regionRepository;
    private final ZoneRepository zoneRepository;

    @Override
    protected JpaRepository<Region, Long> getRepository() {
        return regionRepository;
    }

    @Override
    protected String getEntityName() {
        return "Region";
    }

    @Override
    protected RegionDTO toDTO(Region entity) {
        return RegionDTO.fromEntity(entity);
    }

    @Override
    protected Region toEntity(RegionDTO dto) {
        Region entity = dto.toEntity();
        if (dto.getZoneId() != null) {
            Zone zone = zoneRepository.findById(dto.getZoneId())
                    .orElseThrow(() -> new RuntimeException("Zone not found with id: " + dto.getZoneId()));
            entity.setZone(zone);
        }
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Region entity, RegionDTO dto) {
        dto.updateEntity(entity);
        if (dto.getZoneId() != null) {
            Zone zone = zoneRepository.findById(dto.getZoneId())
                    .orElseThrow(() -> new RuntimeException("Zone not found with id: " + dto.getZoneId()));
            entity.setZone(zone);
        }
    }

    @Override
    @Transactional
    public RegionDTO create(RegionDTO dto) {
        log.info("Creating region: name={}", dto.getDesignationFr());
        
        if (regionRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException("Region with name '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public RegionDTO update(Long id, RegionDTO dto) {
        log.info("Updating region with ID: {}", id);
        
        if (regionRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException("Region with name '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<RegionDTO> getAll() {
        log.debug("Getting all regions without pagination");
        return regionRepository.findAll().stream()
                .map(RegionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<RegionDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for regions with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> regionRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    public List<RegionDTO> findByZone(Long zoneId) {
        log.debug("Finding regions by zone id: {}", zoneId);
        return regionRepository.findByZoneId(zoneId).stream()
                .map(RegionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RegionDTO> findByActivity(Long activityId) {
        log.debug("Finding regions by activity id: {}", activityId);
        return regionRepository.findByActivityId(activityId).stream()
                .map(RegionDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

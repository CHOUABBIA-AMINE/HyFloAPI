/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FacilityService
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Network / Service
 *	@Package	: Network / Service
 *
 **/

package dz.mdn.iaas.network.core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.mdn.iaas.configuration.template.GenericService;
import dz.mdn.iaas.exception.BusinessValidationException;
import dz.mdn.iaas.network.common.model.OperationalStatus;
import dz.mdn.iaas.network.common.repository.OperationalStatusRepository;
import dz.mdn.iaas.network.core.dto.FacilityDTO;
import dz.mdn.iaas.network.core.model.Facility;
import dz.mdn.iaas.network.core.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FacilityService extends GenericService<Facility, FacilityDTO, Long> {

    private final FacilityRepository facilityRepository;
    private final OperationalStatusRepository operationalStatusRepository;
    //private final LocationRepository locationRepository;

    @Override
    protected JpaRepository<Facility, Long> getRepository() {
        return facilityRepository;
    }

    @Override
    protected String getEntityName() {
        return "Facility";
    }

    @Override
    protected FacilityDTO toDTO(Facility entity) {
        return FacilityDTO.fromEntity(entity);
    }

    @Override
    protected Facility toEntity(FacilityDTO dto) {
        Facility entity = dto.toEntity();
        
        if (dto.getOperationalStatusId() != null) {
            OperationalStatus status = operationalStatusRepository.findById(dto.getOperationalStatusId())
                    .orElseThrow(() -> new RuntimeException("Operational status not found with id: " + dto.getOperationalStatusId()));
            entity.setOperationalStatus(status);
        }
        
        //if (dto.getLocationId() != null) {
        //    Location location = locationRepository.findById(dto.getLocationId())
        //            .orElseThrow(() -> new RuntimeException("Location not found with id: " + dto.getLocationId()));
        //    entity.setLocation(location);
        //}
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Facility entity, FacilityDTO dto) {
        dto.updateEntity(entity);
        
        if (dto.getOperationalStatusId() != null) {
            OperationalStatus status = operationalStatusRepository.findById(dto.getOperationalStatusId())
                    .orElseThrow(() -> new RuntimeException("Operational status not found with id: " + dto.getOperationalStatusId()));
            entity.setOperationalStatus(status);
        }
        
        //if (dto.getLocationId() != null) {
        //    Location location = locationRepository.findById(dto.getLocationId())
        //            .orElseThrow(() -> new RuntimeException("Location not found with id: " + dto.getLocationId()));
        //    entity.setLocation(location);
        //}
    }

    @Override
    @Transactional
    public FacilityDTO create(FacilityDTO dto) {
        log.info("Creating facility: code={}", dto.getCode());
        
        if (facilityRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Facility with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public FacilityDTO update(Long id, FacilityDTO dto) {
        log.info("Updating facility with ID: {}", id);
        
        if (facilityRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Facility with code '" + dto.getCode() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<FacilityDTO> getAll() {
        log.debug("Getting all facilities without pagination");
        return facilityRepository.findAll().stream()
                .map(FacilityDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<FacilityDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for facilities with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> facilityRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    public List<FacilityDTO> findByVendor(Long vendorId) {
        log.debug("Finding locations by vendor id: {}", vendorId);
        return facilityRepository.findByVendorId(vendorId).stream()
                .map(FacilityDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FacilityDTO> findByLocality(Long localityId) {
        log.debug("Finding locations by locality id: {}", localityId);
        return facilityRepository.findByLocalityId(localityId).stream()
                .map(FacilityDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FacilityDTO> findByOperationalStatus(Long operationalStatusId) {
        log.debug("Finding locations by operational status id: {}", operationalStatusId);
        return facilityRepository.findByOperationalStatusId(operationalStatusId).stream()
                .map(FacilityDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

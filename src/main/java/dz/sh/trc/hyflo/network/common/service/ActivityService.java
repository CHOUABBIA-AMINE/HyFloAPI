/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ActivityService
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Network / Service
 *	@Package	: Network / Service
 *
 **/

package dz.sh.trc.hyflo.network.common.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.network.common.dto.ActivityDTO;
import dz.sh.trc.hyflo.network.common.model.Activity;
import dz.sh.trc.hyflo.network.common.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ActivityService extends GenericService<Activity, ActivityDTO, Long> {

    private final ActivityRepository zoneRepository;

    @Override
    protected JpaRepository<Activity, Long> getRepository() {
        return zoneRepository;
    }

    @Override
    protected String getEntityName() {
        return "Activity";
    }

    @Override
    protected ActivityDTO toDTO(Activity entity) {
        return ActivityDTO.fromEntity(entity);
    }

    @Override
    protected Activity toEntity(ActivityDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Activity entity, ActivityDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ActivityDTO create(ActivityDTO dto) {
        log.info("Creating zone: designationFr={}", dto.getDesignationFr());
        
        if (zoneRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException("Activity with name '" + dto.getDesignationFr() + "' already exists");
        }
        
        if (zoneRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Activity with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public ActivityDTO update(Long id, ActivityDTO dto) {
        log.info("Updating zone with ID: {}", id);
        
        if (zoneRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException("Activity with name '" + dto.getDesignationFr() + "' already exists");
        }
        
        if (zoneRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Activity with code '" + dto.getCode() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<ActivityDTO> getAll() {
        log.debug("Getting all zones without pagination");
        return zoneRepository.findAll().stream()
                .map(ActivityDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ActivityDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for zones with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> zoneRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}

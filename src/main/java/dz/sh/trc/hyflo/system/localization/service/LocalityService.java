/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: LocalityService
 *	@CreatedOn	: 10-14-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Common / Administration
 *	@Package	: Common / Administration / Service
 *
 **/

package dz.sh.trc.hyflo.system.localization.service;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.system.localization.dto.LocalityDTO;
import dz.sh.trc.hyflo.system.localization.model.Locality;
import dz.sh.trc.hyflo.system.localization.model.State;
import dz.sh.trc.hyflo.system.localization.repository.LocalityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Locality Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LocalityService extends GenericService<Locality, LocalityDTO, Long> {

    private final LocalityRepository localityRepository;
    private final StateService stateService;

    @Override
    protected JpaRepository<Locality, Long> getRepository() {
        return localityRepository;
    }

    @Override
    protected String getEntityName() {
        return "Locality";
    }

    @Override
    protected LocalityDTO toDTO(Locality entity) {
        return LocalityDTO.fromEntity(entity);
    }

    @Override
    protected Locality toEntity(LocalityDTO dto) {
        Locality entity = dto.toEntity();
        
        // Set relationships
        if (dto.getStateId() != null) {
            State state = stateService.getEntityById(dto.getStateId());
            entity.setState(state);
        }
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Locality entity, LocalityDTO dto) {
        dto.updateEntity(entity);
        
        // Update relationships
        if (dto.getStateId() != null) {
            State state = stateService.getEntityById(dto.getStateId());
            entity.setState(state);
        }
    }

    @Override
    @Transactional
    public LocalityDTO create(LocalityDTO dto) {
        log.info("Creating locality: code={}, designationFr={}, stateId={}", 
                dto.getCode(), dto.getDesignationFr(), dto.getStateId());
        return super.create(dto);
    }

    @Override
    @Transactional
    public LocalityDTO update(Long id, LocalityDTO dto) {
        log.info("Updating locality with ID: {}", id);
        return super.update(id, dto);
    }

    public List<LocalityDTO> getAll() {
        log.debug("Getting all localities without pagination");
        return localityRepository.findAll().stream()
                .map(LocalityDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<LocalityDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for localities with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }

    public List<LocalityDTO> getByStateId(Long stateId) {
        log.debug("Getting localities by state ID: {}", stateId);
        return localityRepository.findByStateId(stateId).stream()
                .map(LocalityDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

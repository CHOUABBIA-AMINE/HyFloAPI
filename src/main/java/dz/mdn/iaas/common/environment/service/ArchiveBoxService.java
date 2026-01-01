/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ArchiveBoxService
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Common / Environment
 *	@Package	: Common / Environment / Service
 *
 **/

package dz.mdn.iaas.common.environment.service;

import dz.mdn.iaas.common.environment.dto.ArchiveBoxDTO;
import dz.mdn.iaas.common.environment.model.ArchiveBox;
import dz.mdn.iaas.common.environment.model.ShelfFloor;
import dz.mdn.iaas.common.environment.repository.ArchiveBoxRepository;
import dz.mdn.iaas.configuration.template.GenericService;
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
 * ArchiveBox Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ArchiveBoxService extends GenericService<ArchiveBox, ArchiveBoxDTO, Long> {

    private final ArchiveBoxRepository archiveBoxRepository;
    private final ShelfFloorService shelfFloorService;

    @Override
    protected JpaRepository<ArchiveBox, Long> getRepository() {
        return archiveBoxRepository;
    }

    @Override
    protected String getEntityName() {
        return "ArchiveBox";
    }

    @Override
    protected ArchiveBoxDTO toDTO(ArchiveBox entity) {
        return ArchiveBoxDTO.fromEntity(entity);
    }

    @Override
    protected ArchiveBox toEntity(ArchiveBoxDTO dto) {
        ArchiveBox entity = dto.toEntity();
        
        // Set relationships
        if (dto.getShelfFloorId() != null) {
            ShelfFloor shelfFloor = shelfFloorService.getEntityById(dto.getShelfFloorId());
            entity.setShelfFloor(shelfFloor);
        }
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(ArchiveBox entity, ArchiveBoxDTO dto) {
        dto.updateEntity(entity);
        
        // Update relationships
        if (dto.getShelfFloorId() != null) {
            ShelfFloor shelfFloor = shelfFloorService.getEntityById(dto.getShelfFloorId());
            entity.setShelfFloor(shelfFloor);
        }
    }

    @Override
    @Transactional
    public ArchiveBoxDTO create(ArchiveBoxDTO dto) {
        log.info("Creating archive box: code={}, shelfFloorId={}", 
                dto.getCode(), dto.getShelfFloorId());
        return super.create(dto);
    }

    @Override
    @Transactional
    public ArchiveBoxDTO update(Long id, ArchiveBoxDTO dto) {
        log.info("Updating archive box with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ArchiveBoxDTO> getAll() {
        log.debug("Getting all archive boxes without pagination");
        return archiveBoxRepository.findAll().stream()
                .map(ArchiveBoxDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ArchiveBoxDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for archive boxes with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }

    public List<ArchiveBoxDTO> getByShelfFloorId(Long shelfFloorId) {
        log.debug("Getting archive boxes by shelf floor ID: {}", shelfFloorId);
        return archiveBoxRepository.findByShelfFloorId(shelfFloorId).stream()
                .map(ArchiveBoxDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ArchiveBoxDTO> getByShelfId(Long shelfId) {
        log.debug("Getting archive boxes by shelf ID: {}", shelfId);
        return archiveBoxRepository.findByShelfId(shelfId).stream()
                .map(ArchiveBoxDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

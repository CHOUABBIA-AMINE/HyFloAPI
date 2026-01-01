/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FolderService
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Common / Environment
 *	@Package	: Common / Environment / Service
 *
 **/

package dz.mdn.iaas.common.environment.service;

import dz.mdn.iaas.common.environment.dto.FolderDTO;
import dz.mdn.iaas.common.environment.model.ArchiveBox;
import dz.mdn.iaas.common.environment.model.Folder;
import dz.mdn.iaas.common.environment.repository.FolderRepository;
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
 * Folder Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FolderService extends GenericService<Folder, FolderDTO, Long> {

    private final FolderRepository folderRepository;
    private final ArchiveBoxService archiveBoxService;

    @Override
    protected JpaRepository<Folder, Long> getRepository() {
        return folderRepository;
    }

    @Override
    protected String getEntityName() {
        return "Folder";
    }

    @Override
    protected FolderDTO toDTO(Folder entity) {
        return FolderDTO.fromEntity(entity);
    }

    @Override
    protected Folder toEntity(FolderDTO dto) {
        Folder entity = dto.toEntity();
        
        // Set relationships
        if (dto.getArchiveBoxId() != null) {
            ArchiveBox archiveBox = archiveBoxService.getEntityById(dto.getArchiveBoxId());
            entity.setArchiveBox(archiveBox);
        }
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Folder entity, FolderDTO dto) {
        dto.updateEntity(entity);
        
        // Update relationships
        if (dto.getArchiveBoxId() != null) {
            ArchiveBox archiveBox = archiveBoxService.getEntityById(dto.getArchiveBoxId());
            entity.setArchiveBox(archiveBox);
        }
    }

    @Override
    @Transactional
    public FolderDTO create(FolderDTO dto) {
        log.info("Creating folder: code={}, archiveBoxId={}", 
                dto.getCode(), dto.getArchiveBoxId());
        return super.create(dto);
    }

    @Override
    @Transactional
    public FolderDTO update(Long id, FolderDTO dto) {
        log.info("Updating folder with ID: {}", id);
        return super.update(id, dto);
    }

    public List<FolderDTO> getAll() {
        log.debug("Getting all folders without pagination");
        return folderRepository.findAll().stream()
                .map(FolderDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<FolderDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for folders with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }

    public List<FolderDTO> getByArchiveBoxId(Long archiveBoxId) {
        log.debug("Getting folders by archive box ID: {}", archiveBoxId);
        return folderRepository.findByArchiveBoxId(archiveBoxId).stream()
                .map(FolderDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

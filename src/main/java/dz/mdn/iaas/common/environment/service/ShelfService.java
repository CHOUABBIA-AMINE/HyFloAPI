/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ShelfService
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Common / Environment
 *	@Package	: Common / Environment / Service
 *
 **/

package dz.mdn.iaas.common.environment.service;

import dz.mdn.iaas.common.environment.dto.ShelfDTO;
import dz.mdn.iaas.common.environment.model.Room;
import dz.mdn.iaas.common.environment.model.Shelf;
import dz.mdn.iaas.common.environment.repository.ShelfRepository;
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
 * Shelf Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ShelfService extends GenericService<Shelf, ShelfDTO, Long> {

    private final ShelfRepository shelfRepository;
    private final RoomService roomService;

    @Override
    protected JpaRepository<Shelf, Long> getRepository() {
        return shelfRepository;
    }

    @Override
    protected String getEntityName() {
        return "Shelf";
    }

    @Override
    protected ShelfDTO toDTO(Shelf entity) {
        return ShelfDTO.fromEntity(entity);
    }

    @Override
    protected Shelf toEntity(ShelfDTO dto) {
        Shelf entity = dto.toEntity();
        
        // Set relationships
        if (dto.getRoomId() != null) {
            Room room = roomService.getEntityById(dto.getRoomId());
            entity.setRoom(room);
        }
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Shelf entity, ShelfDTO dto) {
        dto.updateEntity(entity);
        
        // Update relationships
        if (dto.getRoomId() != null) {
            Room room = roomService.getEntityById(dto.getRoomId());
            entity.setRoom(room);
        }
    }

    @Override
    @Transactional
    public ShelfDTO create(ShelfDTO dto) {
        log.info("Creating shelf: code={}, designationFr={}, roomId={}", 
                dto.getCode(), dto.getDesignationFr(), dto.getRoomId());
        return super.create(dto);
    }

    @Override
    @Transactional
    public ShelfDTO update(Long id, ShelfDTO dto) {
        log.info("Updating shelf with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ShelfDTO> getAll() {
        log.debug("Getting all shelves without pagination");
        return shelfRepository.findAll().stream()
                .map(ShelfDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ShelfDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for shelves with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }

    public List<ShelfDTO> getByRoomId(Long roomId) {
        log.debug("Getting shelves by room ID: {}", roomId);
        return shelfRepository.findByRoomId(roomId).stream()
                .map(ShelfDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

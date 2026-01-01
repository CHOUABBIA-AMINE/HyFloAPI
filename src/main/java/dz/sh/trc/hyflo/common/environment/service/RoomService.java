/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RoomService
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Common / Environment
 *	@Package	: Common / Environment / Service
 *
 **/

package dz.sh.trc.hyflo.common.environment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.common.environment.dto.RoomDTO;
import dz.sh.trc.hyflo.common.environment.model.Bloc;
import dz.sh.trc.hyflo.common.environment.model.Floor;
import dz.sh.trc.hyflo.common.environment.model.Room;
import dz.sh.trc.hyflo.common.environment.repository.RoomRepository;
import dz.sh.trc.hyflo.configuration.template.GenericService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Room Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RoomService extends GenericService<Room, RoomDTO, Long> {

    private final RoomRepository roomRepository;
    private final BlocService blocService;
    private final FloorService floorService;

    @Override
    protected JpaRepository<Room, Long> getRepository() {
        return roomRepository;
    }

    @Override
    protected String getEntityName() {
        return "Room";
    }

    @Override
    protected RoomDTO toDTO(Room entity) {
        return RoomDTO.fromEntity(entity);
    }

    @Override
    protected Room toEntity(RoomDTO dto) {
        Room entity = dto.toEntity();
        
        // Set relationships
        if (dto.getBlocId() != null) {
            Bloc bloc = blocService.getEntityById(dto.getBlocId());
            entity.setBloc(bloc);
        }
        if (dto.getFloorId() != null) {
            Floor floor = floorService.getEntityById(dto.getFloorId());
            entity.setFloor(floor);
        }
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Room entity, RoomDTO dto) {
        dto.updateEntity(entity);
        
        // Update relationships
        if (dto.getBlocId() != null) {
            Bloc bloc = blocService.getEntityById(dto.getBlocId());
            entity.setBloc(bloc);
        }
        if (dto.getFloorId() != null) {
            Floor floor = floorService.getEntityById(dto.getFloorId());
            entity.setFloor(floor);
        }
    }

    @Override
    @Transactional
    public RoomDTO create(RoomDTO dto) {
        log.info("Creating room: code={}, designationFr={}, blocId={}, floorId={}", 
                dto.getCode(), dto.getDesignationFr(), dto.getBlocId(), dto.getFloorId());
        return super.create(dto);
    }

    @Override
    @Transactional
    public RoomDTO update(Long id, RoomDTO dto) {
        log.info("Updating room with ID: {}", id);
        return super.update(id, dto);
    }

    public List<RoomDTO> getAll() {
        log.debug("Getting all rooms without pagination");
        return roomRepository.findAll().stream()
                .map(RoomDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<RoomDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for rooms with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }

    public List<RoomDTO> getByBlocId(Long blocId) {
        log.debug("Getting rooms by bloc ID: {}", blocId);
        return roomRepository.findByBlocId(blocId).stream()
                .map(RoomDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RoomDTO> getByFloorId(Long floorId) {
        log.debug("Getting rooms by floor ID: {}", floorId);
        return roomRepository.findByFloorId(floorId).stream()
                .map(RoomDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

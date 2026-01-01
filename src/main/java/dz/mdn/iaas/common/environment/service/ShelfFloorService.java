/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ShelfFloorService
 *	@CreatedOn	: 10-15-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Common / Environment
 *	@Package	: Common / Environment / Service
 *
 **/

package dz.mdn.iaas.common.environment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.mdn.iaas.common.environment.dto.ShelfFloorDTO;
import dz.mdn.iaas.common.environment.model.ShelfFloor;
import dz.mdn.iaas.common.environment.repository.ShelfFloorRepository;
import dz.mdn.iaas.configuration.template.GenericService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ShelfFloor Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ShelfFloorService extends GenericService<ShelfFloor, ShelfFloorDTO, Long> {

    private final ShelfFloorRepository shelfFloorRepository;

    @Override
    protected JpaRepository<ShelfFloor, Long> getRepository() {
        return shelfFloorRepository;
    }

    @Override
    protected String getEntityName() {
        return "ShelfFloor";
    }

    @Override
    protected ShelfFloorDTO toDTO(ShelfFloor entity) {
        return ShelfFloorDTO.fromEntity(entity);
    }

    @Override
    protected ShelfFloor toEntity(ShelfFloorDTO dto) {
        ShelfFloor entity = dto.toEntity();
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(ShelfFloor entity, ShelfFloorDTO dto) {
        dto.updateEntity(entity);

    }

    @Override
    @Transactional
    public ShelfFloorDTO create(ShelfFloorDTO dto) {
        log.info("Creating shelf floor: code={}, floorLevel={}", 
                dto.getCode(), dto.getFloorLevel());
        return super.create(dto);
    }

    @Override
    @Transactional
    public ShelfFloorDTO update(Long id, ShelfFloorDTO dto) {
        log.info("Updating shelf floor with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ShelfFloorDTO> getAll() {
        log.debug("Getting all shelf floors without pagination");
        return shelfFloorRepository.findAll().stream()
                .map(ShelfFloorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ShelfFloorDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for shelf floors with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

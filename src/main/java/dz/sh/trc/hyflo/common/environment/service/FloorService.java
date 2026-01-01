/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: FloorService
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

import dz.sh.trc.hyflo.common.environment.dto.FloorDTO;
import dz.sh.trc.hyflo.common.environment.model.Floor;
import dz.sh.trc.hyflo.common.environment.repository.FloorRepository;
import dz.sh.trc.hyflo.configuration.template.GenericService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Floor Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FloorService extends GenericService<Floor, FloorDTO, Long> {

    private final FloorRepository floorRepository;

    @Override
    protected JpaRepository<Floor, Long> getRepository() {
        return floorRepository;
    }

    @Override
    protected String getEntityName() {
        return "Floor";
    }

    @Override
    protected FloorDTO toDTO(Floor entity) {
        return FloorDTO.fromEntity(entity);
    }

    @Override
    protected Floor toEntity(FloorDTO dto) {
        Floor entity = dto.toEntity();
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Floor entity, FloorDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public FloorDTO create(FloorDTO dto) {
        log.info("Creating floor: designationFr={}, code={}", 
                dto.getDesignationFr(), dto.getCode());
        return super.create(dto);
    }

    @Override
    @Transactional
    public FloorDTO update(Long id, FloorDTO dto) {
        log.info("Updating floor with ID: {}", id);
        return super.update(id, dto);
    }

    public List<FloorDTO> getAll() {
        log.debug("Getting all floors without pagination");
        return floorRepository.findAll().stream()
                .map(FloorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<FloorDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for floors with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

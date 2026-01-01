/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ItemStatusService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Service
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Service
 *
 **/

package dz.sh.trc.hyflo.business.plan.service;

import dz.sh.trc.hyflo.business.plan.dto.ItemStatusDTO;
import dz.sh.trc.hyflo.business.plan.model.ItemStatus;
import dz.sh.trc.hyflo.business.plan.repository.ItemStatusRepository;
import dz.sh.trc.hyflo.configuration.template.GenericService;
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
 * ItemStatus Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemStatusService extends GenericService<ItemStatus, ItemStatusDTO, Long> {

    private final ItemStatusRepository itemStatusRepository;

    @Override
    protected JpaRepository<ItemStatus, Long> getRepository() {
        return itemStatusRepository;
    }

    @Override
    protected String getEntityName() {
        return "ItemStatus";
    }

    @Override
    protected ItemStatusDTO toDTO(ItemStatus entity) {
        return ItemStatusDTO.fromEntity(entity);
    }

    @Override
    protected ItemStatus toEntity(ItemStatusDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ItemStatus entity, ItemStatusDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ItemStatusDTO create(ItemStatusDTO dto) {
        log.info("Creating item status: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public ItemStatusDTO update(Long id, ItemStatusDTO dto) {
        log.info("Updating item status with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ItemStatusDTO> getAll() {
        log.debug("Getting all item statuses without pagination");
        return itemStatusRepository.findAll().stream()
                .map(ItemStatusDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all active item statuses
     * Note: ItemStatus model doesn't have isActive field, so returning all statuses
     * @return list of all item statuses
     */
    public List<ItemStatusDTO> getActiveStatuses() {
        log.debug("Getting all item statuses (no isActive field in model)");
        return getAll();
    }

    public Page<ItemStatusDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for item statuses with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ItemDistributionService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Service
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Service
 *
 **/

package dz.mdn.iaas.business.plan.service;

import dz.mdn.iaas.business.plan.dto.ItemDistributionDTO;
import dz.mdn.iaas.business.plan.model.ItemDistribution;
import dz.mdn.iaas.business.plan.repository.ItemDistributionRepository;
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
 * ItemDistribution Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemDistributionService extends GenericService<ItemDistribution, ItemDistributionDTO, Long> {

    private final ItemDistributionRepository itemDistributionRepository;

    @Override
    protected JpaRepository<ItemDistribution, Long> getRepository() {
        return itemDistributionRepository;
    }

    @Override
    protected String getEntityName() {
        return "ItemDistribution";
    }

    @Override
    protected ItemDistributionDTO toDTO(ItemDistribution entity) {
        return ItemDistributionDTO.fromEntity(entity);
    }

    @Override
    protected ItemDistribution toEntity(ItemDistributionDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ItemDistribution entity, ItemDistributionDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ItemDistributionDTO create(ItemDistributionDTO dto) {
        log.info("Creating item distribution");
        return super.create(dto);
    }

    @Override
    @Transactional
    public ItemDistributionDTO update(Long id, ItemDistributionDTO dto) {
        log.info("Updating item distribution with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ItemDistributionDTO> getAll() {
        log.debug("Getting all item distributions without pagination");
        return itemDistributionRepository.findAll().stream()
                .map(ItemDistributionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all item distributions by item ID
     * @param itemId the item ID
     * @return list of item distributions
     */
    public List<ItemDistributionDTO> getByItemId(Long plannedItemId) {
        log.debug("Getting item distributions by planned item ID: {}", plannedItemId);
        return itemDistributionRepository.findByPlannedItemId(plannedItemId).stream()
                .map(ItemDistributionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ItemDistributionDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for item distributions with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

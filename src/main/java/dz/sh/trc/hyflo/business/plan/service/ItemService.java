/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ItemService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Service
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Service
 *
 **/

package dz.sh.trc.hyflo.business.plan.service;

import dz.sh.trc.hyflo.business.plan.dto.ItemDTO;
import dz.sh.trc.hyflo.business.plan.model.Item;
import dz.sh.trc.hyflo.business.plan.repository.ItemRepository;
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
 * Item Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemService extends GenericService<Item, ItemDTO, Long> {

    private final ItemRepository itemRepository;

    @Override
    protected JpaRepository<Item, Long> getRepository() {
        return itemRepository;
    }

    @Override
    protected String getEntityName() {
        return "Item";
    }

    @Override
    protected ItemDTO toDTO(Item entity) {
        return ItemDTO.fromEntity(entity);
    }

    @Override
    protected Item toEntity(ItemDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Item entity, ItemDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ItemDTO create(ItemDTO dto) {
        log.info("Creating item: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public ItemDTO update(Long id, ItemDTO dto) {
        log.info("Updating item with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ItemDTO> getAll() {
        log.debug("Getting all items without pagination");
        return itemRepository.findAll().stream()
                .map(ItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ItemDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for items with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

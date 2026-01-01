/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: PlannedItemService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Service
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Service
 *
 **/

package dz.sh.trc.hyflo.business.plan.service;

import dz.sh.trc.hyflo.business.plan.dto.PlannedItemDTO;
import dz.sh.trc.hyflo.business.plan.model.PlannedItem;
import dz.sh.trc.hyflo.business.plan.repository.PlannedItemRepository;
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
 * PlannedItem Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PlannedItemService extends GenericService<PlannedItem, PlannedItemDTO, Long> {

    private final PlannedItemRepository plannedItemRepository;

    @Override
    protected JpaRepository<PlannedItem, Long> getRepository() {
        return plannedItemRepository;
    }

    @Override
    protected String getEntityName() {
        return "PlannedItem";
    }

    @Override
    protected PlannedItemDTO toDTO(PlannedItem entity) {
        return PlannedItemDTO.fromEntity(entity);
    }

    @Override
    protected PlannedItem toEntity(PlannedItemDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(PlannedItem entity, PlannedItemDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public PlannedItemDTO create(PlannedItemDTO dto) {
        log.info("Creating planned item");
        return super.create(dto);
    }

    @Override
    @Transactional
    public PlannedItemDTO update(Long id, PlannedItemDTO dto) {
        log.info("Updating planned item with ID: {}", id);
        return super.update(id, dto);
    }

    public List<PlannedItemDTO> getAll() {
        log.debug("Getting all planned items without pagination");
        return plannedItemRepository.findAll().stream()
                .map(PlannedItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all planned items by year
     * @param year the year
     * @return list of planned items
     */
    public List<PlannedItemDTO> getByYear(String year) {
        log.debug("Getting planned items by year: {}", year);
        return plannedItemRepository.findByFinancialOperation_BudgetYear(year).stream()
                .map(PlannedItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all planned items by financialOperation ID
     * @param financialOperationId the financial operation ID
     * @return list of planned items
     */
    public List<PlannedItemDTO> getByFinancialOperationId(Long financialOperationId) {
        log.debug("Getting planned items by structure ID: {}", financialOperationId);
        return plannedItemRepository.findByFinancialOperationId(financialOperationId).stream()
                .map(PlannedItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<PlannedItemDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for planned items with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

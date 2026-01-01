/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: BudgetTypeService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Service
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Service
 *
 **/

package dz.sh.trc.hyflo.business.plan.service;

import dz.sh.trc.hyflo.business.plan.dto.BudgetTypeDTO;
import dz.sh.trc.hyflo.business.plan.model.BudgetType;
import dz.sh.trc.hyflo.business.plan.repository.BudgetTypeRepository;
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
 * BudgetType Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BudgetTypeService extends GenericService<BudgetType, BudgetTypeDTO, Long> {

    private final BudgetTypeRepository budgetTypeRepository;

    @Override
    protected JpaRepository<BudgetType, Long> getRepository() {
        return budgetTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "BudgetType";
    }

    @Override
    protected BudgetTypeDTO toDTO(BudgetType entity) {
        return BudgetTypeDTO.fromEntity(entity);
    }

    @Override
    protected BudgetType toEntity(BudgetTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(BudgetType entity, BudgetTypeDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public BudgetTypeDTO create(BudgetTypeDTO dto) {
        log.info("Creating budget type: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public BudgetTypeDTO update(Long id, BudgetTypeDTO dto) {
        log.info("Updating budget type with ID: {}", id);
        return super.update(id, dto);
    }

    public List<BudgetTypeDTO> getAll() {
        log.debug("Getting all budget types without pagination");
        return budgetTypeRepository.findAll().stream()
                .map(BudgetTypeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all active budget types
     * Note: BudgetType model doesn't have isActive field, so returning all types
     * @return list of all budget types
     */
    public List<BudgetTypeDTO> getActiveTypes() {
        log.debug("Getting all budget types (no isActive field in model)");
        return getAll();
    }

    public Page<BudgetTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for budget types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

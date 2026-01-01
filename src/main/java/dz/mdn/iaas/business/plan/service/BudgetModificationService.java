/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: BudgetModificationService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Service
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Service
 *
 **/

package dz.mdn.iaas.business.plan.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.mdn.iaas.business.plan.dto.BudgetModificationDTO;
import dz.mdn.iaas.business.plan.model.BudgetModification;
import dz.mdn.iaas.business.plan.repository.BudgetModificationRepository;
import dz.mdn.iaas.configuration.template.GenericService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * BudgetModification Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BudgetModificationService extends GenericService<BudgetModification, BudgetModificationDTO, Long> {

    private final BudgetModificationRepository budgetModificationRepository;

    @Override
    protected JpaRepository<BudgetModification, Long> getRepository() {
        return budgetModificationRepository;
    }

    @Override
    protected String getEntityName() {
        return "BudgetModification";
    }

    @Override
    protected BudgetModificationDTO toDTO(BudgetModification entity) {
        return BudgetModificationDTO.fromEntity(entity);
    }

    @Override
    protected BudgetModification toEntity(BudgetModificationDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(BudgetModification entity, BudgetModificationDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public BudgetModificationDTO create(BudgetModificationDTO dto) {
        log.info("Creating budget modification");
        return super.create(dto);
    }

    @Override
    @Transactional
    public BudgetModificationDTO update(Long id, BudgetModificationDTO dto) {
        log.info("Updating budget modification with ID: {}", id);
        return super.update(id, dto);
    }

    public List<BudgetModificationDTO> getAll() {
        log.debug("Getting all budget modifications without pagination");
        return budgetModificationRepository.findAll().stream()
                .map(BudgetModificationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all budget modifications by year
     * @param year the year
     * @return list of budget modifications
     */
    public List<BudgetModificationDTO> getByYear(Integer year) {
        log.debug("Getting budget modifications by year: {}", year);
        return budgetModificationRepository.findByApprovalDateBetween(LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31)).stream()
                .map(BudgetModificationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<BudgetModificationDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for budget modifications with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

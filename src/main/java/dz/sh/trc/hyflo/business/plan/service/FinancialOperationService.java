/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: FinancialOperationService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Service
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Service
 *
 **/

package dz.sh.trc.hyflo.business.plan.service;

import dz.sh.trc.hyflo.business.plan.dto.FinancialOperationDTO;
import dz.sh.trc.hyflo.business.plan.model.FinancialOperation;
import dz.sh.trc.hyflo.business.plan.repository.FinancialOperationRepository;
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
 * FinancialOperation Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FinancialOperationService extends GenericService<FinancialOperation, FinancialOperationDTO, Long> {

    private final FinancialOperationRepository financialOperationRepository;

    @Override
    protected JpaRepository<FinancialOperation, Long> getRepository() {
        return financialOperationRepository;
    }

    @Override
    protected String getEntityName() {
        return "FinancialOperation";
    }

    @Override
    protected FinancialOperationDTO toDTO(FinancialOperation entity) {
        return FinancialOperationDTO.fromEntity(entity);
    }

    @Override
    protected FinancialOperation toEntity(FinancialOperationDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FinancialOperation entity, FinancialOperationDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public FinancialOperationDTO create(FinancialOperationDTO dto) {
        log.info("Creating financial operation");
        return super.create(dto);
    }

    @Override
    @Transactional
    public FinancialOperationDTO update(Long id, FinancialOperationDTO dto) {
        log.info("Updating financial operation with ID: {}", id);
        return super.update(id, dto);
    }

    public List<FinancialOperationDTO> getAll() {
        log.debug("Getting all financial operations without pagination");
        return financialOperationRepository.findAll().stream()
                .map(FinancialOperationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all financial operations by year
     * @param year the year
     * @return list of financial operations
     */
    public List<FinancialOperationDTO> getByYear(Integer year) {
        log.debug("Getting financial operations by year: {}", year);
        return financialOperationRepository.findByBudgetYear(year).stream()
                .map(FinancialOperationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<FinancialOperationDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for financial operations with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

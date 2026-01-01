/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: CurrencyService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-10-2025
 *
 *	@Type		: Service
 *	@Layer		: Business / Core
 *	@Package	: Business / Core / Service
 *
 **/

package dz.mdn.iaas.business.core.service;

import dz.mdn.iaas.business.core.dto.CurrencyDTO;
import dz.mdn.iaas.business.core.model.Currency;
import dz.mdn.iaas.business.core.repository.CurrencyRepository;
import dz.mdn.iaas.configuration.template.GenericService;
import dz.mdn.iaas.exception.BusinessValidationException;
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
 * Currency Service - Extends GenericService
 * Handles currency operations with validation and search
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CurrencyService extends GenericService<Currency, CurrencyDTO, Long> {

    private final CurrencyRepository currencyRepository;

    // ========== GENERIC SERVICE IMPLEMENTATION ==========

    @Override
    protected JpaRepository<Currency, Long> getRepository() {
        return currencyRepository;
    }

    @Override
    protected String getEntityName() {
        return "Currency";
    }

    @Override
    protected CurrencyDTO toDTO(Currency entity) {
        return CurrencyDTO.fromEntity(entity);
    }

    @Override
    protected Currency toEntity(CurrencyDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Currency entity, CurrencyDTO dto) {
        dto.updateEntity(entity);
    }

    // ========== CREATE WITH VALIDATION ==========

    @Override
    @Transactional
    public CurrencyDTO create(CurrencyDTO dto) {
        log.info("Creating currency: code={}", dto.getCode());
        validateUniqueConstraintsForCreate(dto);
        return super.create(dto);
    }

    // ========== UPDATE WITH VALIDATION ==========

    @Override
    @Transactional
    public CurrencyDTO update(Long id, CurrencyDTO dto) {
        log.info("Updating currency with ID: {}", id);
        validateUniqueConstraintsForUpdate(dto, id);
        return super.update(id, dto);
    }

    // ========== GET ALL (NON-PAGINATED) ==========

    public List<CurrencyDTO> getAll() {
        log.debug("Getting all currencies without pagination");
        return currencyRepository.findAll().stream()
                .map(CurrencyDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // ========== SEARCH METHODS ==========

    /**
     * Global search across all currency fields
     */
    public Page<CurrencyDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for currencies with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> currencyRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    /**
     * Search by designation (all languages)
     */
    public Page<CurrencyDTO> searchByDesignation(String designation, Pageable pageable) {
        log.debug("Searching currencies by designation: {}", designation);
        
        if (designation == null || designation.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> currencyRepository.searchByDesignation(designation.trim(), p), pageable);
    }

    /**
     * Search by acronym (all languages)
     */
    public Page<CurrencyDTO> searchByAcronym(String acronym, Pageable pageable) {
        log.debug("Searching currencies by acronym: {}", acronym);
        
        if (acronym == null || acronym.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> currencyRepository.searchByAcronym(acronym.trim(), p), pageable);
    }

    // ========== EXISTENCE CHECKS ==========

    public boolean existsByCode(String code) {
        return currencyRepository.existsByCode(code);
    }

    public boolean existsByDesignationAr(String designationAr) {
        return currencyRepository.existsByDesignationAr(designationAr);
    }

    public boolean existsByDesignationEn(String designationEn) {
        return currencyRepository.existsByDesignationEn(designationEn);
    }

    public boolean existsByDesignationFr(String designationFr) {
        return currencyRepository.existsByDesignationFr(designationFr);
    }

    public boolean existsByAcronymAr(String acronymAr) {
        return currencyRepository.existsByAcronymAr(acronymAr);
    }

    public boolean existsByAcronymEn(String acronymEn) {
        return currencyRepository.existsByAcronymEn(acronymEn);
    }

    public boolean existsByAcronymFr(String acronymFr) {
        return currencyRepository.existsByAcronymFr(acronymFr);
    }

    // ========== VALIDATION ==========

    private void validateUniqueConstraintsForCreate(CurrencyDTO dto) {
        if (currencyRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Code '" + dto.getCode() + "' already exists");
        }
        if (currencyRepository.existsByDesignationAr(dto.getDesignationAr())) {
            throw new BusinessValidationException("Arabic designation '" + dto.getDesignationAr() + "' already exists");
        }
        if (currencyRepository.existsByDesignationEn(dto.getDesignationEn())) {
            throw new BusinessValidationException("English designation '" + dto.getDesignationEn() + "' already exists");
        }
        if (currencyRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException("French designation '" + dto.getDesignationFr() + "' already exists");
        }
        if (currencyRepository.existsByAcronymAr(dto.getAcronymAr())) {
            throw new BusinessValidationException("Arabic acronym '" + dto.getAcronymAr() + "' already exists");
        }
        if (currencyRepository.existsByAcronymEn(dto.getAcronymEn())) {
            throw new BusinessValidationException("English acronym '" + dto.getAcronymEn() + "' already exists");
        }
        if (currencyRepository.existsByAcronymFr(dto.getAcronymFr())) {
            throw new BusinessValidationException("French acronym '" + dto.getAcronymFr() + "' already exists");
        }
    }

    private void validateUniqueConstraintsForUpdate(CurrencyDTO dto, Long id) {
        if (currencyRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Code '" + dto.getCode() + "' already exists");
        }
        if (currencyRepository.existsByDesignationArAndIdNot(dto.getDesignationAr(), id)) {
            throw new BusinessValidationException("Arabic designation '" + dto.getDesignationAr() + "' already exists");
        }
        if (currencyRepository.existsByDesignationEnAndIdNot(dto.getDesignationEn(), id)) {
            throw new BusinessValidationException("English designation '" + dto.getDesignationEn() + "' already exists");
        }
        if (currencyRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException("French designation '" + dto.getDesignationFr() + "' already exists");
        }
        if (currencyRepository.existsByAcronymArAndIdNot(dto.getAcronymAr(), id)) {
            throw new BusinessValidationException("Arabic acronym '" + dto.getAcronymAr() + "' already exists");
        }
        if (currencyRepository.existsByAcronymEnAndIdNot(dto.getAcronymEn(), id)) {
            throw new BusinessValidationException("English acronym '" + dto.getAcronymEn() + "' already exists");
        }
        if (currencyRepository.existsByAcronymFrAndIdNot(dto.getAcronymFr(), id)) {
            throw new BusinessValidationException("French acronym '" + dto.getAcronymFr() + "' already exists");
        }
    }
}

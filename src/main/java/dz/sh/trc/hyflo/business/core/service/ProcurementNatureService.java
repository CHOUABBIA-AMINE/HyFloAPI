/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ProcurementNatureService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-10-2025
 *	@Type		: Service
 *	@Layer		: Business / Core
 *	@Package	: Business / Core / Service
 *
 **/

package dz.sh.trc.hyflo.business.core.service;

import dz.sh.trc.hyflo.business.core.dto.ProcurementNatureDTO;
import dz.sh.trc.hyflo.business.core.model.ProcurementNature;
import dz.sh.trc.hyflo.business.core.repository.ProcurementNatureRepository;
import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
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
 * ProcurementNature Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProcurementNatureService extends GenericService<ProcurementNature, ProcurementNatureDTO, Long> {

    private final ProcurementNatureRepository procurementNatureRepository;

    @Override
    protected JpaRepository<ProcurementNature, Long> getRepository() {
        return procurementNatureRepository;
    }

    @Override
    protected String getEntityName() {
        return "ProcurementNature";
    }

    @Override
    protected ProcurementNatureDTO toDTO(ProcurementNature entity) {
        return ProcurementNatureDTO.fromEntity(entity);
    }

    @Override
    protected ProcurementNature toEntity(ProcurementNatureDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ProcurementNature entity, ProcurementNatureDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ProcurementNatureDTO create(ProcurementNatureDTO dto) {
        log.info("Creating procurement nature: designationFr={}, code={}", dto.getDesignationFr());
        
        if (procurementNatureRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException("French designation '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public ProcurementNatureDTO update(Long id, ProcurementNatureDTO dto) {
        log.info("Updating procurement nature with ID: {}", id);
        
        if (procurementNatureRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException("French designation '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<ProcurementNatureDTO> getAll() {
        log.debug("Getting all procurement natures without pagination");
        return procurementNatureRepository.findAll().stream()
                .map(ProcurementNatureDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ProcurementNatureDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for procurement natures with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> procurementNatureRepository.searchByDesignation(searchTerm.trim(), p), pageable);
    }

    public boolean existsByDesignationFr(String designationFr) {
        return procurementNatureRepository.existsByDesignationFr(designationFr);
    }
}

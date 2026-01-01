/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ProcurementStatusService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-10-2025
 *	@Type		: Service
 *	@Layer		: Business / Core
 *	@Package	: Business / Core / Service
 *
 **/

package dz.mdn.iaas.business.core.service;

import dz.mdn.iaas.business.core.dto.ProcurementStatusDTO;
import dz.mdn.iaas.business.core.model.ProcurementStatus;
import dz.mdn.iaas.business.core.repository.ProcurementStatusRepository;
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
 * ProcurementStatus Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProcurementStatusService extends GenericService<ProcurementStatus, ProcurementStatusDTO, Long> {

    private final ProcurementStatusRepository procurementStatusRepository;

    @Override
    protected JpaRepository<ProcurementStatus, Long> getRepository() {
        return procurementStatusRepository;
    }

    @Override
    protected String getEntityName() {
        return "ProcurementStatus";
    }

    @Override
    protected ProcurementStatusDTO toDTO(ProcurementStatus entity) {
        return ProcurementStatusDTO.fromEntity(entity);
    }

    @Override
    protected ProcurementStatus toEntity(ProcurementStatusDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ProcurementStatus entity, ProcurementStatusDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ProcurementStatusDTO create(ProcurementStatusDTO dto) {
        log.info("Creating procurement status: designationFr={}, code={}", dto.getDesignationFr());
        
        if (procurementStatusRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException("French designation '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public ProcurementStatusDTO update(Long id, ProcurementStatusDTO dto) {
        log.info("Updating procurement status with ID: {}", id);
        
        if (procurementStatusRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException("French designation '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<ProcurementStatusDTO> getAll() {
        log.debug("Getting all procurement statuses without pagination");
        return procurementStatusRepository.findAll().stream()
                .map(ProcurementStatusDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ProcurementStatusDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for procurement statuses with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> procurementStatusRepository.searchByDesignation(searchTerm.trim(), p), pageable);
    }

    public boolean existsByDesignationFr(String designationFr) {
        return procurementStatusRepository.existsByDesignationFr(designationFr);
    }
}

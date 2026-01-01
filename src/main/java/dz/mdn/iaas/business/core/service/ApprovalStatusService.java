/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ApprovalStatusService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-10-2025
 *	@Type		: Service
 *	@Layer		: Business / Core
 *	@Package	: Business / Core / Service
 *
 **/

package dz.mdn.iaas.business.core.service;

import dz.mdn.iaas.business.core.dto.ApprovalStatusDTO;
import dz.mdn.iaas.business.core.model.ApprovalStatus;
import dz.mdn.iaas.business.core.repository.ApprovalStatusRepository;
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
 * ApprovalStatus Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ApprovalStatusService extends GenericService<ApprovalStatus, ApprovalStatusDTO, Long> {

    private final ApprovalStatusRepository approvalStatusRepository;

    @Override
    protected JpaRepository<ApprovalStatus, Long> getRepository() {
        return approvalStatusRepository;
    }

    @Override
    protected String getEntityName() {
        return "ApprovalStatus";
    }

    @Override
    protected ApprovalStatusDTO toDTO(ApprovalStatus entity) {
        return ApprovalStatusDTO.fromEntity(entity);
    }

    @Override
    protected ApprovalStatus toEntity(ApprovalStatusDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ApprovalStatus entity, ApprovalStatusDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ApprovalStatusDTO create(ApprovalStatusDTO dto) {
        log.info("Creating approval status: designationFr={}", dto.getDesignationFr());
        
        if (approvalStatusRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException("French designation '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public ApprovalStatusDTO update(Long id, ApprovalStatusDTO dto) {
        log.info("Updating approval status with ID: {}", id);
        
        if (approvalStatusRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException("French designation '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<ApprovalStatusDTO> getAll() {
        log.debug("Getting all approval statuses without pagination");
        return approvalStatusRepository.findAll().stream()
                .map(ApprovalStatusDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ApprovalStatusDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for approval statuses with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> approvalStatusRepository.searchByDesignation(searchTerm.trim(), p), pageable);
    }

    public boolean existsByDesignationFr(String designationFr) {
        return approvalStatusRepository.existsByDesignationFr(designationFr);
    }
}

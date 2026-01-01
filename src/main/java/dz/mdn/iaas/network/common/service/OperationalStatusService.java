/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: OperationalStatusService
 *	@CreatedOn	: 12-11-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Network / Service
 *	@Package	: Network / Service
 *
 **/

package dz.mdn.iaas.network.common.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.mdn.iaas.configuration.template.GenericService;
import dz.mdn.iaas.exception.BusinessValidationException;
import dz.mdn.iaas.network.common.dto.OperationalStatusDTO;
import dz.mdn.iaas.network.common.model.OperationalStatus;
import dz.mdn.iaas.network.common.repository.OperationalStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OperationalStatusService extends GenericService<OperationalStatus, OperationalStatusDTO, Long> {

    private final OperationalStatusRepository operationalStatusRepository;

    @Override
    protected JpaRepository<OperationalStatus, Long> getRepository() {
        return operationalStatusRepository;
    }

    @Override
    protected String getEntityName() {
        return "OperationalStatus";
    }

    @Override
    protected OperationalStatusDTO toDTO(OperationalStatus entity) {
        return OperationalStatusDTO.fromEntity(entity);
    }

    @Override
    protected OperationalStatus toEntity(OperationalStatusDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(OperationalStatus entity, OperationalStatusDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public OperationalStatusDTO create(OperationalStatusDTO dto) {
        log.info("Creating operational status: code={}", dto.getCode());
        
        if (operationalStatusRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Operational status with code '" + dto.getCode() + "' already exists");
        }

        if (operationalStatusRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException("Facility type with designationFr '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public OperationalStatusDTO update(Long id, OperationalStatusDTO dto) {
        log.info("Updating operational status with ID: {}", id);
        
        if (operationalStatusRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Operational status with code '" + dto.getCode() + "' already exists");
        }

        if (operationalStatusRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException("Facility type with designationFr '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<OperationalStatusDTO> getAll() {
        log.debug("Getting all operational statuses without pagination");
        return operationalStatusRepository.findAll().stream()
                .map(OperationalStatusDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<OperationalStatusDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for operational statuses with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> operationalStatusRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}

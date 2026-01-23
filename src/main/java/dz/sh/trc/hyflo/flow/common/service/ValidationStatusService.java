/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ValidationStatusService
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.flow.common.dto.ValidationStatusDTO;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.common.repository.ValidationStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ValidationStatusService extends GenericService<ValidationStatus, ValidationStatusDTO, Long> {

    private final ValidationStatusRepository validationStatusRepository;

    @Override
    protected JpaRepository<ValidationStatus, Long> getRepository() {
        return validationStatusRepository;
    }

    @Override
    protected String getEntityName() {
        return "ValidationStatus";
    }

    @Override
    protected ValidationStatusDTO toDTO(ValidationStatus entity) {
        return ValidationStatusDTO.fromEntity(entity);
    }

    @Override
    protected ValidationStatus toEntity(ValidationStatusDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ValidationStatus entity, ValidationStatusDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ValidationStatusDTO create(ValidationStatusDTO dto) {
        log.info("Creating validation status: code={}", dto.getCode());
        
        if (validationStatusRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException(
                "Validation status with code '" + dto.getCode() + "' already exists"
            );
        }
        
        if (validationStatusRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException(
                "Validation status with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public ValidationStatusDTO update(Long id, ValidationStatusDTO dto) {
        log.info("Updating validation status with ID: {}", id);
        
        if (validationStatusRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException(
                "Validation status with code '" + dto.getCode() + "' already exists"
            );
        }
        
        if (validationStatusRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException(
                "Validation status with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.update(id, dto);
    }

    public List<ValidationStatusDTO> getAll() {
        log.debug("Getting all validation statuses without pagination");
        return validationStatusRepository.findAllByOrderByCodeAsc().stream()
                .map(ValidationStatusDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ValidationStatusDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for validation statuses with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> validationStatusRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    public ValidationStatusDTO findByCode(String code) {
        log.debug("Finding validation status by code: {}", code);
        return validationStatusRepository.findByCode(code)
                .map(ValidationStatusDTO::fromEntity)
                .orElseThrow(() -> new BusinessValidationException(
                    "Validation status with code '" + code + "' not found"
                ));
    }

    public ValidationStatusDTO findByDesignationFr(String designationFr) {
        log.debug("Finding validation status by French designation: {}", designationFr);
        return validationStatusRepository.findByDesignationFr(designationFr)
                .map(ValidationStatusDTO::fromEntity)
                .orElseThrow(() -> new BusinessValidationException(
                    "Validation status with French designation '" + designationFr + "' not found"
                ));
    }
}

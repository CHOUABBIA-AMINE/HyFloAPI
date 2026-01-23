/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: OperationTypeService
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Type
 *
 **/

package dz.sh.trc.hyflo.flow.type.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.flow.type.dto.OperationTypeDTO;
import dz.sh.trc.hyflo.flow.type.model.OperationType;
import dz.sh.trc.hyflo.flow.type.repository.OperationTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for OperationType entity.
 * Provides business logic and CRUD operations for flow operation type management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OperationTypeService extends GenericService<OperationType, OperationTypeDTO, Long> {

    private final OperationTypeRepository operationTypeRepository;

    @Override
    protected JpaRepository<OperationType, Long> getRepository() {
        return operationTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "OperationType";
    }

    @Override
    protected OperationTypeDTO toDTO(OperationType entity) {
        return OperationTypeDTO.fromEntity(entity);
    }

    @Override
    protected OperationType toEntity(OperationTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(OperationType entity, OperationTypeDTO dto) {
        dto.updateEntity(entity);
    }

    /**
     * Create a new operation type with code uniqueness validation.
     *
     * @param dto the operation type DTO to create
     * @return the created operation type DTO
     * @throws BusinessValidationException if code already exists
     */
    @Override
    @Transactional
    public OperationTypeDTO create(OperationTypeDTO dto) {
        log.info("Creating operation type: code={}", dto.getCode());
        
        if (operationTypeRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException(
                "Operation type with code '" + dto.getCode() + "' already exists"
            );
        }
        
        return super.create(dto);
    }

    /**
     * Update an existing operation type with code uniqueness validation.
     *
     * @param id the operation type ID
     * @param dto the operation type DTO with updated data
     * @return the updated operation type DTO
     * @throws BusinessValidationException if code already exists for another record
     */
    @Override
    @Transactional
    public OperationTypeDTO update(Long id, OperationTypeDTO dto) {
        log.info("Updating operation type with ID: {}", id);
        
        if (operationTypeRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException(
                "Operation type with code '" + dto.getCode() + "' already exists"
            );
        }
        
        return super.update(id, dto);
    }

    /**
     * Get all operation types without pagination, sorted by code.
     *
     * @return list of all operation type DTOs
     */
    public List<OperationTypeDTO> getAll() {
        log.debug("Getting all operation types without pagination");
        return operationTypeRepository.findAllByOrderByCodeAsc().stream()
                .map(OperationTypeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Search operation types across all fields (code and names in all languages).
     *
     * @param searchTerm the search term
     * @param pageable pagination parameters
     * @return page of matching operation type DTOs
     */
    public Page<OperationTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for operation types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(
            p -> operationTypeRepository.searchByAnyField(searchTerm.trim(), p), 
            pageable
        );
    }

    /**
     * Find operation type by code.
     *
     * @param code the operation type code
     * @return the operation type DTO if found
     * @throws BusinessValidationException if not found
     */
    public OperationTypeDTO findByCode(String code) {
        log.debug("Finding operation type by code: {}", code);
        return operationTypeRepository.findByCode(code)
                .map(OperationTypeDTO::fromEntity)
                .orElseThrow(() -> new BusinessValidationException(
                    "Operation type with code '" + code + "' not found"
                ));
    }
}

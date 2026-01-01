/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ContractTypeService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Service
 *	@Layer		: Business / Contract
 *	@Package	: Business / Contract / Service
 *
 **/

package dz.mdn.iaas.business.contract.service;

import dz.mdn.iaas.business.contract.dto.ContractTypeDTO;
import dz.mdn.iaas.business.contract.model.ContractType;
import dz.mdn.iaas.business.contract.repository.ContractTypeRepository;
import dz.mdn.iaas.configuration.template.GenericService;
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
 * ContractType Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ContractTypeService extends GenericService<ContractType, ContractTypeDTO, Long> {

    private final ContractTypeRepository contractTypeRepository;

    @Override
    protected JpaRepository<ContractType, Long> getRepository() {
        return contractTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "ContractType";
    }

    @Override
    protected ContractTypeDTO toDTO(ContractType entity) {
        return ContractTypeDTO.fromEntity(entity);
    }

    @Override
    protected ContractType toEntity(ContractTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ContractType entity, ContractTypeDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ContractTypeDTO create(ContractTypeDTO dto) {
        log.info("Creating contract type: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public ContractTypeDTO update(Long id, ContractTypeDTO dto) {
        log.info("Updating contract type with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ContractTypeDTO> getAll() {
        log.debug("Getting all contract types without pagination");
        return contractTypeRepository.findAll().stream()
                .map(ContractTypeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all active contract types
     * Note: ContractType model doesn't have isActive field, so returning all types
     * @return list of all contract types
     */
    public List<ContractTypeDTO> getActiveTypes() {
        log.debug("Getting all contract types (no isActive field in model)");
        return getAll();
    }

    public Page<ContractTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for contract types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

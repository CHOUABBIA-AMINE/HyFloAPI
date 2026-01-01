/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ContractService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Service
 *	@Layer		: Business / Contract
 *	@Package	: Business / Contract / Service
 *
 **/

package dz.mdn.iaas.business.contract.service;

import dz.mdn.iaas.business.contract.dto.ContractDTO;
import dz.mdn.iaas.business.contract.model.Contract;
import dz.mdn.iaas.business.contract.repository.ContractRepository;
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
 * Contract Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ContractService extends GenericService<Contract, ContractDTO, Long> {

    private final ContractRepository contractRepository;

    @Override
    protected JpaRepository<Contract, Long> getRepository() {
        return contractRepository;
    }

    @Override
    protected String getEntityName() {
        return "Contract";
    }

    @Override
    protected ContractDTO toDTO(Contract entity) {
        return ContractDTO.fromEntity(entity);
    }

    @Override
    protected Contract toEntity(ContractDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Contract entity, ContractDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ContractDTO create(ContractDTO dto) {
        log.info("Creating contract: internalId={}, designationFr={}", dto.getInternalId(), dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public ContractDTO update(Long id, ContractDTO dto) {
        log.info("Updating contract with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ContractDTO> getAll() {
        log.debug("Getting all contracts without pagination");
        return contractRepository.findAll().stream()
                .map(ContractDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ContractDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for contracts with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

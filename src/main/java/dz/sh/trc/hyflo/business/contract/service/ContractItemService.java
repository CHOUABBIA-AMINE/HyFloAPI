/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ContractItemService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *	@Type		: Service
 *	@Layer		: Business / Contract
 *	@Package	: Business / Contract / Service
 *
 **/

package dz.sh.trc.hyflo.business.contract.service;

import dz.sh.trc.hyflo.business.contract.dto.ContractItemDTO;
import dz.sh.trc.hyflo.business.contract.model.ContractItem;
import dz.sh.trc.hyflo.business.contract.repository.ContractItemRepository;
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
 * ContractItem Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ContractItemService extends GenericService<ContractItem, ContractItemDTO, Long> {

    private final ContractItemRepository contractItemRepository;

    @Override
    protected JpaRepository<ContractItem, Long> getRepository() {
        return contractItemRepository;
    }

    @Override
    protected String getEntityName() {
        return "ContractItem";
    }

    @Override
    protected ContractItemDTO toDTO(ContractItem entity) {
        return ContractItemDTO.fromEntity(entity);
    }

    @Override
    protected ContractItem toEntity(ContractItemDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ContractItem entity, ContractItemDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ContractItemDTO create(ContractItemDTO dto) {
        log.info("Creating contract item: designation={}", dto.getDesignation());
        return super.create(dto);
    }

    @Override
    @Transactional
    public ContractItemDTO update(Long id, ContractItemDTO dto) {
        log.info("Updating contract item with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ContractItemDTO> getAll() {
        log.debug("Getting all contract items without pagination");
        return contractItemRepository.findAll().stream()
                .map(ContractItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all contract items by contract ID
     * @param contractId the contract ID
     * @return list of contract items
     */
    public List<ContractItemDTO> getByContractId(Long contractId) {
        log.debug("Getting contract items by contract ID: {}", contractId);
        return contractItemRepository.findByContractId(contractId).stream()
                .map(ContractItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ContractItemDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for contract items with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

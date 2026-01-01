/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ContractStepService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *	@Type		: Service
 *	@Layer		: Business / Contract
 *	@Package	: Business / Contract / Service
 *
 **/

package dz.sh.trc.hyflo.business.contract.service;

import dz.sh.trc.hyflo.business.contract.dto.ContractStepDTO;
import dz.sh.trc.hyflo.business.contract.model.ContractStep;
import dz.sh.trc.hyflo.business.contract.repository.ContractStepRepository;
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
 * ContractStep Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ContractStepService extends GenericService<ContractStep, ContractStepDTO, Long> {

    private final ContractStepRepository contractStepRepository;

    @Override
    protected JpaRepository<ContractStep, Long> getRepository() {
        return contractStepRepository;
    }

    @Override
    protected String getEntityName() {
        return "ContractStep";
    }

    @Override
    protected ContractStepDTO toDTO(ContractStep entity) {
        return ContractStepDTO.fromEntity(entity);
    }

    @Override
    protected ContractStep toEntity(ContractStepDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ContractStep entity, ContractStepDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ContractStepDTO create(ContractStepDTO dto) {
        log.info("Creating contract step: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public ContractStepDTO update(Long id, ContractStepDTO dto) {
        log.info("Updating contract step with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ContractStepDTO> getAll() {
        log.debug("Getting all contract steps without pagination");
        return contractStepRepository.findAll().stream()
                .map(ContractStepDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all contract steps by phase ID
     * @param phaseId the contract phase ID
     * @return list of contract steps
     */
    public List<ContractStepDTO> getByPhaseId(Long phaseId) {
        log.debug("Getting contract steps by phase ID: {}", phaseId);
        return contractStepRepository.findByContractPhaseId(phaseId).stream()
                .map(ContractStepDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ContractStepDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for contract steps with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

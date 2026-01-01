/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ContractPhaseService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Service
 *	@Layer		: Business / Contract
 *	@Package	: Business / Contract / Service
 *
 **/

package dz.sh.trc.hyflo.business.contract.service;

import dz.sh.trc.hyflo.business.contract.dto.ContractPhaseDTO;
import dz.sh.trc.hyflo.business.contract.model.ContractPhase;
import dz.sh.trc.hyflo.business.contract.repository.ContractPhaseRepository;
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
 * ContractPhase Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ContractPhaseService extends GenericService<ContractPhase, ContractPhaseDTO, Long> {

    private final ContractPhaseRepository contractPhaseRepository;

    @Override
    protected JpaRepository<ContractPhase, Long> getRepository() {
        return contractPhaseRepository;
    }

    @Override
    protected String getEntityName() {
        return "ContractPhase";
    }

    @Override
    protected ContractPhaseDTO toDTO(ContractPhase entity) {
        return ContractPhaseDTO.fromEntity(entity);
    }

    @Override
    protected ContractPhase toEntity(ContractPhaseDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ContractPhase entity, ContractPhaseDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ContractPhaseDTO create(ContractPhaseDTO dto) {
        log.info("Creating contract phase: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public ContractPhaseDTO update(Long id, ContractPhaseDTO dto) {
        log.info("Updating contract phase with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ContractPhaseDTO> getAll() {
        log.debug("Getting all contract phases without pagination");
        return contractPhaseRepository.findAll().stream()
                .map(ContractPhaseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ContractPhaseDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for contract phases with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

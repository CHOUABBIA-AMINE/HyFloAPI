/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: AmendmentStepService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-12-2025
 *	@Type		: Service
 *	@Layer		: Business / Amendment
 *	@Package	: Business / Amendment / Service
 *
 **/

package dz.sh.trc.hyflo.business.amendment.service;

import dz.sh.trc.hyflo.business.amendment.dto.AmendmentStepDTO;
import dz.sh.trc.hyflo.business.amendment.model.AmendmentStep;
import dz.sh.trc.hyflo.business.amendment.repository.AmendmentStepRepository;
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
 * AmendmentStep Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AmendmentStepService extends GenericService<AmendmentStep, AmendmentStepDTO, Long> {

    private final AmendmentStepRepository amendmentStepRepository;

    @Override
    protected JpaRepository<AmendmentStep, Long> getRepository() {
        return amendmentStepRepository;
    }

    @Override
    protected String getEntityName() {
        return "AmendmentStep";
    }

    @Override
    protected AmendmentStepDTO toDTO(AmendmentStep entity) {
        return AmendmentStepDTO.fromEntity(entity);
    }

    @Override
    protected AmendmentStep toEntity(AmendmentStepDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(AmendmentStep entity, AmendmentStepDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public AmendmentStepDTO create(AmendmentStepDTO dto) {
        log.info("Creating amendment step: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public AmendmentStepDTO update(Long id, AmendmentStepDTO dto) {
        log.info("Updating amendment step with ID: {}", id);
        return super.update(id, dto);
    }

    public List<AmendmentStepDTO> getAll() {
        log.debug("Getting all amendment steps without pagination");
        return amendmentStepRepository.findAll().stream()
                .map(AmendmentStepDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all amendment steps by phase ID
     * @param phaseId the amendment phase ID
     * @return list of amendment steps
     */
    public List<AmendmentStepDTO> getByPhaseId(Long phaseId) {
        log.debug("Getting amendment steps by phase ID: {}", phaseId);
        return amendmentStepRepository.findByAmendmentPhaseId(phaseId).stream()
                .map(AmendmentStepDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<AmendmentStepDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for amendment steps with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

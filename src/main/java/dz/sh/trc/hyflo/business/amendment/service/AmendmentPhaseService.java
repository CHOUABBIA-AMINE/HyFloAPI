/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: AmendmentPhaseService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Service
 *	@Layer		: Business / Amendment
 *	@Package	: Business / Amendment / Service
 *
 **/

package dz.sh.trc.hyflo.business.amendment.service;

import dz.sh.trc.hyflo.business.amendment.dto.AmendmentPhaseDTO;
import dz.sh.trc.hyflo.business.amendment.model.AmendmentPhase;
import dz.sh.trc.hyflo.business.amendment.repository.AmendmentPhaseRepository;
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
 * AmendmentPhase Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AmendmentPhaseService extends GenericService<AmendmentPhase, AmendmentPhaseDTO, Long> {

    private final AmendmentPhaseRepository amendmentPhaseRepository;

    @Override
    protected JpaRepository<AmendmentPhase, Long> getRepository() {
        return amendmentPhaseRepository;
    }

    @Override
    protected String getEntityName() {
        return "AmendmentPhase";
    }

    @Override
    protected AmendmentPhaseDTO toDTO(AmendmentPhase entity) {
        return AmendmentPhaseDTO.fromEntity(entity);
    }

    @Override
    protected AmendmentPhase toEntity(AmendmentPhaseDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(AmendmentPhase entity, AmendmentPhaseDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public AmendmentPhaseDTO create(AmendmentPhaseDTO dto) {
        log.info("Creating amendment phase: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public AmendmentPhaseDTO update(Long id, AmendmentPhaseDTO dto) {
        log.info("Updating amendment phase with ID: {}", id);
        return super.update(id, dto);
    }

    public List<AmendmentPhaseDTO> getAll() {
        log.debug("Getting all amendment phases without pagination");
        return amendmentPhaseRepository.findAll().stream()
                .map(AmendmentPhaseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<AmendmentPhaseDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for amendment phases with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

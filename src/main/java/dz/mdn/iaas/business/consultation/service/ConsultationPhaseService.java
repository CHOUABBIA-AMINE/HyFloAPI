/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ConsultationPhaseService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Service
 *	@Layer		: Business / Consultation
 *	@Package	: Business / Consultation / Service
 *
 **/

package dz.mdn.iaas.business.consultation.service;

import dz.mdn.iaas.business.consultation.dto.ConsultationPhaseDTO;
import dz.mdn.iaas.business.consultation.model.ConsultationPhase;
import dz.mdn.iaas.business.consultation.repository.ConsultationPhaseRepository;
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
 * ConsultationPhase Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ConsultationPhaseService extends GenericService<ConsultationPhase, ConsultationPhaseDTO, Long> {

    private final ConsultationPhaseRepository consultationPhaseRepository;

    @Override
    protected JpaRepository<ConsultationPhase, Long> getRepository() {
        return consultationPhaseRepository;
    }

    @Override
    protected String getEntityName() {
        return "ConsultationPhase";
    }

    @Override
    protected ConsultationPhaseDTO toDTO(ConsultationPhase entity) {
        return ConsultationPhaseDTO.fromEntity(entity);
    }

    @Override
    protected ConsultationPhase toEntity(ConsultationPhaseDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ConsultationPhase entity, ConsultationPhaseDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ConsultationPhaseDTO create(ConsultationPhaseDTO dto) {
        log.info("Creating consultation phase: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public ConsultationPhaseDTO update(Long id, ConsultationPhaseDTO dto) {
        log.info("Updating consultation phase with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ConsultationPhaseDTO> getAll() {
        log.debug("Getting all consultation phases without pagination");
        return consultationPhaseRepository.findAll().stream()
                .map(ConsultationPhaseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ConsultationPhaseDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for consultation phases with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

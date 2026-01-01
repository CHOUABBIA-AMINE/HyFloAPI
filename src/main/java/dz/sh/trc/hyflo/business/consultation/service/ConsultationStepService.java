/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ConsultationStepService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Service
 *	@Layer		: Business / Consultation
 *	@Package	: Business / Consultation / Service
 *
 **/

package dz.sh.trc.hyflo.business.consultation.service;

import dz.sh.trc.hyflo.business.consultation.dto.ConsultationStepDTO;
import dz.sh.trc.hyflo.business.consultation.model.ConsultationStep;
import dz.sh.trc.hyflo.business.consultation.repository.ConsultationStepRepository;
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
 * ConsultationStep Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ConsultationStepService extends GenericService<ConsultationStep, ConsultationStepDTO, Long> {

    private final ConsultationStepRepository consultationStepRepository;

    @Override
    protected JpaRepository<ConsultationStep, Long> getRepository() {
        return consultationStepRepository;
    }

    @Override
    protected String getEntityName() {
        return "ConsultationStep";
    }

    @Override
    protected ConsultationStepDTO toDTO(ConsultationStep entity) {
        return ConsultationStepDTO.fromEntity(entity);
    }

    @Override
    protected ConsultationStep toEntity(ConsultationStepDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ConsultationStep entity, ConsultationStepDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ConsultationStepDTO create(ConsultationStepDTO dto) {
        log.info("Creating consultation step: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public ConsultationStepDTO update(Long id, ConsultationStepDTO dto) {
        log.info("Updating consultation step with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ConsultationStepDTO> getAll() {
        log.debug("Getting all consultation steps without pagination");
        return consultationStepRepository.findAll().stream()
                .map(ConsultationStepDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all consultation steps by phase ID
     * @param phaseId the consultation phase ID
     * @return list of consultation steps
     */
    public List<ConsultationStepDTO> getByPhaseId(Long phaseId) {
        log.debug("Getting consultation steps by phase ID: {}", phaseId);
        return consultationStepRepository.findByConsultationPhaseId(phaseId).stream()
                .map(ConsultationStepDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ConsultationStepDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for consultation steps with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

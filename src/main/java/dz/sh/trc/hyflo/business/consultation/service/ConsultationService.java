/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ConsultationService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Service
 *	@Layer		: Business / Consultation
 *	@Package	: Business / Consultation / Service
 *
 **/

package dz.sh.trc.hyflo.business.consultation.service;

import dz.sh.trc.hyflo.business.consultation.dto.ConsultationDTO;
import dz.sh.trc.hyflo.business.consultation.model.Consultation;
import dz.sh.trc.hyflo.business.consultation.repository.ConsultationRepository;
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
 * Consultation Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ConsultationService extends GenericService<Consultation, ConsultationDTO, Long> {

    private final ConsultationRepository consultationRepository;

    @Override
    protected JpaRepository<Consultation, Long> getRepository() {
        return consultationRepository;
    }

    @Override
    protected String getEntityName() {
        return "Consultation";
    }

    @Override
    protected ConsultationDTO toDTO(Consultation entity) {
        return ConsultationDTO.fromEntity(entity);
    }

    @Override
    protected Consultation toEntity(ConsultationDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Consultation entity, ConsultationDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ConsultationDTO create(ConsultationDTO dto) {
        log.info("Creating consultation: internalId={}", dto.getInternalId());
        return super.create(dto);
    }

    @Override
    @Transactional
    public ConsultationDTO update(Long id, ConsultationDTO dto) {
        log.info("Updating consultation with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ConsultationDTO> getAll() {
        log.debug("Getting all consultations without pagination");
        return consultationRepository.findAll().stream()
                .map(ConsultationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ConsultationDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for consultations with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

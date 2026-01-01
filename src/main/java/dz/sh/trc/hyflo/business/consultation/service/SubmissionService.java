/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: SubmissionService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Service
 *	@Layer		: Business / Consultation
 *	@Package	: Business / Consultation / Service
 *
 **/

package dz.sh.trc.hyflo.business.consultation.service;

import dz.sh.trc.hyflo.business.consultation.dto.SubmissionDTO;
import dz.sh.trc.hyflo.business.consultation.model.Submission;
import dz.sh.trc.hyflo.business.consultation.repository.SubmissionRepository;
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
 * Submission Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SubmissionService extends GenericService<Submission, SubmissionDTO, Long> {

    private final SubmissionRepository submissionRepository;

    @Override
    protected JpaRepository<Submission, Long> getRepository() {
        return submissionRepository;
    }

    @Override
    protected String getEntityName() {
        return "Submission";
    }

    @Override
    protected SubmissionDTO toDTO(Submission entity) {
        return SubmissionDTO.fromEntity(entity);
    }

    @Override
    protected Submission toEntity(SubmissionDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Submission entity, SubmissionDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public SubmissionDTO create(SubmissionDTO dto) {
        log.info("Creating submission for consultation ID: {}", dto.getConsultationId());
        return super.create(dto);
    }

    @Override
    @Transactional
    public SubmissionDTO update(Long id, SubmissionDTO dto) {
        log.info("Updating submission with ID: {}", id);
        return super.update(id, dto);
    }

    public List<SubmissionDTO> getAll() {
        log.debug("Getting all submissions without pagination");
        return submissionRepository.findAll().stream()
                .map(SubmissionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all submissions by consultation ID
     * @param consultationId the consultation ID
     * @return list of submissions
     */
    public List<SubmissionDTO> getByConsultationId(Long consultationId) {
        log.debug("Getting submissions by consultation ID: {}", consultationId);
        return submissionRepository.findByConsultationId(consultationId).stream()
                .map(SubmissionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<SubmissionDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for submissions with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

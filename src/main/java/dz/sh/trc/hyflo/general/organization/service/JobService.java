/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: JobService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.general.organization.dto.JobDTO;
import dz.sh.trc.hyflo.general.organization.model.Job;
import dz.sh.trc.hyflo.general.organization.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Job Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class JobService extends GenericService<Job, JobDTO, Long> {

    private final JobRepository jobRepository;

    @Override
    protected JpaRepository<Job, Long> getRepository() {
        return jobRepository;
    }

    @Override
    protected String getEntityName() {
        return "Job";
    }

    @Override
    protected JobDTO toDTO(Job entity) {
        return JobDTO.fromEntity(entity);
    }

    @Override
    protected Job toEntity(JobDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Job entity, JobDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public JobDTO create(JobDTO dto) {
        log.info("Creating job: code={}, designationFr={}", dto.getCode(), dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public JobDTO update(Long id, JobDTO dto) {
        log.info("Updating job with ID: {}", id);
        return super.update(id, dto);
    }

    public List<JobDTO> getAll() {
        log.debug("Getting all jobs without pagination");
        return jobRepository.findAll().stream()
                .map(JobDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<JobDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for jobs with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> jobRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    public List<JobDTO> getByStructureId(Long structureId) {
        log.debug("Getting localities by structure ID: {}", structureId);
        return jobRepository.findByStructureId(structureId).stream()
                .map(JobDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

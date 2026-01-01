/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: RubricService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Service
 *	@Layer		: Business / Plan
 *	@Package	: Business / Plan / Service
 *
 **/

package dz.mdn.iaas.business.plan.service;

import dz.mdn.iaas.business.plan.dto.RubricDTO;
import dz.mdn.iaas.business.plan.model.Rubric;
import dz.mdn.iaas.business.plan.repository.RubricRepository;
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
 * Rubric Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RubricService extends GenericService<Rubric, RubricDTO, Long> {

    private final RubricRepository rubricRepository;

    @Override
    protected JpaRepository<Rubric, Long> getRepository() {
        return rubricRepository;
    }

    @Override
    protected String getEntityName() {
        return "Rubric";
    }

    @Override
    protected RubricDTO toDTO(Rubric entity) {
        return RubricDTO.fromEntity(entity);
    }

    @Override
    protected Rubric toEntity(RubricDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Rubric entity, RubricDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public RubricDTO create(RubricDTO dto) {
        log.info("Creating rubric: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public RubricDTO update(Long id, RubricDTO dto) {
        log.info("Updating rubric with ID: {}", id);
        return super.update(id, dto);
    }

    public List<RubricDTO> getAll() {
        log.debug("Getting all rubrics without pagination");
        return rubricRepository.findAll().stream()
                .map(RubricDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<RubricDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for rubrics with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

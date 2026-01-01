/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: AwardMethodService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Service
 *	@Layer		: Business / Consultation
 *	@Package	: Business / Consultation / Service
 *
 **/

package dz.sh.trc.hyflo.business.consultation.service;

import dz.sh.trc.hyflo.business.consultation.dto.AwardMethodDTO;
import dz.sh.trc.hyflo.business.consultation.model.AwardMethod;
import dz.sh.trc.hyflo.business.consultation.repository.AwardMethodRepository;
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
 * AwardMethod Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AwardMethodService extends GenericService<AwardMethod, AwardMethodDTO, Long> {

    private final AwardMethodRepository awardMethodRepository;

    @Override
    protected JpaRepository<AwardMethod, Long> getRepository() {
        return awardMethodRepository;
    }

    @Override
    protected String getEntityName() {
        return "AwardMethod";
    }

    @Override
    protected AwardMethodDTO toDTO(AwardMethod entity) {
        return AwardMethodDTO.fromEntity(entity);
    }

    @Override
    protected AwardMethod toEntity(AwardMethodDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(AwardMethod entity, AwardMethodDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public AwardMethodDTO create(AwardMethodDTO dto) {
        log.info("Creating award method: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public AwardMethodDTO update(Long id, AwardMethodDTO dto) {
        log.info("Updating award method with ID: {}", id);
        return super.update(id, dto);
    }

    public List<AwardMethodDTO> getAll() {
        log.debug("Getting all award methods without pagination");
        return awardMethodRepository.findAll().stream()
                .map(AwardMethodDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all active award methods
     * Note: AwardMethod model doesn't have isActive field, so returning all methods
     * @return list of all award methods
     */
    public List<AwardMethodDTO> getActiveMethods() {
        log.debug("Getting all award methods (no isActive field in model)");
        return getAll();
    }

    public Page<AwardMethodDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for award methods with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ExclusionTypeService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Service
 *	@Layer		: Business / Provider
 *	@Package	: Business / Provider / Service
 *
 **/

package dz.sh.trc.hyflo.business.provider.service;

import dz.sh.trc.hyflo.business.provider.dto.ExclusionTypeDTO;
import dz.sh.trc.hyflo.business.provider.model.ExclusionType;
import dz.sh.trc.hyflo.business.provider.repository.ExclusionTypeRepository;
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
 * ExclusionType Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ExclusionTypeService extends GenericService<ExclusionType, ExclusionTypeDTO, Long> {

    private final ExclusionTypeRepository exclusionTypeRepository;

    @Override
    protected JpaRepository<ExclusionType, Long> getRepository() {
        return exclusionTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "ExclusionType";
    }

    @Override
    protected ExclusionTypeDTO toDTO(ExclusionType entity) {
        return ExclusionTypeDTO.fromEntity(entity);
    }

    @Override
    protected ExclusionType toEntity(ExclusionTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ExclusionType entity, ExclusionTypeDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ExclusionTypeDTO create(ExclusionTypeDTO dto) {
        log.info("Creating exclusion type: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public ExclusionTypeDTO update(Long id, ExclusionTypeDTO dto) {
        log.info("Updating exclusion type with ID: {}", id);
        return super.update(id, dto);
    }

    public List<ExclusionTypeDTO> getAll() {
        log.debug("Getting all exclusion types without pagination");
        return exclusionTypeRepository.findAll().stream()
                .map(ExclusionTypeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ExclusionTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for exclusion types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

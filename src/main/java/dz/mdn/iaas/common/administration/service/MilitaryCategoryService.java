/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MilitaryCategoryService
 *	@CreatedOn	: 10-14-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Common / Administration
 *	@Package	: Common / Administration / Service
 *
 **/

package dz.mdn.iaas.common.administration.service;

import dz.mdn.iaas.common.administration.dto.MilitaryCategoryDTO;
import dz.mdn.iaas.common.administration.model.MilitaryCategory;
import dz.mdn.iaas.common.administration.repository.MilitaryCategoryRepository;
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
 * MilitaryCategory Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MilitaryCategoryService extends GenericService<MilitaryCategory, MilitaryCategoryDTO, Long> {

    private final MilitaryCategoryRepository militaryCategoryRepository;

    @Override
    protected JpaRepository<MilitaryCategory, Long> getRepository() {
        return militaryCategoryRepository;
    }

    @Override
    protected String getEntityName() {
        return "MilitaryCategory";
    }

    @Override
    protected MilitaryCategoryDTO toDTO(MilitaryCategory entity) {
        return MilitaryCategoryDTO.fromEntity(entity);
    }

    @Override
    protected MilitaryCategory toEntity(MilitaryCategoryDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(MilitaryCategory entity, MilitaryCategoryDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public MilitaryCategoryDTO create(MilitaryCategoryDTO dto) {
        log.info("Creating military category: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public MilitaryCategoryDTO update(Long id, MilitaryCategoryDTO dto) {
        log.info("Updating military category with ID: {}", id);
        return super.update(id, dto);
    }

    public List<MilitaryCategoryDTO> getAll() {
        log.debug("Getting all military categories without pagination");
        return militaryCategoryRepository.findAll().stream()
                .map(MilitaryCategoryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<MilitaryCategoryDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for military categories with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MilitaryRankService
 *	@CreatedOn	: 10-14-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Common / Administration
 *	@Package	: Common / Administration / Service
 *
 **/

package dz.sh.trc.hyflo.common.administration.service;

import dz.sh.trc.hyflo.common.administration.dto.MilitaryRankDTO;
import dz.sh.trc.hyflo.common.administration.model.MilitaryCategory;
import dz.sh.trc.hyflo.common.administration.model.MilitaryRank;
import dz.sh.trc.hyflo.common.administration.repository.MilitaryRankRepository;
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
 * MilitaryRank Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MilitaryRankService extends GenericService<MilitaryRank, MilitaryRankDTO, Long> {

    private final MilitaryRankRepository militaryRankRepository;
    private final MilitaryCategoryService militaryCategoryService;

    @Override
    protected JpaRepository<MilitaryRank, Long> getRepository() {
        return militaryRankRepository;
    }

    @Override
    protected String getEntityName() {
        return "MilitaryRank";
    }

    @Override
    protected MilitaryRankDTO toDTO(MilitaryRank entity) {
        return MilitaryRankDTO.fromEntity(entity);
    }

    @Override
    protected MilitaryRank toEntity(MilitaryRankDTO dto) {
        MilitaryRank entity = dto.toEntity();
        
        // Set relationships
        if (dto.getMilitaryCategoryId() != null) {
            MilitaryCategory militaryCategory = militaryCategoryService.getEntityById(dto.getMilitaryCategoryId());
            entity.setMilitaryCategory(militaryCategory);
        }
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(MilitaryRank entity, MilitaryRankDTO dto) {
        dto.updateEntity(entity);
        
        // Set relationships
        if (dto.getMilitaryCategoryId() != null) {
            MilitaryCategory militaryCategory = militaryCategoryService.getEntityById(dto.getMilitaryCategoryId());
            entity.setMilitaryCategory(militaryCategory);
        }
    }

    @Override
    @Transactional
    public MilitaryRankDTO create(MilitaryRankDTO dto) {
        log.info("Creating military rank: acronymFr={}, categoryId={}", 
                dto.getAcronymFr(), dto.getMilitaryCategoryId());
        return super.create(dto);
    }

    @Override
    @Transactional
    public MilitaryRankDTO update(Long id, MilitaryRankDTO dto) {
        log.info("Updating military rank with ID: {}", id);
        return super.update(id, dto);
    }

    public List<MilitaryRankDTO> getAll() {
        log.debug("Getting all military ranks without pagination");
        return militaryRankRepository.findAll().stream()
                .map(MilitaryRankDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<MilitaryRankDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for military ranks with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }

    public List<MilitaryRankDTO> getByMilitaryCategoryId(Long militaryCategoryId) {
        log.debug("Getting military ranks by Military category ID: {}", militaryCategoryId);
        return militaryRankRepository.findByMilitaryCategoryId(militaryCategoryId).stream()
                .map(MilitaryRankDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

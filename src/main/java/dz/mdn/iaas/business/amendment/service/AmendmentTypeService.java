/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: AmendmentTypeService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-13-2025
 *	@Type		: Service
 *	@Layer		: Business / Amendment
 *	@Package	: Business / Amendment / Service
 *
 **/

package dz.mdn.iaas.business.amendment.service;

import dz.mdn.iaas.business.amendment.dto.AmendmentTypeDTO;
import dz.mdn.iaas.business.amendment.model.AmendmentType;
import dz.mdn.iaas.business.amendment.repository.AmendmentTypeRepository;
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
 * AmendmentType Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AmendmentTypeService extends GenericService<AmendmentType, AmendmentTypeDTO, Long> {

    private final AmendmentTypeRepository amendmentTypeRepository;

    @Override
    protected JpaRepository<AmendmentType, Long> getRepository() {
        return amendmentTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "AmendmentType";
    }

    @Override
    protected AmendmentTypeDTO toDTO(AmendmentType entity) {
        return AmendmentTypeDTO.fromEntity(entity);
    }

    @Override
    protected AmendmentType toEntity(AmendmentTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(AmendmentType entity, AmendmentTypeDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public AmendmentTypeDTO create(AmendmentTypeDTO dto) {
        log.info("Creating amendment type: designationFr={}", dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public AmendmentTypeDTO update(Long id, AmendmentTypeDTO dto) {
        log.info("Updating amendment type with ID: {}", id);
        return super.update(id, dto);
    }

    public List<AmendmentTypeDTO> getAll() {
        log.debug("Getting all amendment types without pagination");
        return amendmentTypeRepository.findAll().stream()
                .map(AmendmentTypeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all active amendment types
     * Note: AmendmentType model doesn't have isActive field, so returning all types
     * @return list of all amendment types
     */
    public List<AmendmentTypeDTO> getActiveTypes() {
        log.debug("Getting all amendment types (no isActive field in model)");
        return getAll();
    }

    public Page<AmendmentTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for amendment types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

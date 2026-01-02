/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: StructureTypeService
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.service;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.network.type.dto.StructureTypeDTO;
import dz.sh.trc.hyflo.network.type.model.StructureType;
import dz.sh.trc.hyflo.network.type.repository.StructureTypeRepository;
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
 * StructureType Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StructureTypeService extends GenericService<StructureType, StructureTypeDTO, Long> {

    private final StructureTypeRepository structureTypeRepository;

    @Override
    protected JpaRepository<StructureType, Long> getRepository() {
        return structureTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "StructureType";
    }

    @Override
    protected StructureTypeDTO toDTO(StructureType entity) {
        return StructureTypeDTO.fromEntity(entity);
    }

    @Override
    protected StructureType toEntity(StructureTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(StructureType entity, StructureTypeDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public StructureTypeDTO create(StructureTypeDTO dto) {
        log.info("Creating structure type: acronymFr={}", dto.getAcronymFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public StructureTypeDTO update(Long id, StructureTypeDTO dto) {
        log.info("Updating structure type with ID: {}", id);
        return super.update(id, dto);
    }

    public List<StructureTypeDTO> getAll() {
        log.debug("Getting all structure types without pagination");
        return structureTypeRepository.findAll().stream()
                .map(StructureTypeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<StructureTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for structure types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }
}

/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: StructureService
 *	@CreatedOn	: 10-14-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Common / Administration
 *	@Package	: Common / Administration / Service
 *
 **/

package dz.mdn.iaas.common.administration.service;

import dz.mdn.iaas.common.administration.dto.StructureDTO;
import dz.mdn.iaas.common.administration.model.Structure;
import dz.mdn.iaas.common.administration.model.StructureType;
import dz.mdn.iaas.common.administration.repository.StructureRepository;
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
 * Structure Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StructureService extends GenericService<Structure, StructureDTO, Long> {

    private final StructureRepository structureRepository;
    private final StructureTypeService structureTypeService;

    @Override
    protected JpaRepository<Structure, Long> getRepository() {
        return structureRepository;
    }

    @Override
    protected String getEntityName() {
        return "Structure";
    }

    @Override
    protected StructureDTO toDTO(Structure entity) {
        return StructureDTO.fromEntity(entity);
    }

    @Override
    protected Structure toEntity(StructureDTO dto) {
        Structure entity = dto.toEntity();
        
        // Set relationships
        if (dto.getParentStructureId() != null) {
            Structure parentStructure = getEntityById(dto.getParentStructureId());
            entity.setParentStructure(parentStructure);
        }
        
        if (dto.getStructureTypeId() != null) {
            StructureType structureType = structureTypeService.getEntityById(dto.getStructureTypeId());
            entity.setStructureType(structureType);
        }
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Structure entity, StructureDTO dto) {
        dto.updateEntity(entity);
        
        // Update relationships
        if (dto.getParentStructureId() != null) {
            Structure parentStructure = getEntityById(dto.getParentStructureId());
            entity.setParentStructure(parentStructure);
        }
        
        if (dto.getStructureTypeId() != null) {
            StructureType structureType = structureTypeService.getEntityById(dto.getStructureTypeId());
            entity.setStructureType(structureType);
        }
    }

    @Override
    @Transactional
    public StructureDTO create(StructureDTO dto) {
        log.info("Creating structure: code={}, designationFr={}, structureTypeId={}", 
                dto.getCode(), dto.getDesignationFr(), dto.getStructureTypeId());
        return super.create(dto);
    }

    @Override
    @Transactional
    public StructureDTO update(Long id, StructureDTO dto) {
        log.info("Updating structure with ID: {}", id);
        return super.update(id, dto);
    }

    public List<StructureDTO> getAll() {
        log.debug("Getting all structures without pagination");
        return structureRepository.findAll().stream()
                .map(StructureDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<StructureDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for structures with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return getAll(pageable);
    }

    public List<StructureDTO> getByStructureTypeId(Long structureTypeId) {
        log.debug("Getting structures by structure type ID: {}", structureTypeId);
        return structureRepository.findByStructureTypeId(structureTypeId).stream()
                .map(StructureDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<StructureDTO> getByParentStructureId(Long parentStructureId) {
        log.debug("Getting structures by parent structure ID: {}", parentStructureId);
        return structureRepository.findByParentStructureId(parentStructureId).stream()
                .map(StructureDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<StructureDTO> getRootStructures() {
        log.debug("Getting root structures (no parent)");
        return structureRepository.findByParentStructureIsNull().stream()
                .map(StructureDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

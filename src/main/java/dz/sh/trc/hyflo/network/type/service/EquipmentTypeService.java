/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: EquipmentTypeService
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Type
 *
 **/

package dz.sh.trc.hyflo.network.type.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.network.type.dto.EquipmentTypeDTO;
import dz.sh.trc.hyflo.network.type.model.EquipmentType;
import dz.sh.trc.hyflo.network.type.repository.EquipmentTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EquipmentTypeService extends GenericService<EquipmentType, EquipmentTypeDTO, Long> {

    private final EquipmentTypeRepository equipmentTypeRepository;

    @Override
    protected JpaRepository<EquipmentType, Long> getRepository() {
        return equipmentTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "EquipmentType";
    }

    @Override
    protected EquipmentTypeDTO toDTO(EquipmentType entity) {
        return EquipmentTypeDTO.fromEntity(entity);
    }

    @Override
    protected EquipmentType toEntity(EquipmentTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(EquipmentType entity, EquipmentTypeDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public EquipmentTypeDTO create(EquipmentTypeDTO dto) {
        log.info("Creating equipment type: code={}", dto.getCode());
        
        if (equipmentTypeRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Equipment type with code '" + dto.getCode() + "' already exists");
        }

        if (equipmentTypeRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException("Equipment type with designationFr '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public EquipmentTypeDTO update(Long id, EquipmentTypeDTO dto) {
        log.info("Updating equipment type with ID: {}", id);
        
        if (equipmentTypeRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Equipment type with code '" + dto.getCode() + "' already exists");
        }

        if (equipmentTypeRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException("Equipment type with designationFr '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<EquipmentTypeDTO> getAll() {
        log.debug("Getting all equipment types without pagination");
        return equipmentTypeRepository.findAll().stream()
                .map(EquipmentTypeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<EquipmentTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for equipment types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> equipmentTypeRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}

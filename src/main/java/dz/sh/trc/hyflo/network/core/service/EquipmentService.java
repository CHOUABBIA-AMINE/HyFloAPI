/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: EquipmentService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.network.core.dto.EquipmentDTO;
import dz.sh.trc.hyflo.network.core.model.Equipment;
import dz.sh.trc.hyflo.network.core.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EquipmentService extends GenericService<Equipment, EquipmentDTO, Long> {

    private final EquipmentRepository equipmentRepository;

    @Override
    protected JpaRepository<Equipment, Long> getRepository() {
        return equipmentRepository;
    }

    @Override
    protected String getEntityName() {
        return "Equipment";
    }

    @Override
    protected EquipmentDTO toDTO(Equipment entity) {
        return EquipmentDTO.fromEntity(entity);
    }

    @Override
    protected Equipment toEntity(EquipmentDTO dto) {
        Equipment entity = dto.toEntity();
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(Equipment entity, EquipmentDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public EquipmentDTO create(EquipmentDTO dto) {
        log.info("Creating equipment: code={}", dto.getCode());
        
        if (equipmentRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Equipment with code '" + dto.getCode() + "' already exists");
        }
        
        if (equipmentRepository.existsBySerialNumber(dto.getSerialNumber())) {
            throw new BusinessValidationException("Equipment with serial number '" + dto.getSerialNumber() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public EquipmentDTO update(Long id, EquipmentDTO dto) {
        log.info("Updating equipment with ID: {}", id);
        
        if (equipmentRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Equipment with code '" + dto.getCode() + "' already exists");
        }
        
        if (equipmentRepository.existsBySerialNumberAndIdNot(dto.getSerialNumber(), id)) {
            throw new BusinessValidationException("Equipment with serial number '" + dto.getSerialNumber() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<EquipmentDTO> getAll() {
        log.debug("Getting all equipment without pagination");
        return equipmentRepository.findAll().stream()
                .map(EquipmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<EquipmentDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for equipment with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> equipmentRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    public List<EquipmentDTO> findByFacility(Long facilityId) {
        log.debug("Finding equipment by facility id: {}", facilityId);
        return equipmentRepository.findByFacilityId(facilityId).stream()
                .map(EquipmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<EquipmentDTO> findByEquipmentType(Long typeId) {
        log.debug("Finding equipment by equipment type id: {}", typeId);
        return equipmentRepository.findByEquipmentTypeId(typeId).stream()
                .map(EquipmentDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

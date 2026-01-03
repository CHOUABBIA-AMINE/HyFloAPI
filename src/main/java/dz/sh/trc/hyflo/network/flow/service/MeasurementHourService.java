/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: MeasurementHourService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.network.flow.dto.MeasurementHourDTO;
import dz.sh.trc.hyflo.network.flow.model.MeasurementHour;
import dz.sh.trc.hyflo.network.flow.repository.MeasurementHourRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MeasurementHourService extends GenericService<MeasurementHour, MeasurementHourDTO, Long> {

    private final MeasurementHourRepository equipmentTypeRepository;

    @Override
    protected JpaRepository<MeasurementHour, Long> getRepository() {
        return equipmentTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "MeasurementHour";
    }

    @Override
    protected MeasurementHourDTO toDTO(MeasurementHour entity) {
        return MeasurementHourDTO.fromEntity(entity);
    }

    @Override
    protected MeasurementHour toEntity(MeasurementHourDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(MeasurementHour entity, MeasurementHourDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public MeasurementHourDTO create(MeasurementHourDTO dto) {
        log.info("Creating equipment type: code={}", dto.getCode());
        
        if (equipmentTypeRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Equipment type with code '" + dto.getCode() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public MeasurementHourDTO update(Long id, MeasurementHourDTO dto) {
        log.info("Updating equipment type with ID: {}", id);
        
        if (equipmentTypeRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Equipment type with code '" + dto.getCode() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<MeasurementHourDTO> getAll() {
        log.debug("Getting all equipment types without pagination");
        return equipmentTypeRepository.findAll().stream()
                .map(MeasurementHourDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<MeasurementHourDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for equipment types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> equipmentTypeRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}

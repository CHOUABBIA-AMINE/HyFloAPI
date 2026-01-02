/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: FacilityTypeService
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
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
import dz.sh.trc.hyflo.network.type.dto.FacilityTypeDTO;
import dz.sh.trc.hyflo.network.type.model.FacilityType;
import dz.sh.trc.hyflo.network.type.repository.FacilityTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FacilityTypeService extends GenericService<FacilityType, FacilityTypeDTO, Long> {

    private final FacilityTypeRepository facilityTypeRepository;

    @Override
    protected JpaRepository<FacilityType, Long> getRepository() {
        return facilityTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "FacilityType";
    }

    @Override
    protected FacilityTypeDTO toDTO(FacilityType entity) {
        return FacilityTypeDTO.fromEntity(entity);
    }

    @Override
    protected FacilityType toEntity(FacilityTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(FacilityType entity, FacilityTypeDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public FacilityTypeDTO create(FacilityTypeDTO dto) {
        log.info("Creating facility type: code={}", dto.getCode());
        
        if (facilityTypeRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException("Facility type with code '" + dto.getCode() + "' already exists");
        }

        if (facilityTypeRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException("Facility type with designationFr '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public FacilityTypeDTO update(Long id, FacilityTypeDTO dto) {
        log.info("Updating facility type with ID: {}", id);
        
        if (facilityTypeRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException("Facility type with code '" + dto.getCode() + "' already exists");
        }

        if (facilityTypeRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException("Facility type with designationFr '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<FacilityTypeDTO> getAll() {
        log.debug("Getting all facility types without pagination");
        return facilityTypeRepository.findAll().stream()
                .map(FacilityTypeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<FacilityTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for facility types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> facilityTypeRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}

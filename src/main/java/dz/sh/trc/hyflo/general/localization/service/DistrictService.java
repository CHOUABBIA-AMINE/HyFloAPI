/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: DistrictService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.general.localization.dto.DistrictDTO;
import dz.sh.trc.hyflo.general.localization.model.District;
import dz.sh.trc.hyflo.general.localization.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DistrictService extends GenericService<District, DistrictDTO, Long> {

    private final DistrictRepository districtRepository;

    @Override
    protected JpaRepository<District, Long> getRepository() {
        return districtRepository;
    }

    @Override
    protected String getEntityName() {
        return "District";
    }

    @Override
    protected DistrictDTO toDTO(District entity) {
        return DistrictDTO.fromEntity(entity);
    }

    @Override
    protected District toEntity(DistrictDTO dto) {
        District entity = dto.toEntity();
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(District entity, DistrictDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public DistrictDTO create(DistrictDTO dto) {
        log.info("Creating district: code={}, designationFr={}, stateId={}", 
                dto.getCode(), dto.getDesignationFr(), dto.getStateId());
        return super.create(dto);
    }

    @Override
    @Transactional
    public DistrictDTO update(Long id, DistrictDTO dto) {
        log.info("Updating district with ID: {}", id);
        return super.update(id, dto);
    }

    public List<DistrictDTO> getAll() {
        log.debug("Getting all localities without pagination");
        return districtRepository.findAll().stream()
                .map(DistrictDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<DistrictDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for localities with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> districtRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    public List<DistrictDTO> getByStateId(Long stateId) {
        log.debug("Getting localities by state ID: {}", stateId);
        return districtRepository.findByStateId(stateId).stream()
                .map(DistrictDTO::fromEntity)
                .collect(Collectors.toList());
    }
}

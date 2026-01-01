/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ProcurementDirectorService
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-10-2025
 *	@Type		: Service
 *	@Layer		: Business / Core
 *	@Package	: Business / Core / Service
 *
 **/

package dz.mdn.iaas.business.core.service;

import dz.mdn.iaas.business.core.dto.ProcurementDirectorDTO;
import dz.mdn.iaas.business.core.model.ProcurementDirector;
import dz.mdn.iaas.business.core.repository.ProcurementDirectorRepository;
import dz.mdn.iaas.configuration.template.GenericService;
import dz.mdn.iaas.exception.BusinessValidationException;
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
 * ProcurementDirector Service - Extends GenericService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProcurementDirectorService extends GenericService<ProcurementDirector, ProcurementDirectorDTO, Long> {

    private final ProcurementDirectorRepository procurementDirectorRepository;

    @Override
    protected JpaRepository<ProcurementDirector, Long> getRepository() {
        return procurementDirectorRepository;
    }

    @Override
    protected String getEntityName() {
        return "ProcurementDirector";
    }

    @Override
    protected ProcurementDirectorDTO toDTO(ProcurementDirector entity) {
        return ProcurementDirectorDTO.fromEntity(entity);
    }

    @Override
    protected ProcurementDirector toEntity(ProcurementDirectorDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(ProcurementDirector entity, ProcurementDirectorDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public ProcurementDirectorDTO create(ProcurementDirectorDTO dto) {
        log.info("Creating procurement director: designation={}", dto.getDesignationFr());
        
        if (procurementDirectorRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException("Designation '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public ProcurementDirectorDTO update(Long id, ProcurementDirectorDTO dto) {
        log.info("Updating procurement director with ID: {}", id);
        
        if (procurementDirectorRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException("Designation '" + dto.getDesignationFr() + "' already exists");
        }
        
        return super.update(id, dto);
    }

    public List<ProcurementDirectorDTO> getAll() {
        log.debug("Getting all procurement directors without pagination");
        return procurementDirectorRepository.findAll().stream()
                .map(ProcurementDirectorDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<ProcurementDirectorDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for procurement directors with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> procurementDirectorRepository.searchByDesignation(searchTerm.trim(), p), pageable);
    }

    public boolean existsByDesignation(String designation) {
        return procurementDirectorRepository.existsByDesignationFr(designation);
    }
}

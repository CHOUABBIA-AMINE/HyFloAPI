/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: SeverityService
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.flow.common.dto.SeverityDTO;
import dz.sh.trc.hyflo.flow.common.model.Severity;
import dz.sh.trc.hyflo.flow.common.repository.SeverityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SeverityService extends GenericService<Severity, SeverityDTO, Long> {

    private final SeverityRepository severityRepository;

    @Override
    protected JpaRepository<Severity, Long> getRepository() {
        return severityRepository;
    }

    @Override
    protected String getEntityName() {
        return "Severity";
    }

    @Override
    protected SeverityDTO toDTO(Severity entity) {
        return SeverityDTO.fromEntity(entity);
    }

    @Override
    protected Severity toEntity(SeverityDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Severity entity, SeverityDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public SeverityDTO create(SeverityDTO dto) {
        
        if (severityRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException(
                "Severity with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public SeverityDTO update(Long id, SeverityDTO dto) {
        
        if (severityRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException(
                "Severity with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.update(id, dto);
    }

    public List<SeverityDTO> getAll() {
        log.debug("Getting all severities without pagination");
        return severityRepository.findAll().stream()
                .map(SeverityDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<SeverityDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for severities with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> severityRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    public SeverityDTO findByDesignationFr(String designationFr) {
        log.debug("Finding severity by French designation: {}", designationFr);
        return severityRepository.findByDesignationFr(designationFr)
                .map(SeverityDTO::fromEntity)
                .orElseThrow(() -> new BusinessValidationException(
                    "Severity with French designation '" + designationFr + "' not found"
                ));
    }
}

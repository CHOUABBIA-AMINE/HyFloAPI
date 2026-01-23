/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: AlertStatusService
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
import dz.sh.trc.hyflo.flow.common.dto.AlertStatusDTO;
import dz.sh.trc.hyflo.flow.common.model.AlertStatus;
import dz.sh.trc.hyflo.flow.common.repository.AlertStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AlertStatusService extends GenericService<AlertStatus, AlertStatusDTO, Long> {

    private final AlertStatusRepository alertStatusRepository;

    @Override
    protected JpaRepository<AlertStatus, Long> getRepository() {
        return alertStatusRepository;
    }

    @Override
    protected String getEntityName() {
        return "AlertStatus";
    }

    @Override
    protected AlertStatusDTO toDTO(AlertStatus entity) {
        return AlertStatusDTO.fromEntity(entity);
    }

    @Override
    protected AlertStatus toEntity(AlertStatusDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(AlertStatus entity, AlertStatusDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public AlertStatusDTO create(AlertStatusDTO dto) {
        
        if (alertStatusRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException(
                "Alert status with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public AlertStatusDTO update(Long id, AlertStatusDTO dto) {
        
        if (alertStatusRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException(
                "Alert status with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.update(id, dto);
    }

    public List<AlertStatusDTO> getAll() {
        log.debug("Getting all alert statuses without pagination");
        return alertStatusRepository.findAll().stream()
                .map(AlertStatusDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<AlertStatusDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for alert statuses with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> alertStatusRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    public AlertStatusDTO findByDesignationFr(String designationFr) {
        log.debug("Finding alert status by French designation: {}", designationFr);
        return alertStatusRepository.findByDesignationFr(designationFr)
                .map(AlertStatusDTO::fromEntity)
                .orElseThrow(() -> new BusinessValidationException(
                    "Alert status with French designation '" + designationFr + "' not found"
                ));
    }
}

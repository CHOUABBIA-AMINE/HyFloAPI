/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: EventStatusService
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
import dz.sh.trc.hyflo.flow.common.dto.EventStatusDTO;
import dz.sh.trc.hyflo.flow.common.model.EventStatus;
import dz.sh.trc.hyflo.flow.common.repository.EventStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventStatusService extends GenericService<EventStatus, EventStatusDTO, Long> {

    private final EventStatusRepository eventStatusRepository;

    @Override
    protected JpaRepository<EventStatus, Long> getRepository() {
        return eventStatusRepository;
    }

    @Override
    protected String getEntityName() {
        return "EventStatus";
    }

    @Override
    protected EventStatusDTO toDTO(EventStatus entity) {
        return EventStatusDTO.fromEntity(entity);
    }

    @Override
    protected EventStatus toEntity(EventStatusDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(EventStatus entity, EventStatusDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public EventStatusDTO create(EventStatusDTO dto) {
        log.info("Creating event status: code={}", dto.getCode());
        
        if (eventStatusRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException(
                "Event status with code '" + dto.getCode() + "' already exists"
            );
        }
        
        if (eventStatusRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException(
                "Event status with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public EventStatusDTO update(Long id, EventStatusDTO dto) {
        log.info("Updating event status with ID: {}", id);
        
        if (eventStatusRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException(
                "Event status with code '" + dto.getCode() + "' already exists"
            );
        }
        
        if (eventStatusRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException(
                "Event status with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.update(id, dto);
    }

    public List<EventStatusDTO> getAll() {
        log.debug("Getting all event statuses without pagination");
        return eventStatusRepository.findAllByOrderByCodeAsc().stream()
                .map(EventStatusDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<EventStatusDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for event statuses with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> eventStatusRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }

    public EventStatusDTO findByCode(String code) {
        log.debug("Finding event status by code: {}", code);
        return eventStatusRepository.findByCode(code)
                .map(EventStatusDTO::fromEntity)
                .orElseThrow(() -> new BusinessValidationException(
                    "Event status with code '" + code + "' not found"
                ));
    }

    public EventStatusDTO findByDesignationFr(String designationFr) {
        log.debug("Finding event status by French designation: {}", designationFr);
        return eventStatusRepository.findByDesignationFr(designationFr)
                .map(EventStatusDTO::fromEntity)
                .orElseThrow(() -> new BusinessValidationException(
                    "Event status with French designation '" + designationFr + "' not found"
                ));
    }
}

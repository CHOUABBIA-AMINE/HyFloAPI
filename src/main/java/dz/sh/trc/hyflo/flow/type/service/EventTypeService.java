/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: EventTypeService
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Service
 * 	@Package	: Flow / Type
 *
 **/

package dz.sh.trc.hyflo.flow.type.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.BusinessValidationException;
import dz.sh.trc.hyflo.flow.type.dto.EventTypeDTO;
import dz.sh.trc.hyflo.flow.type.model.EventType;
import dz.sh.trc.hyflo.flow.type.repository.EventTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for EventType entity.
 * Provides business logic and CRUD operations for operational event type management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventTypeService extends GenericService<EventType, EventTypeDTO, Long> {

    private final EventTypeRepository eventTypeRepository;

    @Override
    protected JpaRepository<EventType, Long> getRepository() {
        return eventTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "EventType";
    }

    @Override
    protected EventTypeDTO toDTO(EventType entity) {
        return EventTypeDTO.fromEntity(entity);
    }

    @Override
    protected EventType toEntity(EventTypeDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(EventType entity, EventTypeDTO dto) {
        dto.updateEntity(entity);
    }

    /**
     * Create a new event type with dual uniqueness validation (code and French designation).
     *
     * @param dto the event type DTO to create
     * @return the created event type DTO
     * @throws BusinessValidationException if code or French designation already exists
     */
    @Override
    @Transactional
    public EventTypeDTO create(EventTypeDTO dto) {
        log.info("Creating event type: code={}, designationFr={}", dto.getCode(), dto.getDesignationFr());
        
        if (eventTypeRepository.existsByCode(dto.getCode())) {
            throw new BusinessValidationException(
                "Event type with code '" + dto.getCode() + "' already exists"
            );
        }
        
        if (eventTypeRepository.existsByDesignationFr(dto.getDesignationFr())) {
            throw new BusinessValidationException(
                "Event type with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.create(dto);
    }

    /**
     * Update an existing event type with dual uniqueness validation.
     *
     * @param id the event type ID
     * @param dto the event type DTO with updated data
     * @return the updated event type DTO
     * @throws BusinessValidationException if code or French designation already exists for another record
     */
    @Override
    @Transactional
    public EventTypeDTO update(Long id, EventTypeDTO dto) {
        log.info("Updating event type with ID: {}", id);
        
        if (eventTypeRepository.existsByCodeAndIdNot(dto.getCode(), id)) {
            throw new BusinessValidationException(
                "Event type with code '" + dto.getCode() + "' already exists"
            );
        }
        
        if (eventTypeRepository.existsByDesignationFrAndIdNot(dto.getDesignationFr(), id)) {
            throw new BusinessValidationException(
                "Event type with French designation '" + dto.getDesignationFr() + "' already exists"
            );
        }
        
        return super.update(id, dto);
    }

    /**
     * Get all event types without pagination, sorted by code.
     *
     * @return list of all event type DTOs
     */
    public List<EventTypeDTO> getAll() {
        log.debug("Getting all event types without pagination");
        return eventTypeRepository.findAllByOrderByCodeAsc().stream()
                .map(EventTypeDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Search event types across all fields (code and designations in all languages).
     *
     * @param searchTerm the search term
     * @param pageable pagination parameters
     * @return page of matching event type DTOs
     */
    public Page<EventTypeDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for event types with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(
            p -> eventTypeRepository.searchByAnyField(searchTerm.trim(), p), 
            pageable
        );
    }

    /**
     * Find event type by code.
     *
     * @param code the event type code
     * @return the event type DTO if found
     * @throws BusinessValidationException if not found
     */
    public EventTypeDTO findByCode(String code) {
        log.debug("Finding event type by code: {}", code);
        return eventTypeRepository.findByCode(code)
                .map(EventTypeDTO::fromEntity)
                .orElseThrow(() -> new BusinessValidationException(
                    "Event type with code '" + code + "' not found"
                ));
    }

    /**
     * Find event type by French designation.
     *
     * @param designationFr the French designation
     * @return the event type DTO if found
     * @throws BusinessValidationException if not found
     */
    public EventTypeDTO findByDesignationFr(String designationFr) {
        log.debug("Finding event type by French designation: {}", designationFr);
        return eventTypeRepository.findByDesignationFr(designationFr)
                .map(EventTypeDTO::fromEntity)
                .orElseThrow(() -> new BusinessValidationException(
                    "Event type with French designation '" + designationFr + "' not found"
                ));
    }
}

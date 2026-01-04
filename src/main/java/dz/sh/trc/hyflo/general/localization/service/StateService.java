/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StateService
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
import dz.sh.trc.hyflo.general.localization.dto.StateDTO;
import dz.sh.trc.hyflo.general.localization.model.State;
import dz.sh.trc.hyflo.general.localization.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StateService extends GenericService<State, StateDTO, Long> {

    private final StateRepository stateRepository;

    @Override
    protected JpaRepository<State, Long> getRepository() {
        return stateRepository;
    }

    @Override
    protected String getEntityName() {
        return "State";
    }

    @Override
    protected StateDTO toDTO(State entity) {
        return StateDTO.fromEntity(entity);
    }

    @Override
    protected State toEntity(StateDTO dto) {
        State entity = dto.toEntity();
        
        return entity;
    }

    @Override
    protected void updateEntityFromDTO(State entity, StateDTO dto) {
        dto.updateEntity(entity);
    }

    @Override
    @Transactional
    public StateDTO create(StateDTO dto) {
        log.info("Creating state: code={}, designationFr={}", 
                dto.getCode(), dto.getDesignationFr());
        return super.create(dto);
    }

    @Override
    @Transactional
    public StateDTO update(Long id, StateDTO dto) {
        log.info("Updating state with ID: {}", id);
        return super.update(id, dto);
    }

    public List<StateDTO> getAll() {
        log.debug("Getting all states without pagination");
        return stateRepository.findAll().stream()
                .map(StateDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Page<StateDTO> globalSearch(String searchTerm, Pageable pageable) {
        log.debug("Global search for states with term: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll(pageable);
        }
        
        return executeQuery(p -> stateRepository.searchByAnyField(searchTerm.trim(), p), pageable);
    }
}

/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ParameterService
 *	@CreatedOn	: 06-26-2023
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: System / Setting
 *
 **/

package dz.sh.trc.hyflo.system.setting.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.system.setting.dto.ParameterDTO;
import dz.sh.trc.hyflo.system.setting.model.Parameter;
import dz.sh.trc.hyflo.system.setting.repository.ParameterRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class ParameterService extends GenericService<Parameter, ParameterDTO, Long> {

	private final ParameterRepository parameterRepository;

    public ParameterService(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }
    
    // ========== GENERIC SERVICE ABSTRACT METHOD IMPLEMENTATIONS ==========

    @Override
    protected JpaRepository<Parameter, Long> getRepository() {
        return parameterRepository;
    }

    @Override
    protected String getEntityName() {
        return "Parameter";
    }

    @Override
    protected ParameterDTO toDTO(Parameter entity) {
        return ParameterDTO.fromEntity(entity);
    }
    
    @Override
    protected Parameter toEntity(ParameterDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Parameter entity, ParameterDTO dto) {
        dto.updateEntity(entity);
    }
    

    /*
     * Overridden save / update to add your own pre / post logic if needed.
     */

    @Override
    @Transactional
    public ParameterDTO create(ParameterDTO dto) {
        Parameter entity = assignParameterFields(new Parameter(), dto);
        Parameter saved = parameterRepository.save(entity);
        log.info("Parameter with key={} has been created", saved.getKey());
        return ParameterDTO.fromEntity(saved);
    }

    @Override
    @Transactional
    public ParameterDTO update(Long id, ParameterDTO dto) {
        Parameter entity = parameterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parameter not found with id: " + id));

        assignParameterFields(entity, dto);

        Parameter updated = parameterRepository.save(entity);

        log.info("Parameter with id={} (key={}) has been updated", updated.getId(), updated.getKey());
        return ParameterDTO.fromEntity(updated);
    }

    private Parameter assignParameterFields(Parameter entity, ParameterDTO dto) {
        entity.setKey(dto.getKey());
        entity.setValue(dto.getValue());
        entity.setType(dto.getType());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    /*
     * Custom business‑level methods:
     */

    public Optional<ParameterDTO> findByKey(String key) {
        return parameterRepository.findByKey(key)
                .map(ParameterDTO::fromEntity);
    }

    public Page<ParameterDTO> findByType(String type, Pageable pageable) {
        return parameterRepository.findByType(type, pageable)
                .map(ParameterDTO::fromEntity);
    }

    public Page<ParameterDTO> search(String query, Pageable pageable) {
        return parameterRepository.searchParameters(query, pageable)
                .map(ParameterDTO::fromEntity);
    }

    /*
     * Helper: safely get value by key.
     * Type‑aware usage is a responsibility of the calling code.
     */

    public Optional<String> getString(String key) {
        return findByKey(key)
                .filter(p -> "STRING".equals(p.getType()))
                .map(ParameterDTO::getValue);
    }

    public Optional<Long> getNumber(String key) {
        return findByKey(key)
                .filter(p -> "NUMBER".equals(p.getType()))
                .map(ParameterDTO::getValue)
                .map(value -> {
                    try {
                        return Long.parseLong(value.trim());
                    } catch (NumberFormatException e) {
                        log.warn("Invalid number format for key {}: {}", key, value);
                        return null;
                    }
                });
    }

    public Optional<String> getForeignKeyValue(String key) {
        return findByKey(key)
                .filter(p -> "FOREIGN_KEY".equals(p.getType()))
                .map(ParameterDTO::getValue);
    }

}

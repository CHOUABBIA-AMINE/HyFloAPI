/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AuthorityService
 *	@CreatedOn	: 11-18-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Service
 *	@Layer		: Service
 *	@Package	: System / Security
 *
 **/

package dz.mdn.iaas.system.security.service;

import dz.mdn.iaas.configuration.template.GenericService;
import dz.mdn.iaas.system.security.dto.AuthorityDTO;
import dz.mdn.iaas.system.security.model.Authority;
import dz.mdn.iaas.system.security.repository.AuthorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class AuthorityService extends GenericService<Authority, AuthorityDTO, Long> {

    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    // ========== GENERIC SERVICE ABSTRACT METHOD IMPLEMENTATIONS ==========

    @Override
    protected JpaRepository<Authority, Long> getRepository() {
        return authorityRepository;
    }

    @Override
    protected String getEntityName() {
        return "Authority";
    }

    @Override
    protected AuthorityDTO toDTO(Authority entity) {
        return AuthorityDTO.fromEntity(entity);
    }

    @Override
    protected Authority toEntity(AuthorityDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Authority entity, AuthorityDTO dto) {
        dto.updateEntity(entity);
    }

    // ========== OVERRIDDEN METHODS WITH VALIDATION ==========

    @Override
    @Transactional
    public AuthorityDTO create(AuthorityDTO dto) {
        log.info("Creating new Authority: {}", dto.getName());
        
        // Validate unique name
        if (authorityRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Authority with name '" + dto.getName() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public AuthorityDTO update(Long id, AuthorityDTO dto) {
        log.info("Updating Authority with ID: {}", id);
        
        // Validate unique name if changed
        Authority existingAuthority = getEntityById(id);
        if (dto.getName() != null && !dto.getName().equals(existingAuthority.getName())) {
            if (authorityRepository.existsByName(dto.getName())) {
                throw new IllegalArgumentException("Authority with name '" + dto.getName() + "' already exists");
            }
        }
        
        return super.update(id, dto);
    }

    // ========== CUSTOM BUSINESS METHODS ==========

    /**
     * Find authority by name
     */
    @Transactional(readOnly = true)
    public AuthorityDTO findByName(String name) {
        log.debug("Finding authority by name: {}", name);
        return authorityRepository.findByName(name)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Authority not found with name: " + name));
    }

    /**
     * Find authorities by type
     */
    @Transactional(readOnly = true)
    public List<AuthorityDTO> findByType(String type) {
        log.debug("Finding authorities by type: {}", type);
        return authorityRepository.findByType(type)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Check if authority exists by name
     */
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return authorityRepository.existsByName(name);
    }
}

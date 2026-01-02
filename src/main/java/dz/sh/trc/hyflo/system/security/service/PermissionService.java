/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: PermissionService
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.service;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.system.security.dto.PermissionDTO;
import dz.sh.trc.hyflo.system.security.model.Permission;
import dz.sh.trc.hyflo.system.security.repository.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class PermissionService extends GenericService<Permission, PermissionDTO, Long> {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    // ========== GENERIC SERVICE ABSTRACT METHOD IMPLEMENTATIONS ==========

    @Override
    protected JpaRepository<Permission, Long> getRepository() {
        return permissionRepository;
    }

    @Override
    protected String getEntityName() {
        return "Permission";
    }

    @Override
    protected PermissionDTO toDTO(Permission entity) {
        return PermissionDTO.fromEntity(entity);
    }

    @Override
    protected Permission toEntity(PermissionDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Permission entity, PermissionDTO dto) {
        dto.updateEntity(entity);
    }

    // ========== OVERRIDDEN METHODS WITH VALIDATION ==========

    @Override
    @Transactional
    public PermissionDTO create(PermissionDTO dto) {
        log.info("Creating new Permission: {}", dto.getName());
        
        // Validate unique name
        if (permissionRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Permission with name '" + dto.getName() + "' already exists");
        }
        
        return super.create(dto);
    }

    @Override
    @Transactional
    public PermissionDTO update(Long id, PermissionDTO dto) {
        log.info("Updating Permission with ID: {}", id);
        
        // Validate unique name if changed
        Permission existingPermission = getEntityById(id);
        if (dto.getName() != null && !dto.getName().equals(existingPermission.getName())) {
            if (permissionRepository.existsByName(dto.getName())) {
                throw new IllegalArgumentException("Permission with name '" + dto.getName() + "' already exists");
            }
        }
        
        return super.update(id, dto);
    }

    // ========== CUSTOM BUSINESS METHODS ==========

    /**
     * Find permission by name
     */
    @Transactional(readOnly = true)
    public PermissionDTO findByName(String name) {
        log.debug("Finding permission by name: {}", name);
        return permissionRepository.findByName(name)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Permission not found with name: " + name));
    }

    /**
     * Find permissions by resource
     */
    @Transactional(readOnly = true)
    public List<PermissionDTO> findByResource(String resource) {
        log.debug("Finding permissions by resource: {}", resource);
        return permissionRepository.findByResource(resource)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find permissions by action
     */
    @Transactional(readOnly = true)
    public List<PermissionDTO> findByAction(String action) {
        log.debug("Finding permissions by action: {}", action);
        return permissionRepository.findByAction(action)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find permissions by resource and action
     */
    @Transactional(readOnly = true)
    public List<PermissionDTO> findByResourceAndAction(String resource, String action) {
        log.debug("Finding permissions by resource: {} and action: {}", resource, action);
        return permissionRepository.findByResourceAndAction(resource, action)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Check if permission exists by name
     */
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return permissionRepository.existsByName(name);
    }
}

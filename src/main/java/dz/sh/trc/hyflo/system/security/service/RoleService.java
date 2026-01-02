/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: RoleService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.service;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.system.security.dto.RoleDTO;
import dz.sh.trc.hyflo.system.security.model.Permission;
import dz.sh.trc.hyflo.system.security.model.Role;
import dz.sh.trc.hyflo.system.security.repository.PermissionRepository;
import dz.sh.trc.hyflo.system.security.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class RoleService extends GenericService<Role, RoleDTO, Long> {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    // ========== GENERIC SERVICE ABSTRACT METHOD IMPLEMENTATIONS ==========

    @Override
    protected JpaRepository<Role, Long> getRepository() {
        return roleRepository;
    }

    @Override
    protected String getEntityName() {
        return "Role";
    }

    @Override
    protected RoleDTO toDTO(Role entity) {
        return RoleDTO.fromEntity(entity);
    }

    @Override
    protected Role toEntity(RoleDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Role entity, RoleDTO dto) {
        dto.updateEntity(entity);
    }

    // ========== OVERRIDDEN METHODS WITH CUSTOM LOGIC ==========

    @Override
    @Transactional
    public RoleDTO create(RoleDTO dto) {
        log.info("Creating new Role: {}", dto.getName());
        
        // Validate unique name
        if (roleRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Role with name '" + dto.getName() + "' already exists");
        }
        
        // Create role and resolve permissions
        Role role = toEntity(dto);
        resolvePermissions(role, dto);
        
        Role savedRole = roleRepository.save(role);
        return toDTO(savedRole);
    }

    @Override
    @Transactional
    public RoleDTO update(Long id, RoleDTO dto) {
        log.info("Updating Role with ID: {}", id);
        
        Role existingRole = getEntityById(id);
        
        // Validate unique name if changed
        if (dto.getName() != null && !dto.getName().equals(existingRole.getName())) {
            if (roleRepository.existsByName(dto.getName())) {
                throw new IllegalArgumentException("Role with name '" + dto.getName() + "' already exists");
            }
        }
        
        // Update basic fields
        updateEntityFromDTO(existingRole, dto);
        
        // Resolve and update permissions
        resolvePermissions(existingRole, dto);
        
        Role savedRole = roleRepository.save(existingRole);
        return toDTO(savedRole);
    }

    // ========== PERMISSION MANAGEMENT ==========

    /**
     * Assign permission to role
     */
    @Transactional
    public RoleDTO assignPermission(Long roleId, Long permissionId) {
        log.info("Assigning permission {} to role {}", permissionId, roleId);
        
        Role role = getEntityById(roleId);
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with ID: " + permissionId));
        
        if (role.getPermissions().contains(permission)) {
            log.warn("Role {} already has permission {}", roleId, permissionId);
        } else {
            role.getPermissions().add(permission);
            roleRepository.save(role);
            log.info("Permission {} assigned to role {} successfully", permissionId, roleId);
        }
        
        return toDTO(role);
    }

    /**
     * Remove permission from role
     */
    @Transactional
    public RoleDTO removePermission(Long roleId, Long permissionId) {
        log.info("Removing permission {} from role {}", permissionId, roleId);
        
        Role role = getEntityById(roleId);
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with ID: " + permissionId));
        
        if (role.getPermissions().remove(permission)) {
            roleRepository.save(role);
            log.info("Permission {} removed from role {} successfully", permissionId, roleId);
        } else {
            log.warn("Role {} does not have permission {}", roleId, permissionId);
        }
        
        return toDTO(role);
    }

    // ========== CUSTOM BUSINESS METHODS ==========

    /**
     * Find role by name
     */
    @Transactional(readOnly = true)
    public RoleDTO findByName(String name) {
        log.debug("Finding role by name: {}", name);
        return roleRepository.findByName(name)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + name));
    }

    /**
     * Find roles by permission
     */
    @Transactional(readOnly = true)
    public List<RoleDTO> findByPermission(Long permissionId) {
        log.debug("Finding roles by permission: {}", permissionId);
        
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with ID: " + permissionId));
        
        return roleRepository.findAll().stream()
                .filter(role -> role.getPermissions().contains(permission))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Check if role exists by name
     */
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    // ========== HELPER METHODS ==========

    /**
     * Resolve permission IDs from DTO to actual entities
     */
    private void resolvePermissions(Role role, RoleDTO dto) {
        if (dto.getPermissions() != null && !dto.getPermissions().isEmpty()) {
            role.getPermissions().clear();
            dto.getPermissions().forEach(permissionDTO -> {
                if (permissionDTO.getId() != null) {
                    Permission permission = permissionRepository.findById(permissionDTO.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Permission not found with ID: " + permissionDTO.getId()));
                    role.getPermissions().add(permission);
                }
            });
        }
    }
}

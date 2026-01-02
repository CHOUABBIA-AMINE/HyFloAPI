/**
 *	
 *	@author		: MEDJERAB Abir
 *
 *	@Name		: GroupService
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
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.system.security.dto.GroupDTO;
import dz.sh.trc.hyflo.system.security.model.Group;
import dz.sh.trc.hyflo.system.security.model.Role;
import dz.sh.trc.hyflo.system.security.repository.GroupRepository;
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
public class GroupService extends GenericService<Group, GroupDTO, Long> {

    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;

    public GroupService(GroupRepository groupRepository, RoleRepository roleRepository) {
        this.groupRepository = groupRepository;
        this.roleRepository = roleRepository;
    }

    // ========== GENERIC SERVICE ABSTRACT METHOD IMPLEMENTATIONS ==========

    @Override
    protected JpaRepository<Group, Long> getRepository() {
        return groupRepository;
    }

    @Override
    protected String getEntityName() {
        return "Group";
    }

    @Override
    protected GroupDTO toDTO(Group entity) {
        return GroupDTO.fromEntity(entity);
    }

    @Override
    protected Group toEntity(GroupDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(Group entity, GroupDTO dto) {
        dto.updateEntity(entity);
    }

    // ========== OVERRIDDEN METHODS WITH CUSTOM LOGIC ==========

    @Override
    @Transactional
    public GroupDTO create(GroupDTO dto) {
        log.info("Creating new Group: {}", dto.getName());
        
        // Validate unique name
        if (groupRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Group with name '" + dto.getName() + "' already exists");
        }
        
        // Create group and resolve roles
        Group group = toEntity(dto);
        resolveRoles(group, dto);
        
        Group savedGroup = groupRepository.save(group);
        return toDTO(savedGroup);
    }

    @Override
    @Transactional
    public GroupDTO update(Long id, GroupDTO dto) {
        log.info("Updating Group with ID: {}", id);
        
        Group existingGroup = getEntityById(id);
        
        // Validate unique name if changed
        if (dto.getName() != null && !dto.getName().equals(existingGroup.getName())) {
            if (groupRepository.existsByName(dto.getName())) {
                throw new IllegalArgumentException("Group with name '" + dto.getName() + "' already exists");
            }
        }
        
        // Update basic fields
        updateEntityFromDTO(existingGroup, dto);
        
        // Resolve and update roles
        resolveRoles(existingGroup, dto);
        
        Group savedGroup = groupRepository.save(existingGroup);
        return toDTO(savedGroup);
    }

    // ========== ROLE MANAGEMENT ==========

    /**
     * Assign role to group
     */
    @Transactional
    public GroupDTO assignRole(Long groupId, Long roleId) {
        log.info("Assigning role {} to group {}", roleId, groupId);
        
        Group group = getEntityById(groupId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));
        
        if (group.getRoles().contains(role)) {
            log.warn("Group {} already has role {}", groupId, roleId);
        } else {
            group.getRoles().add(role);
            groupRepository.save(group);
            log.info("Role {} assigned to group {} successfully", roleId, groupId);
        }
        
        return toDTO(group);
    }

    /**
     * Remove role from group
     */
    @Transactional
    public GroupDTO removeRole(Long groupId, Long roleId) {
        log.info("Removing role {} from group {}", roleId, groupId);
        
        Group group = getEntityById(groupId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));
        
        if (group.getRoles().remove(role)) {
            groupRepository.save(group);
            log.info("Role {} removed from group {} successfully", roleId, groupId);
        } else {
            log.warn("Group {} does not have role {}", groupId, roleId);
        }
        
        return toDTO(group);
    }

    // ========== CUSTOM BUSINESS METHODS ==========

    /**
     * Find group by name
     */
    @Transactional(readOnly = true)
    public GroupDTO findByName(String name) {
        log.debug("Finding group by name: {}", name);
        return groupRepository.findByName(name)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with name: " + name));
    }

    /**
     * Find groups by role
     */
    @Transactional(readOnly = true)
    public List<GroupDTO> findByRole(Long roleId) {
        log.debug("Finding groups by role: {}", roleId);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));
        
        return groupRepository.findAll().stream()
                .filter(group -> group.getRoles().contains(role))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Check if group exists by name
     */
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return groupRepository.existsByName(name);
    }

    // ========== HELPER METHODS ==========

    /**
     * Resolve role IDs from DTO to actual entities
     */
    private void resolveRoles(Group group, GroupDTO dto) {
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            group.getRoles().clear();
            dto.getRoles().forEach(roleDTO -> {
                if (roleDTO.getId() != null) {
                    Role role = roleRepository.findById(roleDTO.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleDTO.getId()));
                    group.getRoles().add(role);
                }
            });
        }
    }
}

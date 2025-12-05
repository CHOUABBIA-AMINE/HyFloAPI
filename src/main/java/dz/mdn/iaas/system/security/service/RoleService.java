/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RoleService
 *	@CreatedOn	: 11-18-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: System / Security
 *
 **/

package dz.mdn.iaas.system.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.mdn.iaas.system.security.dto.RoleDTO;
import dz.mdn.iaas.system.security.model.Permission;
import dz.mdn.iaas.system.security.model.Role;
import dz.mdn.iaas.system.security.repository.PermissionRepository;
import dz.mdn.iaas.system.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public RoleDTO findById(Long id) {
        return roleRepository.findById(id)
            .map(this::convertToDTO)
            .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Transactional
    public RoleDTO create(RoleDTO dto) {
        Role role = new Role();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        return convertToDTO(roleRepository.save(role));
    }

    @Transactional
    public RoleDTO update(Long id, RoleDTO dto) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role not found"));
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        return convertToDTO(roleRepository.save(role));
    }

    @Transactional
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    @Transactional
    public RoleDTO assignPermission(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
            .orElseThrow(() -> new RuntimeException("Role not found"));
        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new RuntimeException("Permission not found"));

        role.getPermissions().add(permission);
        return convertToDTO(roleRepository.save(role));
    }

    private RoleDTO convertToDTO(Role role) {
        return RoleDTO.builder()
            .id(role.getId())
            .name(role.getName())
            .description(role.getDescription())
            .build();
    }
}

/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PermissionService
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

import dz.mdn.iaas.system.security.dto.PermissionDTO;
import dz.mdn.iaas.system.security.model.Permission;
import dz.mdn.iaas.system.security.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public List<PermissionDTO> findAll() {
        return permissionRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public PermissionDTO findById(Long id) {
        return permissionRepository.findById(id)
            .map(this::convertToDTO)
            .orElseThrow(() -> new RuntimeException("Permission not found"));
    }

    @Transactional
    public PermissionDTO create(PermissionDTO dto) {
        Permission permission = new Permission();
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());
        return convertToDTO(permissionRepository.save(permission));
    }

    @Transactional
    public PermissionDTO update(Long id, PermissionDTO dto) {
        Permission permission = permissionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Permission not found"));
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());
        return convertToDTO(permissionRepository.save(permission));
    }

    @Transactional
    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }

    private PermissionDTO convertToDTO(Permission permission) {
        return PermissionDTO.builder()
            .id(permission.getId())
            .name(permission.getName())
            .description(permission.getDescription())
            .build();
    }
}

/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: UserService
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Service
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.configuration.template.GenericService;
import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.system.security.dto.UserDTO;
import dz.sh.trc.hyflo.system.security.model.Group;
import dz.sh.trc.hyflo.system.security.model.Role;
import dz.sh.trc.hyflo.system.security.model.User;
import dz.sh.trc.hyflo.system.security.repository.GroupRepository;
import dz.sh.trc.hyflo.system.security.repository.RoleRepository;
import dz.sh.trc.hyflo.system.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class UserService extends GenericService<User, UserDTO, Long> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            GroupRepository groupRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.groupRepository = groupRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ========== GENERIC SERVICE ABSTRACT METHOD IMPLEMENTATIONS ==========

    @Override
    protected JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    protected String getEntityName() {
        return "User";
    }

    @Override
    protected UserDTO toDTO(User entity) {
        return UserDTO.fromEntity(entity);
    }

    @Override
    protected User toEntity(UserDTO dto) {
        return dto.toEntity();
    }

    @Override
    protected void updateEntityFromDTO(User entity, UserDTO dto) {
        dto.updateEntity(entity);
    }

    // ========== OVERRIDDEN METHODS WITH CUSTOM LOGIC ==========

    /**
     * Override create to hash password before saving
     */
    @Override
    @Transactional
    public UserDTO create(UserDTO dto) {
        log.info("Creating new User with username: {}", dto.getUsername());
        
        // Validate unique username
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + dto.getUsername());
        }
        
        // Validate unique email
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
        }
        
        // Hash password before creating entity
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else {
            throw new IllegalArgumentException("Password is required for new user");
        }
        
        // Resolve role and group IDs to actual entities
        User user = toEntity(dto);
        resolveRolesAndGroups(user, dto);
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        
        return toDTO(savedUser);
    }

    /**
     * Override update to handle password hashing and unique constraints
     */
    @Override
    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        log.info("Updating User with ID: {}", id);
        
        User existingUser = getEntityById(id);
        
        // Validate unique username if changed
        if (dto.getUsername() != null && !dto.getUsername().equals(existingUser.getUsername())) {
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new IllegalArgumentException("Username already exists: " + dto.getUsername());
            }
        }
        
        // Validate unique email if changed
        if (dto.getEmail() != null && !dto.getEmail().equals(existingUser.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
            }
        }
        
        // Hash password if provided
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else {
            // Don't update password if not provided
            dto.setPassword(null);
        }
        
        // Update basic fields
        updateEntityFromDTO(existingUser, dto);
        
        // Resolve and update roles and groups
        resolveRolesAndGroups(existingUser, dto);
        
        User savedUser = userRepository.save(existingUser);
        log.info("User updated successfully with ID: {}", id);
        
        return toDTO(savedUser);
    }

    // ========== CUSTOM USER OPERATIONS ==========

    /**
     * Find user by username
     */
    @Transactional(readOnly = true)
    public UserDTO findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        
        return userRepository.findByUsername(username)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    /**
     * Find user by email
     */
    @Transactional(readOnly = true)
    public UserDTO findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        
        return userRepository.findByEmail(email)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    /**
     * Check if username exists
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check if email exists
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Reset user password
     */
    @Transactional
    public void resetPassword(String username, String newPassword) {
        log.info("Resetting password for user: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }
        
        // Hash and set new password
        user.setPassword(passwordEncoder.encode(newPassword));
        
        // Optional: Reset account status
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        
        userRepository.save(user);
        log.info("Password reset successfully for user: {}", username);
    }

    /**
     * Assign role to user
     */
    @Transactional
    public UserDTO assignRole(Long userId, Long roleId) {
        log.info("Assigning role {} to user {}", roleId, userId);
        
        User user = getEntityById(userId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));
        
        if (user.getRoles().contains(role)) {
            log.warn("User {} already has role {}", userId, roleId);
        } else {
            user.getRoles().add(role);
            userRepository.save(user);
            log.info("Role {} assigned to user {} successfully", roleId, userId);
        }
        
        return toDTO(user);
    }

    /**
     * Remove role from user
     */
    @Transactional
    public UserDTO removeRole(Long userId, Long roleId) {
        log.info("Removing role {} from user {}", roleId, userId);
        
        User user = getEntityById(userId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));
        
        if (user.getRoles().remove(role)) {
            userRepository.save(user);
            log.info("Role {} removed from user {} successfully", roleId, userId);
        } else {
            log.warn("User {} does not have role {}", userId, roleId);
        }
        
        return toDTO(user);
    }

    /**
     * Assign group to user
     */
    @Transactional
    public UserDTO assignGroup(Long userId, Long groupId) {
        log.info("Assigning group {} to user {}", groupId, userId);
        
        User user = getEntityById(userId);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with ID: " + groupId));
        
        if (user.getGroups().contains(group)) {
            log.warn("User {} already in group {}", userId, groupId);
        } else {
            user.getGroups().add(group);
            userRepository.save(user);
            log.info("Group {} assigned to user {} successfully", groupId, userId);
        }
        
        return toDTO(user);
    }

    /**
     * Remove group from user
     */
    @Transactional
    public UserDTO removeGroup(Long userId, Long groupId) {
        log.info("Removing group {} from user {}", groupId, userId);
        
        User user = getEntityById(userId);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with ID: " + groupId));
        
        if (user.getGroups().remove(group)) {
            userRepository.save(user);
            log.info("Group {} removed from user {} successfully", groupId, userId);
        } else {
            log.warn("User {} is not in group {}", userId, groupId);
        }
        
        return toDTO(user);
    }

    /**
     * Enable user account
     */
    @Transactional
    public UserDTO enableUser(Long userId) {
        log.info("Enabling user: {}", userId);
        
        User user = getEntityById(userId);
        user.setEnabled(true);
        userRepository.save(user);
        
        log.info("User {} enabled successfully", userId);
        return toDTO(user);
    }

    /**
     * Disable user account
     */
    @Transactional
    public UserDTO disableUser(Long userId) {
        log.info("Disabling user: {}", userId);
        
        User user = getEntityById(userId);
        user.setEnabled(false);
        userRepository.save(user);
        
        log.info("User {} disabled successfully", userId);
        return toDTO(user);
    }

    /**
     * Lock user account
     */
    @Transactional
    public UserDTO lockUser(Long userId) {
        log.info("Locking user: {}", userId);
        
        User user = getEntityById(userId);
        user.setAccountNonLocked(false);
        userRepository.save(user);
        
        log.info("User {} locked successfully", userId);
        return toDTO(user);
    }

    /**
     * Unlock user account
     */
    @Transactional
    public UserDTO unlockUser(Long userId) {
        log.info("Unlocking user: {}", userId);
        
        User user = getEntityById(userId);
        user.setAccountNonLocked(true);
        userRepository.save(user);
        
        log.info("User {} unlocked successfully", userId);
        return toDTO(user);
    }

    /**
     * Find users by role
     */
    @Transactional(readOnly = true)
    public List<UserDTO> findByRole(Long roleId) {
        log.debug("Finding users by role: {}", roleId);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));
        
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(role))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find users by group
     */
    @Transactional(readOnly = true)
    public List<UserDTO> findByGroup(Long groupId) {
        log.debug("Finding users by group: {}", groupId);
        
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with ID: " + groupId));
        
        return userRepository.findAll().stream()
                .filter(user -> user.getGroups().contains(group))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ========== HELPER METHODS ==========

    /**
     * Resolve role and group IDs from DTO to actual entities
     */
    private void resolveRolesAndGroups(User user, UserDTO dto) {
        // Resolve roles
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            user.getRoles().clear();
            dto.getRoles().forEach(roleDTO -> {
                if (roleDTO.getId() != null) {
                    Role role = roleRepository.findById(roleDTO.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleDTO.getId()));
                    user.getRoles().add(role);
                }
            });
        }
        
        // Resolve groups
        if (dto.getGroups() != null && !dto.getGroups().isEmpty()) {
            user.getGroups().clear();
            dto.getGroups().forEach(groupDTO -> {
                if (groupDTO.getId() != null) {
                    Group group = groupRepository.findById(groupDTO.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Group not found with ID: " + groupDTO.getId()));
                    user.getGroups().add(group);
                }
            });
        }
    }
}

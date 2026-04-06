/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : UserCommandServiceImpl
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : Service / Impl
 *  @Package    : System / Security
 *
 *  Phase 3 — Commit 24
 *
 **/

package dz.sh.trc.hyflo.domain.security.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.domain.security.dto.command.UserCommandDTO;
import dz.sh.trc.hyflo.domain.security.dto.query.UserReadDTO;
import dz.sh.trc.hyflo.domain.security.mapper.UserMapper;
import dz.sh.trc.hyflo.domain.security.model.User;
import dz.sh.trc.hyflo.domain.security.repository.UserRepository;
import dz.sh.trc.hyflo.domain.security.service.UserCommandService;
import dz.sh.trc.hyflo.exception.business.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Command implementation for User write operations.
 *
 * Uses UserMapper from Phase 2.
 * Returns UserReadDTO — no raw entity exposure.
 *
 * Phase 3 — Commit 24
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;

    @Override
    public UserReadDTO createUser(UserCommandDTO command) {
        log.info("Creating user: {}", command.getUsername());
        User entity = UserMapper.toEntity(command);
        return UserMapper.toReadDTO(userRepository.save(entity));
    }

    @Override
    public UserReadDTO updateUser(Long id, UserCommandDTO command) {
        log.info("Updating user ID: {}", id);
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        UserMapper.updateEntity(command, existing);
        return UserMapper.toReadDTO(userRepository.save(existing));
    }

    @Override
    public void deactivateUser(Long id) {
        log.info("Deactivating user ID: {}", id);
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        existing.setEnabled(false);
        userRepository.save(existing);
    }

    @Override
    public void changePassword(Long id, String newEncodedPassword) {
        log.info("Changing password for user ID: {}", id);
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        existing.setPassword(newEncodedPassword);
        userRepository.save(existing);
    }
}

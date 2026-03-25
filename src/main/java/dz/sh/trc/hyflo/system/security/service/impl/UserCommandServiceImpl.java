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

package dz.sh.trc.hyflo.system.security.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.system.security.dto.command.UserCommandDto;
import dz.sh.trc.hyflo.system.security.dto.query.UserReadDto;
import dz.sh.trc.hyflo.system.security.mapper.UserMapper;
import dz.sh.trc.hyflo.system.security.model.User;
import dz.sh.trc.hyflo.system.security.repository.UserRepository;
import dz.sh.trc.hyflo.system.security.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Command implementation for User write operations.
 *
 * Uses UserMapper from Phase 2.
 * Returns UserReadDto — no raw entity exposure.
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
    public UserReadDto createUser(UserCommandDto command) {
        log.info("Creating user: {}", command.getUsername());
        User entity = UserMapper.toEntity(command);
        return UserMapper.toReadDto(userRepository.save(entity));
    }

    @Override
    public UserReadDto updateUser(Long id, UserCommandDto command) {
        log.info("Updating user ID: {}", id);
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        UserMapper.updateEntity(command, existing);
        return UserMapper.toReadDto(userRepository.save(existing));
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

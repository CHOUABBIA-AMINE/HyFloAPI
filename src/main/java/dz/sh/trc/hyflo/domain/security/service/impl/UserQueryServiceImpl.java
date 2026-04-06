/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : UserQueryServiceImpl
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

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dz.sh.trc.hyflo.exception.ResourceNotFoundException;
import dz.sh.trc.hyflo.domain.security.dto.query.UserReadDTO;
import dz.sh.trc.hyflo.domain.security.mapper.UserMapper;
import dz.sh.trc.hyflo.domain.security.repository.UserRepository;
import dz.sh.trc.hyflo.domain.security.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Query implementation for User read operations.
 *
 * Uses UserMapper from Phase 2.
 * Returns UserReadDTO — no raw entity exposure.
 *
 * Phase 3 — Commit 24
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    @Override
    public UserReadDTO getById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    @Override
    public UserReadDTO getByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserMapper::toReadDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found by username: " + username));
    }

    @Override
    public Page<UserReadDTO> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper::toReadDTO);
    }

    @Override
    public List<UserReadDTO> getByRole(Long roleId) {
        return userRepository.findByRoleId(roleId)
                .stream()
                .map(UserMapper::toReadDTO)
                .collect(Collectors.toList());
    }
}

/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : UserQueryService
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : System / Security
 *
 *  Phase 3 — Commit 24
 *
 **/

package dz.sh.trc.hyflo.system.security.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.system.security.dto.query.UserReadDto;

/**
 * Query contract for User read operations.
 *
 * All methods return UserReadDto — never raw entities.
 *
 * Phase 3 — Commit 24
 */
public interface UserQueryService {

    UserReadDto getById(Long id);

    UserReadDto getByUsername(String username);

    Page<UserReadDto> getAll(Pageable pageable);

    List<UserReadDto> getByRole(Long roleId);
}

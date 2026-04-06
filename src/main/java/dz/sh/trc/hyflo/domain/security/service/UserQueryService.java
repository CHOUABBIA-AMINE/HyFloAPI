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

package dz.sh.trc.hyflo.domain.security.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.domain.security.dto.query.UserReadDTO;

/**
 * Query contract for User read operations.
 *
 * All methods return UserReadDTO — never raw entities.
 *
 * Phase 3 — Commit 24
 */
public interface UserQueryService {

    UserReadDTO getById(Long id);

    UserReadDTO getByUsername(String username);

    Page<UserReadDTO> getAll(Pageable pageable);

    List<UserReadDTO> getByRole(Long roleId);
}

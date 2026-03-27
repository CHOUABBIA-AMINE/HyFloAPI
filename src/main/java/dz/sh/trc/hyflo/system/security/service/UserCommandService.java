/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : UserCommandService
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

import dz.sh.trc.hyflo.system.security.dto.command.UserCommandDTO;
import dz.sh.trc.hyflo.system.security.dto.query.UserReadDTO;

/**
 * Command contract for User write operations.
 *
 * All methods return UserReadDTO — never raw entities.
 * Password encoding handled by implementation — callers pass raw password.
 *
 * Phase 3 — Commit 24
 */
public interface UserCommandService {

    UserReadDTO createUser(UserCommandDTO command);

    UserReadDTO updateUser(Long id, UserCommandDTO command);

    /**
     * Deactivate a user account (soft disable).
     * Deactivated accounts cannot authenticate.
     */
    void deactivateUser(Long id);

    /**
     * Change a user's password.
     * @param id                User ID
     * @param newEncodedPassword Pre-encoded password (caller must use PasswordEncoder)
     */
    void changePassword(Long id, String newEncodedPassword);
}

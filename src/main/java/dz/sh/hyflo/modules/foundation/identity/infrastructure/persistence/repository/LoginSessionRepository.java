/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : LoginSessionRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for LoginSession.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import dz.sh.hyflo.modules.foundation.identity.domain.model.LoginSession;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.Repository;

public interface LoginSessionRepository extends Repository<LoginSession, Long> {

    LoginSession save(LoginSession entity);

    Optional<LoginSession> findById(Long id);

    @Query("select s from LoginSession s where s.user.id = :userId and s.id > :afterId "
            + "and s.revokedAt is null and s.startedAt <= :now and s.expiresAt > :now order by s.id")
    List<LoginSession> findOwnActivePage(@Param("userId") long userId, @Param("afterId") long afterId,
            @Param("now") Instant now, Pageable page);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from LoginSession s where s.user.id = :userId and s.id = :sessionId")
    Optional<LoginSession> findOwnedForUpdate(@Param("userId") long userId, @Param("sessionId") long sessionId);

    @Query("select s.user.id from LoginSession s where s.id = :id")
    Optional<Long> findUserIdBySessionId(@Param("id") long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from LoginSession s where s.id = :id")
    Optional<LoginSession> findForUpdate(@Param("id") long id);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from LoginSession s where s.user.id = :userId and s.revokedAt is null order by s.id")
    java.util.List<LoginSession> findUnrevokedForUpdate(@Param("userId") long userId);

}

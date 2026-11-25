/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : PlatformRoleGrantRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : PlatformRoleGrantRepository for scoped platform administration.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import java.util.List;
import dz.sh.hyflo.modules.foundation.identity.domain.model.PlatformRoleGrant;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface PlatformRoleGrantRepository extends Repository<PlatformRoleGrant, Long> {
    PlatformRoleGrant save(PlatformRoleGrant grant);
    boolean existsByUserId(long userId);

    @Query("select g from PlatformRoleGrant g join fetch g.role join fetch g.user where g.user.id = :userId order by g.id")
    List<PlatformRoleGrant> findCandidates(@Param("userId") long userId, org.springframework.data.domain.Pageable page);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select g from PlatformRoleGrant g join fetch g.role join fetch g.user where g.user.id = :userId order by g.id")
    List<PlatformRoleGrant> findCandidatesForWrite(@Param("userId") long userId, org.springframework.data.domain.Pageable page);
    @org.springframework.data.jpa.repository.Query("select count(g) from PlatformRoleGrant g where g.role.id = :roleId "
            + "and (g.validTo is null or g.validTo > :now) and (g.revokedAt is null or g.revokedAt > :now)")
    long countNonEndedForRole(@org.springframework.data.repository.query.Param("roleId") long roleId,
            @org.springframework.data.repository.query.Param("now") java.time.Instant now);

}

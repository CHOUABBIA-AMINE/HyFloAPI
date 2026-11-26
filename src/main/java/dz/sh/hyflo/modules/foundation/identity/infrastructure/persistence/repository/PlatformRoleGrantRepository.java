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

    @Query("select count(g) from PlatformRoleGrant g where g.role.id = :roleId "
            + "and (g.validTo is null or g.validTo > :now) and (g.revokedAt is null or g.revokedAt > :now) "
            + "and (g.validFrom > :now or g.validTo is not null or g.revokedAt is not null)")
    long countNonEndedTemporalForRole(@Param("roleId") long roleId, @Param("now") java.time.Instant now);

    @Query("select case when count(u) > 0 then true else false end from User u where u.id = :userId and u.username <> 'hyflo-bootstrap'")
    boolean existsEligibleUser(@Param("userId") long userId);

    long countByUserId(long userId);

    @Query("select g from PlatformRoleGrant g join fetch g.role where g.user.id = :userId and g.id > :afterId order by g.id")
    List<PlatformRoleGrant> findHistory(@Param("userId") long userId, @Param("afterId") long afterId,
            org.springframework.data.domain.Pageable page);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select g from PlatformRoleGrant g where g.id = :grantId and g.user.id = :userId")
    java.util.Optional<PlatformRoleGrant> findSelectedForUpdate(@Param("grantId") long grantId, @Param("userId") long userId);

    @Query("select count(g) from PlatformRoleGrant g where g.user.id = :userId and g.role.id = :roleId "
            + "and (g.validTo is null or g.validTo > :now) and (g.revokedAt is null or g.revokedAt > :now)")
    long countNonEndedForUserRole(@Param("userId") long userId, @Param("roleId") long roleId, @Param("now") java.time.Instant now);

    @Query("select distinct g.user.id from PlatformRoleGrant g where g.validTo is null and g.revokedAt is null "
            + "and g.validFrom <= :now and g.role.enabled = true and g.role.platformRole = true")
    List<Long> findDurableCandidateUsers(@Param("now") java.time.Instant now);
}

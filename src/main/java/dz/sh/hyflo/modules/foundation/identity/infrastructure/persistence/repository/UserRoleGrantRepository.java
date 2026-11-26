/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserRoleGrantRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for UserRoleGrant.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import java.util.List;

import dz.sh.hyflo.modules.foundation.identity.domain.model.UserRoleGrant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface UserRoleGrantRepository extends Repository<UserRoleGrant, Long> {

    UserRoleGrant save(UserRoleGrant entity);

    @Query("select g from UserRoleGrant g join fetch g.role join fetch g.user "
            + "where g.user.id = :userId and g.authorizationScope.organizationUnitId = :companyId")
    List<UserRoleGrant> findCompanyCandidates(@Param("userId") long userId, @Param("companyId") long companyId);
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_READ)
    @Query("select g from UserRoleGrant g join fetch g.role join fetch g.user "
            + "where g.user.id = :userId and g.authorizationScope.organizationUnitId = :companyId")
    List<UserRoleGrant> findCompanyCandidatesForWrite(@Param("userId") long userId, @Param("companyId") long companyId);

    @Query("select g from UserRoleGrant g join fetch g.role join fetch g.user "
            + "where g.user.id = :userId and g.authorizationScope.organizationUnitId = :companyId "
            + "and g.authorizationScope.scopeType = 'GLOBAL' and g.authorizationScope.scopeReferenceId is null "
            + "and g.id > :afterId order by g.id")
    List<UserRoleGrant> findGlobalPage(@Param("userId") long userId, @Param("companyId") long companyId,
            @Param("afterId") long afterId, org.springframework.data.domain.Pageable page);

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @Query("select g from UserRoleGrant g where g.id = :grantId and g.user.id = :userId "
            + "and g.authorizationScope.organizationUnitId = :companyId")
    java.util.Optional<UserRoleGrant> findSelectedForUpdate(@Param("grantId") long grantId,
            @Param("userId") long userId, @Param("companyId") long companyId);

    @org.springframework.data.jpa.repository.Query("select count(g) from UserRoleGrant g where g.role.id = :roleId "
            + "and (g.validTo is null or g.validTo > :now) and (g.revokedAt is null or g.revokedAt > :now) and g.user.id = :userId")
    long countNonEndedForUser(@org.springframework.data.repository.query.Param("roleId") long roleId,
            @org.springframework.data.repository.query.Param("now") java.time.Instant now, @org.springframework.data.repository.query.Param("userId") long userId);

    @org.springframework.data.jpa.repository.Query("select count(g) from UserRoleGrant g where g.role.id = :roleId "
            + "and (g.validTo is null or g.validTo > :now) and (g.revokedAt is null or g.revokedAt > :now) and g.user.owningOrganizationUnitId <> g.authorizationScope.organizationUnitId")
    long countNonEndedForeign(@org.springframework.data.repository.query.Param("roleId") long roleId,
            @org.springframework.data.repository.query.Param("now") java.time.Instant now);


    @Query("select count(g) from UserRoleGrant g where g.role.id = :roleId "
            + "and (g.validTo is null or g.validTo > :now) and (g.revokedAt is null or g.revokedAt > :now) "
            + "and g.grantedBy.username = 'hyflo-bootstrap'")
    long countNonEndedBootstrapGrants(@Param("roleId") long roleId, @Param("now") java.time.Instant now);
}

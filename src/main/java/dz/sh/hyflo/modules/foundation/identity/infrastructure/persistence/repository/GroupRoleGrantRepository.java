/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : GroupRoleGrantRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for GroupRoleGrant.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.foundation.identity.domain.model.GroupRoleGrant;
import org.springframework.data.repository.Repository;

public interface GroupRoleGrantRepository extends Repository<GroupRoleGrant, Long> {
    @org.springframework.data.jpa.repository.Query("select count(g) from GroupRoleGrant g where g.role.id = :roleId "
            + "and (g.validTo is null or g.validTo > :now) and (g.revokedAt is null or g.revokedAt > :now)")
    long countNonEndedForRole(@org.springframework.data.repository.query.Param("roleId") long roleId,
            @org.springframework.data.repository.query.Param("now") java.time.Instant now);

    @org.springframework.data.jpa.repository.Query("select count(g) from GroupRoleGrant g where g.group.id = :groupId "
            + "and (g.validTo is null or g.validTo > :now) and (g.revokedAt is null or g.revokedAt > :now)")
    long countNonEndedForGroup(@org.springframework.data.repository.query.Param("groupId") long groupId,
            @org.springframework.data.repository.query.Param("now") java.time.Instant now);
    GroupRoleGrant save(GroupRoleGrant grant);

    @org.springframework.data.jpa.repository.Query("select g from GroupRoleGrant g join fetch g.role where g.group.id = :groupId "
            + "and (g.validTo is null or g.validTo > :now) and (g.revokedAt is null or g.revokedAt > :now)")
    java.util.List<GroupRoleGrant> findNonEnded(@org.springframework.data.repository.query.Param("groupId") long groupId,
            @org.springframework.data.repository.query.Param("now") java.time.Instant now);

    @org.springframework.data.jpa.repository.Query("select g from GroupRoleGrant g join fetch g.role join fetch g.group "
            + "where g.group.id in :groupIds and g.authorizationScope.organizationUnitId = :companyId")
    java.util.List<GroupRoleGrant> findCompanyCandidates(@org.springframework.data.repository.query.Param("groupIds") java.util.Set<Long> groupIds,
            @org.springframework.data.repository.query.Param("companyId") long companyId);

    @org.springframework.data.jpa.repository.Query("select g from GroupRoleGrant g join fetch g.role where g.group.id = :groupId "
            + "and g.authorizationScope.organizationUnitId = :companyId and g.authorizationScope.scopeType = 'GLOBAL' "
            + "and g.authorizationScope.scopeReferenceId is null and g.id > :afterId order by g.id")
    java.util.List<GroupRoleGrant> findGlobalPage(@org.springframework.data.repository.query.Param("groupId") long groupId,
            @org.springframework.data.repository.query.Param("companyId") long companyId,
            @org.springframework.data.repository.query.Param("afterId") long afterId, org.springframework.data.domain.Pageable page);

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select g from GroupRoleGrant g where g.id = :id and g.group.id = :groupId "
            + "and g.authorizationScope.organizationUnitId = :companyId")
    java.util.Optional<GroupRoleGrant> findSelectedForUpdate(@org.springframework.data.repository.query.Param("id") long id,
            @org.springframework.data.repository.query.Param("groupId") long groupId,
            @org.springframework.data.repository.query.Param("companyId") long companyId);
}

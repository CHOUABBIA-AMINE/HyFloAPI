/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : RolePermissionRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for RolePermission.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import java.util.List;
import java.util.Set;

import dz.sh.hyflo.modules.foundation.identity.domain.model.Permission;
import dz.sh.hyflo.modules.foundation.identity.domain.model.RolePermission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface RolePermissionRepository extends Repository<RolePermission, Long> {

    RolePermission save(RolePermission entity);

    @Query("select p from RolePermission rp join rp.permission p join fetch p.action join fetch p.resourceType "
            + "where rp.role.id = :roleId and p.id > :afterId order by p.id")
    List<Permission> findCatalogPageForRole(@Param("roleId") long roleId,
            @Param("afterId") long afterId, Pageable page);

    @Query("select rp from RolePermission rp join fetch rp.role join fetch rp.permission p "
            + "join fetch p.action join fetch p.resourceType where rp.role.id in :roleIds")
    List<RolePermission> findForRoles(@Param("roleIds") Set<Long> roleIds);
    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_READ)
    @Query("select rp from RolePermission rp join fetch rp.role join fetch rp.permission p "
            + "join fetch p.action join fetch p.resourceType where rp.role.id in :roleIds")
    List<RolePermission> findForRolesForWrite(@Param("roleIds") Set<Long> roleIds);

}

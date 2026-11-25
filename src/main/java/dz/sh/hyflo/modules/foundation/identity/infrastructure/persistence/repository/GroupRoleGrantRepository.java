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

}

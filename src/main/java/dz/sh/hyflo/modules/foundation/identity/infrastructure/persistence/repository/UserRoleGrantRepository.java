/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserRoleGrantRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
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

}

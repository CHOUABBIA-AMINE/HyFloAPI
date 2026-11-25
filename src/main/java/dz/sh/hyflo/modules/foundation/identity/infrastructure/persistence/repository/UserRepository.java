/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for User.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import java.util.Optional;

import dz.sh.hyflo.modules.foundation.identity.domain.model.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User, Long> {

    User save(User entity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :id")
    Optional<User> findForUpdate(@Param("id") long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.identityProvider.id = :providerId and u.username = :username")
    Optional<User> findForAuthentication(@Param("providerId") long providerId, @Param("username") String username);
    @Query("select u from User u where u.id = :id and u.owningOrganizationUnitId = :companyId and u.username <> 'hyflo-bootstrap'")
    Optional<User> findOwned(@Param("id") long id, @Param("companyId") long companyId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :id and u.owningOrganizationUnitId = :companyId and u.username <> 'hyflo-bootstrap'")
    Optional<User> findOwnedForUpdate(@Param("id") long id, @Param("companyId") long companyId);

    @Query("select u from User u where u.owningOrganizationUnitId = :companyId and u.id > :afterId "
            + "and u.username <> 'hyflo-bootstrap' order by u.id")
    java.util.List<User> findOwnedPage(@Param("companyId") long companyId, @Param("afterId") long afterId,
            org.springframework.data.domain.Pageable page);

}

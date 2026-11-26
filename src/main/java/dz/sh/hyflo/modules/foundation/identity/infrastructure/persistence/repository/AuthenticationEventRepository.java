/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuthenticationEventRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for AuthenticationEvent.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.foundation.identity.domain.model.AuthenticationEvent;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.Repository;

public interface AuthenticationEventRepository extends Repository<AuthenticationEvent, Long> {

    AuthenticationEvent save(AuthenticationEvent entity);

    @Query("select e from AuthenticationEvent e join e.user u where u.owningOrganizationUnitId = :companyId and u.username <> 'hyflo-bootstrap' and e.id > :afterId order by e.id asc")
    List<AuthenticationEvent> findCompanyPage(@Param("companyId") long companyId, @Param("afterId") long afterId, Pageable page);

    @Query("select e from AuthenticationEvent e where e.user is null and e.eventCode = 'LOGIN_FAILED' and e.id > :afterId order by e.id asc")
    List<AuthenticationEvent> findUnattributedPage(@Param("afterId") long afterId, Pageable page);
}

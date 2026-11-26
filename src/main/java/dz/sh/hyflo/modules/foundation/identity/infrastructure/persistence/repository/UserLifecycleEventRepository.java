/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserLifecycleEventRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : Persists append-only account lifecycle evidence.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.foundation.identity.domain.model.UserLifecycleEvent;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.Repository;

public interface UserLifecycleEventRepository extends Repository<UserLifecycleEvent, Long> {
    UserLifecycleEvent save(UserLifecycleEvent entity);

    @Query("select e from UserLifecycleEvent e join e.user u where u.owningOrganizationUnitId = :companyId and e.owningOrganizationUnitId = :companyId and u.username <> 'hyflo-bootstrap' and e.id > :afterId order by e.id asc")
    List<UserLifecycleEvent> findCompanyPage(@Param("companyId") long companyId, @Param("afterId") long afterId, Pageable page);
}

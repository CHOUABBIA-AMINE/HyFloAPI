/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : UserEmployeeAssociationEventRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : Append-only association evidence persistence.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.foundation.identity.domain.model.UserEmployeeAssociationEvent;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.Repository;

public interface UserEmployeeAssociationEventRepository extends Repository<UserEmployeeAssociationEvent, Long> {
    UserEmployeeAssociationEvent save(UserEmployeeAssociationEvent event);

    @Query("select e from UserEmployeeAssociationEvent e join e.user u where u.owningOrganizationUnitId = :companyId and e.owningOrganizationUnitId = :companyId and u.username <> 'hyflo-bootstrap' and e.id > :afterId order by e.id asc")
    List<UserEmployeeAssociationEvent> findCompanyPage(@Param("companyId") long companyId, @Param("afterId") long afterId, Pageable page);
}

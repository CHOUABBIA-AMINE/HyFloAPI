/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : CapabilityReconciliationEventRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : CapabilityReconciliationEventRepository for controlled capability reconciliation.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.foundation.identity.domain.model.CapabilityReconciliationEvent;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.Repository;

public interface CapabilityReconciliationEventRepository extends Repository<CapabilityReconciliationEvent, Long> {
    CapabilityReconciliationEvent save(CapabilityReconciliationEvent event);

    @Query("select e from CapabilityReconciliationEvent e where e.id > :afterId order by e.id asc")
    List<CapabilityReconciliationEvent> findHistoryPage(@Param("afterId") long afterId, Pageable page);
}

/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : GroupDefinitionEventRepository
 * @CreatedOn   : 11-26-2026
 * @UpdatedOn   : 11-26-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : GroupDefinitionEventRepository for scoped platform administration.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.foundation.identity.domain.model.GroupDefinitionEvent;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.Repository;

public interface GroupDefinitionEventRepository extends Repository<GroupDefinitionEvent, Long> {
    GroupDefinitionEvent save(GroupDefinitionEvent event);

    @Query("select e from GroupDefinitionEvent e where e.id > :afterId order by e.id asc")
    List<GroupDefinitionEvent> findHistoryPage(@Param("afterId") long afterId, Pageable page);
}

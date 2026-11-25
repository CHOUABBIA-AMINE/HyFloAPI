/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : RoleDefinitionEventRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : RoleDefinitionEventRepository for scoped platform administration.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.foundation.identity.domain.model.RoleDefinitionEvent;
import org.springframework.data.repository.Repository;

public interface RoleDefinitionEventRepository extends Repository<RoleDefinitionEvent, Long> {
    RoleDefinitionEvent save(RoleDefinitionEvent event);
}

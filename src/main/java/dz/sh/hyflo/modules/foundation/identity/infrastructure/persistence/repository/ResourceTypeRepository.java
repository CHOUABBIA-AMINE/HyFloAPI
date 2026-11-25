/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : ResourceTypeRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Identity
 * @Package     : dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for ResourceType.
 */

package dz.sh.hyflo.modules.foundation.identity.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.foundation.identity.domain.model.ResourceType;
import org.springframework.data.repository.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ResourceTypeRepository extends Repository<ResourceType, Long> {
    long count();

    ResourceType save(ResourceType entity);

    List<ResourceType> findByIdGreaterThanOrderByIdAsc(long afterId, Pageable page);
}

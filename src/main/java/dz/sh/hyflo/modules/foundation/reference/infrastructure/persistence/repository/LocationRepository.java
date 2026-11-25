/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : LocationRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Reference
 * @Package     : dz.sh.hyflo.modules.foundation.reference.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for Location.
 */

package dz.sh.hyflo.modules.foundation.reference.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.foundation.reference.domain.model.Location;
import org.springframework.data.repository.Repository;

public interface LocationRepository extends Repository<Location, Long> {
}

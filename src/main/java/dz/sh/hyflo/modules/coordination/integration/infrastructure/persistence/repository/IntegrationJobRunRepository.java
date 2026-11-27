/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IntegrationJobRunRepository
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Coordination / Integration
 * @Package     : dz.sh.hyflo.modules.coordination.integration.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for IntegrationJobRun.
 */

package dz.sh.hyflo.modules.coordination.integration.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.coordination.integration.domain.model.IntegrationJobRun;
import org.springframework.data.repository.Repository;

public interface IntegrationJobRunRepository extends Repository<IntegrationJobRun, Long> {
}

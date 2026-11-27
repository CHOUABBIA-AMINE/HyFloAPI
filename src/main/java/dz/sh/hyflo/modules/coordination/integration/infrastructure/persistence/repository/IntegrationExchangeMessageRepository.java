/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : IntegrationExchangeMessageRepository
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Coordination / Integration
 * @Package     : dz.sh.hyflo.modules.coordination.integration.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for IntegrationExchangeMessage.
 */

package dz.sh.hyflo.modules.coordination.integration.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.coordination.integration.domain.model.IntegrationExchangeMessage;
import org.springframework.data.repository.Repository;

public interface IntegrationExchangeMessageRepository extends Repository<IntegrationExchangeMessage, Long> {
}

/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : ParameterValueRepository
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Coordination / Configuration
 * @Package     : dz.sh.hyflo.modules.coordination.configuration.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for ParameterValue.
 */

package dz.sh.hyflo.modules.coordination.configuration.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.coordination.configuration.domain.model.ParameterValue;
import org.springframework.data.repository.Repository;

public interface ParameterValueRepository extends Repository<ParameterValue, Long> {
}

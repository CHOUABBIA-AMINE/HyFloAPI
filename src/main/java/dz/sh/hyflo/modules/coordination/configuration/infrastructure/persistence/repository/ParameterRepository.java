/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : ParameterRepository
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Coordination / Configuration
 * @Package     : dz.sh.hyflo.modules.coordination.configuration.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for Parameter.
 */

package dz.sh.hyflo.modules.coordination.configuration.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.coordination.configuration.domain.model.Parameter;
import org.springframework.data.repository.Repository;

public interface ParameterRepository extends Repository<Parameter, Long> {
}

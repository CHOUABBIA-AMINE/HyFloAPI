/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : FeatureFlagRepository
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Coordination / Configuration
 * @Package     : dz.sh.hyflo.modules.coordination.configuration.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for FeatureFlag.
 */

package dz.sh.hyflo.modules.coordination.configuration.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.coordination.configuration.domain.model.FeatureFlag;
import org.springframework.data.repository.Repository;

public interface FeatureFlagRepository extends Repository<FeatureFlag, Long> {
}

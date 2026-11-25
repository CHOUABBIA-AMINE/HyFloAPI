/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : OrganizationUnitStatusRepository
 * @CreatedOn   : 11-25-2026
 * @UpdatedOn   : 11-25-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Foundation / Organization
 * @Package     : dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for OrganizationUnitStatus.
 */

package dz.sh.hyflo.modules.foundation.organization.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.foundation.organization.domain.model.OrganizationUnitStatus;
import org.springframework.data.repository.Repository;

public interface OrganizationUnitStatusRepository extends Repository<OrganizationUnitStatus, Long> {
}

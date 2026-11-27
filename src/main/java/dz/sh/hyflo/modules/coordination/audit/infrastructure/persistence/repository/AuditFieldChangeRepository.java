/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : AuditFieldChangeRepository
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Coordination / Audit
 * @Package     : dz.sh.hyflo.modules.coordination.audit.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for AuditFieldChange.
 */

package dz.sh.hyflo.modules.coordination.audit.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.coordination.audit.domain.model.AuditFieldChange;
import org.springframework.data.repository.Repository;

public interface AuditFieldChangeRepository extends Repository<AuditFieldChange, Long> {
}

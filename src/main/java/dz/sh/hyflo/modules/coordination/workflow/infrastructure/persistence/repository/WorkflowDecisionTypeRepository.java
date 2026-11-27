/**
 *
 * @Project     : HyFloAPI
 * @Product     : HyFlo - Hydrocarbon Flow Management Platform
 * @Author      : OpenAI Codex
 * @Owner       : SONATRACH / TRC : Digitalization Initiative
 *
 * @Name        : WorkflowDecisionTypeRepository
 * @CreatedOn   : 11-27-2026
 * @UpdatedOn   : 11-27-2026
 *
 * @Type        : Interface
 * @Layer       : Repository
 * @Module      : Modules / Coordination / Workflow
 * @Package     : dz.sh.hyflo.modules.coordination.workflow.infrastructure.persistence.repository
 *
 * @Description : Declares the module-owned persistence repository for WorkflowDecisionType.
 */

package dz.sh.hyflo.modules.coordination.workflow.infrastructure.persistence.repository;

import dz.sh.hyflo.modules.coordination.workflow.domain.model.WorkflowDecisionType;
import org.springframework.data.repository.Repository;

public interface WorkflowDecisionTypeRepository extends Repository<WorkflowDecisionType, Long> {
}

package dz.sh.trc.hyflo.core.system.audit.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.core.system.audit.dto.request.AuditFilterDTO;
import dz.sh.trc.hyflo.core.system.audit.dto.request.CreateAuditRequest;
import dz.sh.trc.hyflo.core.system.audit.dto.request.UpdateAuditRequest;
import dz.sh.trc.hyflo.core.system.audit.dto.response.AuditResponse;
import dz.sh.trc.hyflo.core.system.audit.dto.response.AuditSummary;
import dz.sh.trc.hyflo.platform.kernel.BaseService;

public interface AuditService extends BaseService<CreateAuditRequest, UpdateAuditRequest, AuditResponse, AuditSummary> {
    Page<AuditSummary> search(AuditFilterDTO filter, Pageable pageable);
    
    void log(String entityName, Long entityId, String action, String description);

    /**
     * Records a workflow state transition.
     *
     * @param entityName Class name of the entity
     * @param entityId   ID of the entity instance
     * @param fromState  Previous state code
     * @param toState    New state code
     * @param action     Workflow action (e.g., "SUBMIT", "APPROVE")
     * @param actorId    Employee ID who performed the action
     */
    void logWorkflow(String entityName, Long entityId, String fromState, String toState, String action, Long actorId);

    /**
     * Detailed logging with metadata.
     */
    void log(String entityName, Long entityId, String action, String description, Map<String, Object> metadata);
}

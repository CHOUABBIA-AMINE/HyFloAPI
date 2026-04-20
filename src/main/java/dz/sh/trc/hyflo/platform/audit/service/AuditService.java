package dz.sh.trc.hyflo.platform.audit.service;

import java.util.Map;

/**
 * Platform service for recording business activities and workflow transitions.
 * Records go to the T_00_03_01 (Audited) table.
 */
public interface AuditService {

    /**
     * Records a generic business action.
     *
     * @param entityName  Class name of the entity (e.g., "Pipeline")
     * @param entityId    ID of the entity instance
     * @param action      Action performed (e.g., "CREATE", "UPDATE", "DELETE")
     * @param description Human-readable description
     */
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

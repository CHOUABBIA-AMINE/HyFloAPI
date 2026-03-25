/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowThresholdCommandService
 *  @CreatedOn  : Phase 5 — Commit 35
 *
 *  @Type       : Interface
 *  @Layer      : Service (Command)
 *  @Package    : Flow / Core
 *
 *  @Description: Command contract for FlowThreshold write operations.
 *                Handles create / update / delete / activate / deactivate.
 *                Business invariant: only one active threshold per pipeline.
 *                Activation of a new threshold must deactivate the existing active one.
 *                Implementations must enforce this invariant atomically.
 *
 *  Phase 5 — Commit 35
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

import dz.sh.trc.hyflo.flow.core.dto.FlowThresholdDTO;

/**
 * Command contract for FlowThreshold write operations.
 *
 * Business invariant: only one active threshold per pipeline at any time.
 * Implementations must enforce this atomically on create, update, and activate.
 *
 * Phase 5 — Commit 35
 */
public interface FlowThresholdCommandService {

    /**
     * Create a new threshold for a pipeline.
     * If active=true, deactivates any existing active threshold for the same pipeline.
     * Validates that min < max for all physical parameters (pressure, temperature, flow rate, volume).
     *
     * @throws dz.sh.trc.hyflo.exception.ResourceNotFoundException if pipeline not found
     * @throws dz.sh.trc.hyflo.exception.BusinessValidationException if range validation fails
     */
    FlowThresholdDTO createThreshold(FlowThresholdDTO dto);

    /**
     * Update an existing threshold.
     * If activating an inactive threshold, deactivates the currently active threshold
     * for the same pipeline.
     * Validates range constraints on all updated physical parameters.
     *
     * @throws dz.sh.trc.hyflo.exception.ResourceNotFoundException if threshold not found
     * @throws dz.sh.trc.hyflo.exception.BusinessValidationException if range validation fails
     */
    FlowThresholdDTO updateThreshold(Long id, FlowThresholdDTO dto);

    /**
     * Hard-delete a threshold.
     * WARNING: No audit trail. Prefer deactivateThreshold() to preserve configuration history.
     *
     * @throws dz.sh.trc.hyflo.exception.ResourceNotFoundException if threshold not found
     */
    void deleteThreshold(Long id);

    /**
     * Activate a threshold.
     * Deactivates the currently active threshold for the same pipeline if one exists.
     * If already active, returns the current state without modification.
     *
     * @throws dz.sh.trc.hyflo.exception.ResourceNotFoundException if threshold not found
     */
    FlowThresholdDTO activateThreshold(Long id);

    /**
     * Deactivate a threshold.
     * After deactivation, the pipeline will have no active threshold until another is activated.
     * If already inactive, returns the current state without modification.
     *
     * @throws dz.sh.trc.hyflo.exception.ResourceNotFoundException if threshold not found
     */
    FlowThresholdDTO deactivateThreshold(Long id);
}

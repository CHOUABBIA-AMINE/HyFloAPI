/**
 * HyFlo v2 — Flow Workflow Controller Layer
 *
 * <h2>Boundary declaration — Phase 4 (Commit 34)</h2>
 *
 * <p>This package contains REST controllers for the reading governance workflow.
 * WorkflowInstance is the authoritative state source.
 * ValidationStatus is maintained as a compatibility projection only.
 *
 * <h3>v2 Active Controllers</h3>
 * <ul>
 *   <li>{@link dz.sh.trc.hyflo.flow.workflow.controller.ReadingWorkflowV2Controller}       — /api/v2/flow/workflow (approve/reject)</li>
 *   <li>{@link dz.sh.trc.hyflo.flow.workflow.controller.WorkflowInstanceQueryController}   — /api/v2/flow/workflow/instances</li>
 *   <li>{@link dz.sh.trc.hyflo.flow.workflow.controller.SlotCoverageController}            — /api/v2/flow/slot-coverage</li>
 * </ul>
 *
 * <h3>Deprecated Legacy Controllers</h3>
 * <ul>
 *   <li>{@link dz.sh.trc.hyflo.flow.workflow.controller.ReadingWorkflowController}  — [DEPRECATED] /flow/workflow/reading</li>
 * </ul>
 *
 * <h3>WorkflowInstance Ownership Model</h3>
 * WorkflowInstance does NOT own a FK back to the domain entity it governs.
 * Domain entities ({@code FlowReading}, {@code FlowOperation}) own the FK to their WorkflowInstance.
 * See: {@link dz.sh.trc.hyflo.flow.workflow.repository.WorkflowInstanceRepository} for query model.
 *
 * Phase 4 — Commit 34
 */
package dz.sh.trc.hyflo.flow.workflow.controller;

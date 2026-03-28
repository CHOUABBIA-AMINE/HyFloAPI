/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ReadableTargetValidationService
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-28-2026 — refactor: moved from flow.core.service → flow.workflow.service
 *                             Rationale: validates workflow eligibility of reading targets,
 *                             not core flow data — belongs in workflow layer.
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Workflow
 *
 *  @Description: Service-layer validation for readable infrastructure targets on FlowReading.
 *                Enforces allowed target types: PIPELINE, PIPELINE_SEGMENT, STATION, TERMINAL.
 *                Validation lives here — NOT in entities, NOT in DTOs.
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.service;

import dz.sh.trc.hyflo.flow.core.dto.command.FlowReadingCommandDTO;

/**
 * Validates readable infrastructure targets for direct flow readings.
 *
 * Allowed readable target types:
 *   PIPELINE, PIPELINE_SEGMENT, STATION, TERMINAL
 *
 * Pipeline remains mandatory for FlowReading (preserved from existing model).
 * Optional secondary targets are validated here if present in the command DTO.
 *
 * This validation lives in workflow service layer only — not in entities, not in DTOs.
 */
public interface ReadableTargetValidationService {

    /**
     * Validate that the command DTO references only allowed readable target types.
     * Throws IllegalArgumentException if an invalid or non-existent target is specified.
     *
     * Rules enforced:
     * 1. pipelineId is always required (validated separately in FlowReadingCommandServiceImpl).
     * 2. If pipelineSegmentId is present: segment must exist and belong to the specified pipeline.
     * 3. If stationId is present: station must exist.
     * 4. If terminalId is present: terminal must exist.
     * 5. At most ONE secondary target may be specified per reading.
     */
    void validateCommandTarget(FlowReadingCommandDTO command);
}

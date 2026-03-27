/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ReadableTargetValidationService
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Core
 *
 *  @Description: Service-layer validation for readable infrastructure targets on FlowReading.
 *                Enforces allowed target types: PIPELINE, PIPELINE_SEGMENT, STATION, TERMINAL.
 *                Validation lives here — NOT in entities, NOT in DTOs.
 *
 *  Phase 3 — Commit 20
 *
 **/

package dz.sh.trc.hyflo.flow.core.service;

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
 * This validation lives in service layer only — not in entities, not in DTOs.
 *
 * Phase 3 — Commit 20
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

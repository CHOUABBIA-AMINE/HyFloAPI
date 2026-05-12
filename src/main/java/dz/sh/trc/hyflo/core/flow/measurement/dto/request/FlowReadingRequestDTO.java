/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowReadingRequestDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 04-19-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Flow / Measurement
 *
 **/

package dz.sh.trc.hyflo.core.flow.measurement.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO used for creating and updating FlowReading entities.
 * Does not expose JPA entities; only scalar values and foreign key IDs.
 */
@Schema(description = "Request payload for creating or updating a flow reading")
public class FlowReadingRequestDTO {

    // --- Measurement core ---

    @Schema(description = "Date of the reading", example = "2026-03-20", required = true)
    private LocalDate readingDate;

    @Schema(description = "Measured flow volume in m³", example = "12500.50")
    private BigDecimal volumeM3;

    @Schema(description = "Measured flow volume in MSCF", example = "441.45")
    private BigDecimal volumeMscf;

    @Schema(description = "Inlet pressure in bar", example = "68.5")
    private BigDecimal inletPressureBar;

    @Schema(description = "Outlet pressure in bar", example = "65.2")
    private BigDecimal outletPressureBar;

    @Schema(description = "Temperature in Celsius", example = "22.3")
    private BigDecimal temperatureCelsius;

    @Schema(description = "Operator notes or observations")
    private String notes;

    // --- Timestamps (operator and lifecycle) ---

    @Schema(description = "Timestamp when the record was submitted by operator")
    private LocalDateTime submittedAt;

    @Schema(description = "Timestamp when the record was validated")
    private LocalDateTime validatedAt;

    @Schema(description = "Timestamp when the reading was first recorded (entered)")
    private LocalDateTime recordedAt;

    // --- Foreign key IDs (flattened relationships) ---

    @Schema(description = "Pipeline ID (FK to core.network.topology.Pipeline)", required = true)
    private Long pipelineId;

    @Schema(description = "Validation status ID (FK to core.flow.reference.ValidationStatus)")
    private Long validationStatusId;

    @Schema(description = "Workflow instance ID (FK to core.flow.workflow.WorkflowInstance)")
    private Long workflowInstanceId;

    @Schema(description = "Data source ID (FK to core.flow.reference.DataSource)")
    private Long dataSourceId;

    @Schema(description = "Reading slot ID (FK to core.flow.reference.ReadingSlot)")
    private Long readingSlotId;

    @Schema(description = "Employee ID of operator who recorded the reading")
    private Long recordedByEmployeeId;

    @Schema(description = "Employee ID of validator/approver")
    private Long validatedByEmployeeId;

    // --- Optimistic locking ---

    // Getters and setters omitted for brevity
    // (Use Lombok @Getter/@Setter if consistent with project style.)
}
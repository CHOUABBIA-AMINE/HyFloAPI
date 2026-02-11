/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ReadingSubmitRequestDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Workflow
 *
 **/

package dz.sh.trc.hyflo.flow.workflow.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import dz.sh.trc.hyflo.network.core.dto.PipelineDTO;
import dz.sh.trc.hyflo.flow.common.dto.ReadingSlotDTO;
import dz.sh.trc.hyflo.general.organization.dto.EmployeeDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Workflow command DTO for submitting flow readings.
 * 
 * NOTE: Intentionally does NOT extend GenericDTO because:
 * - Represents a workflow operation, not a persistent entity
 * - Maps to complex business logic (FlowReadingWorkflowService)
 * - Service layer handles entity orchestration
 * 
 * PATTERN: Dual representation (ID + nested DTO)
 * - ID fields: Used by backend for queries/updates
 * - Nested DTOs: Used by frontend for display (includes translations)
 * 
 * @see dz.sh.trc.hyflo.flow.core.service.FlowReadingWorkflowService#submitReading
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Request to submit or save draft flow reading")
public class ReadingSubmitRequestDTO {
    
    @Schema(description = "Reading ID for update, null for new reading", example = "123")
    private Long readingId;
    
    // ========== PIPELINE CONTEXT (ID + nested DTO) ==========
    
    @NotNull(message = "Pipeline ID is required")
    @Schema(description = "Pipeline ID", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long pipelineId;
    
    @Schema(description = "Pipeline details (code, name, translations)")
    private PipelineDTO pipeline;
    
    // ========== READING CONTEXT ==========
    
    @NotNull(message = "Reading date is required")
    @Schema(description = "Date of the reading", example = "2026-02-04", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate readingDate;
    
    // ========== SLOT CONTEXT (ID + nested DTO) ==========
    
    @NotNull(message = "Slot ID is required")
    @Schema(description = "Reading slot ID", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long slotId;
    
    @Schema(description = "Reading slot details (time range, translations)")
    private ReadingSlotDTO slot;
    
    // ========== OPERATOR CONTEXT (ID + nested DTO) ==========
    
    @NotNull(message = "Employee ID is required")
    @Schema(description = "Employee recording the reading", example = "42", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long employeeId;
    
    @Schema(description = "Employee details (name, structure)")
    private EmployeeDTO employee;
    
    // ========== MEASUREMENT DATA ==========
    
    @NotNull(message = "Pressure is required")
    @PositiveOrZero(message = "Pressure must be positive or zero")
    @DecimalMin(value = "0.0", message = "Pressure cannot be negative")
    @DecimalMax(value = "500.0", message = "Pressure exceeds maximum (500 bar)")
    @Schema(description = "Pressure in bar", example = "85.50", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "0.0", maximum = "500.0")
    private BigDecimal pressure;
    
    @NotNull(message = "Temperature is required")
    @DecimalMin(value = "-50.0", message = "Temperature below minimum range")
    @DecimalMax(value = "200.0", message = "Temperature exceeds maximum range")
    @Schema(description = "Temperature in Celsius", example = "65.5", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "-50.0", maximum = "200.0")
    private BigDecimal temperature;
    
    @NotNull(message = "Flow rate is required")
    @PositiveOrZero(message = "Flow rate must be positive or zero")
    @Schema(description = "Flow rate in m³/h", example = "1250.75", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal flowRate;
    
    @PositiveOrZero(message = "Contained volume must be positive or zero")
    @Schema(description = "Contained volume in m³", example = "5000.00")
    private BigDecimal containedVolume;
    
    // ========== METADATA ==========
    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    @Schema(description = "Additional notes or observations", example = "Normal operation", maxLength = 500)
    private String notes;
    
    @Schema(description = "Submit immediately (SUBMITTED) or save as draft (DRAFT)", example = "true")
    private Boolean submitImmediately; // true = SUBMITTED, false/null = DRAFT
}

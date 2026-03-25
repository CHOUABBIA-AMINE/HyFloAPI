/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineCommandDto
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Command
 *  @Package    : Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.dto.command;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command DTO for creating or updating a Pipeline infrastructure entity.
 *
 * Contains only writable fields and FK IDs.
 * Validation annotations belong exclusively here.
 */
@Schema(description = "Command DTO for creating or updating a hydrocarbon transport pipeline")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipelineCommandDto {

    @Schema(description = "Unique identification code", example = "OLZ-PL-001",
            requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 20)
    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Schema(description = "Official name of the pipeline",
            example = "Hassi Messaoud - Skikda Crude Oil Pipeline",
            requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Schema(description = "Date when the pipeline was physically installed", example = "2020-05-15")
    @PastOrPresent(message = "Installation date cannot be in the future")
    private LocalDate installationDate;

    @Schema(description = "Date when the pipeline was commissioned", example = "2020-08-01")
    @PastOrPresent(message = "Commissioning date cannot be in the future")
    private LocalDate commissioningDate;

    @Schema(description = "Date when the pipeline was decommissioned (if applicable)")
    private LocalDate decommissioningDate;

    @Schema(description = "Nominal internal diameter", example = "48 inches",
            requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 255)
    @NotBlank(message = "Nominal diameter is required")
    @Size(max = 255)
    private String nominalDiameter;

    @Schema(description = "Total length in kilometers", example = "850.5",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Length is required")
    @Positive(message = "Length must be positive")
    private Double length;

    @Schema(description = "Nominal wall thickness", example = "12.7 mm",
            requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 255)
    @NotBlank(message = "Nominal thickness is required")
    @Size(max = 255)
    private String nominalThickness;

    @Schema(description = "Internal surface roughness in mm", example = "0.045",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Nominal roughness is required")
    @Positive(message = "Nominal roughness must be positive")
    private Double nominalRoughness;

    @Schema(description = "Maximum design service pressure in bar", example = "120.5",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Design max service pressure is required")
    @Positive
    private Double designMaxServicePressure;

    @Schema(description = "Maximum operational service pressure in bar", example = "100.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Operational max service pressure is required")
    @Positive
    private Double operationalMaxServicePressure;

    @Schema(description = "Minimum design service pressure in bar", example = "10.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Design min service pressure is required")
    @Positive
    private Double designMinServicePressure;

    @Schema(description = "Minimum operational service pressure in bar", example = "15.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Operational min service pressure is required")
    @Positive
    private Double operationalMinServicePressure;

    @Schema(description = "Design capacity in m\u00b3/day", example = "50000.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Design capacity is required")
    @Positive
    private Double designCapacity;

    @Schema(description = "Operational capacity in m\u00b3/day", example = "45000.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Operational capacity is required")
    @Positive
    private Double operationalCapacity;

    // --- FK references ---

    @Schema(description = "ID of the operational status", example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Operational status ID is required")
    private Long operationalStatusId;

    @Schema(description = "ID of the owning organization structure", example = "5")
    private Long ownerId;

    @Schema(description = "ID of the construction material alloy", example = "3")
    private Long nominalConstructionMaterialId;

    @Schema(description = "ID of the exterior coating alloy", example = "7")
    private Long nominalExteriorCoatingId;

    @Schema(description = "ID of the interior coating alloy", example = "9")
    private Long nominalInteriorCoatingId;

    @Schema(description = "ID of the pipeline system", example = "2",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Pipeline system ID is required")
    private Long pipelineSystemId;

    @Schema(description = "ID of the departure terminal", example = "10",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Departure terminal ID is required")
    private Long departureTerminalId;

    @Schema(description = "ID of the arrival terminal", example = "15",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Arrival terminal ID is required")
    private Long arrivalTerminalId;

    @Schema(description = "ID of the managing organization structure", example = "8",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Manager structure ID is required")
    private Long managerId;

    @Schema(description = "IDs of vendors who supplied or constructed this pipeline")
    @Builder.Default
    private Set<Long> vendorIds = new HashSet<>();
}

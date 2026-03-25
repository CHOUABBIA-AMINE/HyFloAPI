/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineReadDto
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Query
 *  @Package    : Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.dto.query;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Read DTO for a hydrocarbon transport pipeline — topology and operational context")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipelineReadDto {

    @Schema(description = "Technical identifier of the pipeline")
    private Long id;

    @Schema(description = "Unique pipeline code", example = "OLZ-PL-001")
    private String code;

    @Schema(description = "Official pipeline name", example = "Hassi Messaoud - Skikda Crude Oil Pipeline")
    private String name;

    @Schema(description = "Total length in kilometers", example = "850.5")
    private Double length;

    @Schema(description = "Nominal internal diameter", example = "48 inches")
    private String nominalDiameter;

    @Schema(description = "Nominal wall thickness", example = "12.7 mm")
    private String nominalThickness;

    @Schema(description = "Maximum operational service pressure in bar", example = "100.0")
    private Double operationalMaxServicePressure;

    @Schema(description = "Minimum operational service pressure in bar", example = "15.0")
    private Double operationalMinServicePressure;

    @Schema(description = "Operational capacity in m\u00b3/day", example = "45000.0")
    private Double operationalCapacity;

    @Schema(description = "Installation date")
    private LocalDate installationDate;

    @Schema(description = "Commissioning date")
    private LocalDate commissioningDate;

    @Schema(description = "Decommissioning date (null if active)")
    private LocalDate decommissioningDate;

    @Schema(description = "ID of the operational status")
    private Long operationalStatusId;

    @Schema(description = "Code of the operational status", example = "ACTIVE")
    private String operationalStatusCode;

    @Schema(description = "ID of the departure terminal")
    private Long departureTerminalId;

    @Schema(description = "Code of the departure terminal", example = "TRC-HASSI-MESSAOUD")
    private String departureTerminalCode;

    @Schema(description = "ID of the arrival terminal")
    private Long arrivalTerminalId;

    @Schema(description = "Code of the arrival terminal", example = "TRC-SKIKDA")
    private String arrivalTerminalCode;

    @Schema(description = "ID of the pipeline system")
    private Long pipelineSystemId;

    @Schema(description = "Code of the pipeline system", example = "GZ1-SYSTEM")
    private String pipelineSystemCode;

    @Schema(description = "ID of the owning structure")
    private Long ownerId;

    @Schema(description = "Name of the owning structure")
    private String ownerName;

    @Schema(description = "ID of the managing structure")
    private Long managerId;

    @Schema(description = "Name of the managing structure")
    private String managerName;

    // ---- NO fromEntity / mapping logic ----
    // All mapping is owned by PipelineMapper.
}

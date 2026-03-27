/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineSegmentCommandDTO
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Command
 *  @Package    : Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.dto.command;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Command DTO for creating or updating a pipeline segment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipelineSegmentCommandDTO {

    @Schema(description = "Unique segment code", example = "GZ1-SEG-04",
            requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 30)
    @NotBlank(message = "Segment code is required")
    @Size(max = 30)
    private String code;

    @Schema(description = "Segment name or label", example = "Hassi R'Mel - In Salah Section",
            requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 150)
    @NotBlank(message = "Segment name is required")
    @Size(max = 150)
    private String name;

    @Schema(description = "Segment length in kilometers", example = "210.5",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Segment length is required")
    @Positive(message = "Segment length must be positive")
    private Double length;

    @Schema(description = "Start kilometre marker on the parent pipeline", example = "0.0")
    private Double startKm;

    @Schema(description = "End kilometre marker on the parent pipeline", example = "210.5")
    private Double endKm;

    @Schema(description = "ID of the parent pipeline", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Pipeline ID is required")
    private Long pipelineId;

    @Schema(description = "ID of the operational status", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Operational status ID is required")
    private Long operationalStatusId;

    @Schema(description = "ID of the departure node facility")
    private Long departureNodeId;

    @Schema(description = "ID of the arrival node facility")
    private Long arrivalNodeId;

    @Schema(description = "ID of the alloy or material used in this segment")
    private Long constructionMaterialId;
}

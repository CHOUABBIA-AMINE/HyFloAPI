/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineSegmentReadDto
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Query
 *  @Package    : Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.dto.query;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Read DTO for a pipeline segment — topology and physical context")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PipelineSegmentReadDto {

    @Schema(description = "Technical identifier of the segment")
    private Long id;

    @Schema(description = "Unique segment code", example = "GZ1-SEG-04")
    private String code;

    @Schema(description = "Segment name or label")
    private String name;

    @Schema(description = "Segment length in km", example = "210.5")
    private Double length;

    @Schema(description = "Start kilometre marker", example = "0.0")
    private Double startKm;

    @Schema(description = "End kilometre marker", example = "210.5")
    private Double endKm;

    @Schema(description = "ID of the parent pipeline")
    private Long pipelineId;

    @Schema(description = "Code of the parent pipeline", example = "GZ1-HASSI-ARZEW")
    private String pipelineCode;

    @Schema(description = "Name of the parent pipeline")
    private String pipelineName;

    @Schema(description = "ID of the operational status")
    private Long operationalStatusId;

    @Schema(description = "Code of the operational status", example = "ACTIVE")
    private String operationalStatusCode;

    @Schema(description = "ID of the construction material alloy")
    private Long constructionMaterialId;

    @Schema(description = "Code of the construction material alloy", example = "API-5L-X65")
    private String constructionMaterialCode;

    @Schema(description = "ID of the departure node facility")
    private Long departureNodeId;

    @Schema(description = "Code of the departure node facility", example = "TRC-HR-BOOSTER")
    private String departureNodeCode;

    @Schema(description = "ID of the arrival node facility")
    private Long arrivalNodeId;

    @Schema(description = "Code of the arrival node facility", example = "TRC-HR-INSALAH")
    private String arrivalNodeCode;

    // ---- NO fromEntity / mapping logic ----
    // All mapping is owned by PipelineSegmentMapper.
}

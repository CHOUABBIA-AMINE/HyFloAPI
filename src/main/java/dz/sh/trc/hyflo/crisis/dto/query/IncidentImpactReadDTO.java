/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IncidentImpactReadDTO
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 - Aligned fields with real IncidentImpact entity
 *
 *  @Type       : Class
 *  @Layer      : DTO / Query
 *  @Package    : Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.dto.query;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Read DTO for the measured impact of a pipeline incident")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentImpactReadDTO {

    @Schema(description = "Technical identifier of the incident impact record")
    private Long id;

    @Schema(description = "ID of the parent incident")
    private Long incidentId;

    @Schema(description = "Impact level classification", example = "CRITICAL")
    private String impactLevel;

    @Schema(description = "Estimated economic loss (local currency)", example = "1500000.00")
    private BigDecimal estimatedLoss;

    @Schema(description = "Total downtime in minutes", example = "180")
    private Long downtimeMinutes;

    @Schema(description = "IDs of affected pipelines")
    private List<Long> affectedPipelineIds;

    @Schema(description = "IDs of affected pipeline segments")
    private List<Long> affectedSegmentIds;

    // ---- NO fromEntity / mapping logic ----
    // All mapping is owned by IncidentImpactMapper.
}

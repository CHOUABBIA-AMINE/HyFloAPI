/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IncidentImpactReadDto
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Query
 *  @Package    : Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.dto.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class IncidentImpactReadDto {

    @Schema(description = "Technical identifier of the incident impact record")
    private Long id;

    @Schema(description = "ID of the parent incident")
    private Long incidentId;

    @Schema(description = "Impact category", example = "VOLUME_LOSS")
    private String impactCategory;

    @Schema(description = "Estimated volume loss in m\u00b3", example = "1500.0000")
    private BigDecimal estimatedVolumeLossM3;

    @Schema(description = "Duration of operational disruption in hours", example = "6.50")
    private BigDecimal disruptionDurationHours;

    @Schema(description = "Financial impact estimate in USD", example = "250000.00")
    private BigDecimal financialImpactUsd;

    @Schema(description = "Timestamp when impact was assessed")
    private LocalDateTime assessedAt;

    @Schema(description = "ID of the employee who assessed the impact")
    private Long assessedById;

    @Schema(description = "Full name of the employee who assessed the impact")
    private String assessedByFullName;

    // ---- NO fromEntity / mapping logic ----
    // All mapping is owned by IncidentImpactMapper.
}

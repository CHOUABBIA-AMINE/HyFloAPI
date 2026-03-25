/**
 * 
 * 	@Author		: HyFlo v2 DTO
 *
 * 	@Name		: IncidentImpactReadDto
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.dto;

import java.math.BigDecimal;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Read DTO for incident impact analysis.
 */
@Schema(description = "Read DTO for incident impact analysis")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class IncidentImpactReadDto {

    @Schema(description = "Technical identifier of the impact record")
    private Long id;

    @Schema(description = "Identifier of the incident this impact record belongs to")
    private Long incidentId;

    @Schema(description = "Identifiers of pipelines affected by this incident")
    private Set<Long> affectedPipelineIds;

    @Schema(description = "Identifiers of pipeline segments affected by this incident")
    private Set<Long> affectedSegmentIds;

    @Schema(description = "Estimated economic loss associated with this incident (in local currency)")
    private BigDecimal estimatedLoss;

    @Schema(description = "Total downtime in minutes caused by this incident")
    private Long downtimeMinutes;

    @Schema(description = "Impact level classification (e.g., LOW, MEDIUM, HIGH, CRITICAL)")
    private String impactLevel;
}

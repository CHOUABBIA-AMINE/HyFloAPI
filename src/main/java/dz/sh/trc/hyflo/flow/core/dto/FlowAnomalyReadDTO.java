/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAnomalyReadDTO
 *  @CreatedOn  : 03-25-2026
 *  @MovedOn    : 03-28-2026 — refactor: flow.core.dto → flow.intelligence.dto
 *
 *  @Type       : Class
 *  @Layer      : DTO (Read)
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.intelligence.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Read DTO for a flow anomaly detected by the intelligence engine")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowAnomalyReadDTO {

    private Long id;
    private String anomalyType;
    private BigDecimal severityScore;
    private BigDecimal confidenceScore;
    private String modelName;
    private String explanation;
    private LocalDateTime detectedAt;
    private Long readingId;
    private Long derivedReadingId;
    private Long pipelineSegmentId;
    private String pipelineSegmentCode;
}

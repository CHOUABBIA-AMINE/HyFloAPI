/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IncidentSeverityReadDTO
 *  @CreatedOn  : 03-26-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Query
 *  @Package    : Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.dto.query;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Read DTO for an incident severity reference")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentSeverityReadDTO {

    @Schema(description = "Technical identifier")
    private Long id;

    @Schema(description = "Severity code", example = "P1")
    private String code;

    @Schema(description = "Severity label", example = "Critical")
    private String label;

    @Schema(description = "Priority rank (1 = highest)", example = "1")
    private Integer rank;
}

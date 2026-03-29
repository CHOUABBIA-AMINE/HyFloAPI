/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : IncidentReadDTO
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-29-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO / Query
 *  @Package    : Crisis /Core
 *
 **/

package dz.sh.trc.hyflo.crisis.core.dto.query;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Read contract for a single pipeline incident.
 *
 * <p>Field mapping summary:
 * <ul>
 *   <li>{@code id}                  ← {@code GenericModel.id} (PK)</li>
 *   <li>{@code incidentCode}        ← {@code Incident.code} (F_01, unique incident reference)</li>
 *   <li>{@code title}               ← {@code Incident.title} (F_02)</li>
 *   <li>{@code severityLevel}       ← {@code Incident.severity.code} (lazy-loaded, null-safe)</li>
 *   <li>{@code status}              ← derived: {@code "RESOLVED"} when {@code resolvedAt != null},
 *                                     otherwise {@code "ACTIVE"}; based on F_05 and F_06</li>
 *   <li>{@code description}         ← {@code Incident.description} (F_03)</li>
 *   <li>{@code occurredAt}          ← {@code Incident.occurredAt} (F_04, when the event happened)</li>
 *   <li>{@code resolvedAt}          ← {@code Incident.resolvedAt} (F_05, null when still active)</li>
 *   <li>{@code pipelineSegmentId}   ← {@code Incident.pipelineSegment.id} (lazy-loaded, null-safe)</li>
 *   <li>{@code pipelineSegmentCode} ← {@code Incident.pipelineSegment.code} (via Infrastructure)</li>
 *   <li>{@code pipelineId}          ← {@code Incident.pipelineSegment.pipeline.id} (null-safe chain)</li>
 *   <li>{@code pipelineCode}        ← {@code Incident.pipelineSegment.pipeline.code} (null-safe chain)</li>
 *   <li>{@code declaredById}        ← RESERVED — no Employee FK on Incident yet; always null</li>
 *   <li>{@code declaredByFullName}  ← RESERVED — no Employee FK on Incident yet; always null</li>
 * </ul>
 *
 * <p>All mapping is owned exclusively by {@code IncidentMapper}.
 */
@Schema(description = "Read DTO for a pipeline incident event")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentReadDTO {

    @Schema(
            description = "Technical identifier of the incident",
            example = "42"
    )
    private Long id;

    @Schema(
            description = "Unique incident reference code assigned at creation (maps to Incident.code / F_01)",
            example = "INC-2026-0012"
    )
    private String incidentCode;

    @Schema(
            description = "Short title summarising the incident (maps to Incident.title / F_02)",
            example = "Pressure drop – GZ1 Seg 3"
    )
    private String title;

    @Schema(
            description = "Severity level code from the IncidentSeverity reference table "
                    + "(e.g. P1, P2, HIGH, CRITICAL). Null if no severity is linked.",
            example = "P1"
    )
    private String severityLevel;

    @Schema(
            description = "Computed incident lifecycle status. "
                    + "Value is 'RESOLVED' when resolvedAt is not null, 'ACTIVE' otherwise. "
                    + "Derived from Incident.resolvedAt (F_05) and Incident.active (F_06).",
            example = "ACTIVE",
            allowableValues = {"ACTIVE", "RESOLVED"}
    )
    private String status;

    @Schema(
            description = "Short description of the incident (maps to Incident.description / F_03)"
    )
    private String description;

    @Schema(
            description = "Timestamp when the incident physically occurred "
                    + "(maps to Incident.occurredAt / F_04). Not a declaration timestamp."
    )
    private LocalDateTime occurredAt;

    @Schema(
            description = "Timestamp when the incident was resolved. "
                    + "Null indicates the incident is still active (Incident.resolvedAt / F_05)."
    )
    private LocalDateTime resolvedAt;

    @Schema(
            description = "ID of the directly affected pipeline segment "
                    + "(Incident.pipelineSegment.id, null-safe)."
    )
    private Long pipelineSegmentId;

    @Schema(
            description = "Code of the directly affected pipeline segment "
                    + "(Incident.pipelineSegment.code via Infrastructure, null-safe).",
            example = "GZ1-SEG-02"
    )
    private String pipelineSegmentCode;

    @Schema(
            description = "ID of the parent pipeline of the affected segment "
                    + "(Incident.pipelineSegment.pipeline.id, null-safe chain)."
    )
    private Long pipelineId;

    @Schema(
            description = "Code of the parent pipeline of the affected segment "
                    + "(Incident.pipelineSegment.pipeline.code, null-safe chain).",
            example = "GZ1-HASSI-ARZEW"
    )
    private String pipelineCode;

    @Schema(
            description = "ID of the employee who declared the incident. "
                    + "RESERVED: always null until an Employee FK is added to the Incident entity.",
            nullable = true
    )
    private Long declaredById;

    @Schema(
            description = "Full name of the employee who declared the incident. "
                    + "RESERVED: always null until an Employee FK is added to the Incident entity.",
            nullable = true
    )
    private String declaredByFullName;
}

/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: Incident
 * 	@CreatedOn	: 03-25-2026
 *	@UpdatedOn	: 03-26-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 *  @Package    : Crisis / Core
 *
 **/

package dz.sh.trc.hyflo.crisis.core.model;

import java.time.LocalDateTime;

import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import dz.sh.trc.hyflo.crisis.common.model.IncidentSeverity;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a transport incident (leak, rupture, emergency shutdown)
 * affecting a pipeline segment.
 */
@Schema(description = "Hydrocarbon transport incident affecting a pipeline segment")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Incident")
@Table(name = "T_05_01_01")
public class Incident extends GenericModel {

    @Schema(description = "Unique incident code", example = "INC-2026-0012")
    @Column(name = "F_01", length = 50, nullable = false, unique = true)
    private String code;

    @Schema(description = "Short title of the incident", example = "Pressure drop – GZ1 Seg 3")
    @Column(name = "F_02", length = 200)
    private String title;

    @Schema(description = "Full description of the incident")
    @Column(name = "F_03", length = 4000)
    private String description;

    @Schema(description = "Timestamp when the incident occurred")
    @Column(name = "F_04", nullable = false)
    private LocalDateTime occurredAt;

    @Schema(description = "Timestamp when the incident was resolved (null if ongoing)")
    @Column(name = "F_05")
    private LocalDateTime resolvedAt;

    @Schema(description = "Whether the incident is still active")
    @Column(name = "F_06")
    private Boolean active;

    @Schema(description = "FK to the severity reference")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_05_01_01_FK_01"))
    private IncidentSeverity severity;

    @Schema(description = "FK to the affected pipeline segment")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_05_01_01_FK_02"))
    private PipelineSegment pipelineSegment;
}

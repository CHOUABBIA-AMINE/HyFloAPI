/**
 * 
 * 	@Author		: HyFlo v2 Model
 *
 * 	@Name		: Incident
 * 	@CreatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Crisis
 *
 **/

package dz.sh.trc.hyflo.crisis.model;

import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.common.model.EventStatus;
import dz.sh.trc.hyflo.flow.common.model.Severity;
import dz.sh.trc.hyflo.flow.core.model.FlowAlert;
import dz.sh.trc.hyflo.flow.core.model.FlowEvent;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
import dz.sh.trc.hyflo.network.core.model.PipelineSegment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Crisis or incident aggregate linking flow events, alerts, assets, and severity.
 */
@Schema(description = "Crisis or incident aggregate linking flow events, alerts, assets, and severity")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Incident")
@Table(name = "T_03_05_01")
public class Incident extends GenericModel {

    @Schema(
            description = "Human-readable unique incident code",
            example = "INC-2026-0001",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 50
    )
    @NotBlank(message = "Incident code is mandatory")
    @Size(max = 50, message = "Incident code must not exceed 50 characters")
    @Column(name = "F_01", length = 50, nullable = false)
    private String code;

    @Schema(
            description = "Short title describing the incident",
            example = "Pressure spike and emergency shutdown on pipeline P-101",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 200
    )
    @NotBlank(message = "Title is mandatory")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    @Column(name = "F_02", length = 200, nullable = false)
    private String title;

    @Schema(
            description = "Detailed description of the incident",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_03", columnDefinition = "TEXT")
    private String description;

    @Schema(
            description = "Severity of this incident",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_04", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_05_01_FK_01"))
    private Severity severity;

    @Schema(
            description = "Operational status of this incident (e.g., OPEN, UNDER_INVESTIGATION, CLOSED)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_05", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_05_01_FK_02"))
    private EventStatus status;

    @Schema(
            description = "Primary infrastructure asset involved in this incident",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_06", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_05_01_FK_03"))
    private Infrastructure infrastructure;

    @Schema(
            description = "Primary pipeline segment involved in this incident",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_07", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_05_01_FK_04"))
    private PipelineSegment pipelineSegment;

    @Schema(
            description = "Flow event that triggered or represents this incident",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_05_01_FK_05"))
    private FlowEvent rootEvent;

    @Schema(
            description = "Flow alert associated with this incident's origin",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_09", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_05_01_FK_06"))
    private FlowAlert rootAlert;

    @Schema(
            description = "Timestamp when this incident started",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_10", nullable = false)
    private LocalDateTime startedAt;

    @Schema(
            description = "Timestamp when this incident ended",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_11")
    private LocalDateTime endedAt;

    @Schema(
            description = "Indicates whether this incident is still active",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_12", nullable = false)
    private Boolean active = Boolean.TRUE;
}

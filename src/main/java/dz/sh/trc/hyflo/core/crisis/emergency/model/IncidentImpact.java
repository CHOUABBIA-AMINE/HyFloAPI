/**
 * 
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: IncidentImpact
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 *  @Package    : Crisis / Emergency
 *
 **/

package dz.sh.trc.hyflo.core.crisis.emergency.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import dz.sh.trc.hyflo.core.network.topology.model.Pipeline;
import dz.sh.trc.hyflo.core.network.topology.model.PipelineSegment;
import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Structured impact analysis for an incident (affected assets, losses, downtime).
 */
@Schema(description = "Structured impact analysis for an incident")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "IncidentImpact")
@Table(name = "T_05_01_02")
public class IncidentImpact extends GenericModel {

    @Schema(
            description = "Incident this impact record belongs to",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_01", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_05_01_02_FK_01"), nullable = false)
    private Incident incident;

    @Schema(
            description = "Pipelines affected by this incident",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "R_T050102_T020308",
            joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00",
                    foreignKey = @ForeignKey(name = "R_T050102_T020308_FK_01")),
            inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00",
                    foreignKey = @ForeignKey(name = "R_T050102_T020308_FK_02")),
            uniqueConstraints = @UniqueConstraint(name = "R_T050102_T020308_UK_01",
                    columnNames = {"F_01", "F_02"})
    )
    private Set<Pipeline> affectedPipelines = new HashSet<>();

    @Schema(
            description = "Pipeline segments affected by this incident",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "R_T050102_T020309",
            joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00",
                    foreignKey = @ForeignKey(name = "R_T050102_T020309_FK_01")),
            inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00",
                    foreignKey = @ForeignKey(name = "R_T050102_T020309_FK_02")),
            uniqueConstraints = @UniqueConstraint(name = "R_T050102_T020309_UK_01",
                    columnNames = {"F_01", "F_02"})
    )
    private Set<PipelineSegment> affectedSegments = new HashSet<>();

    @Schema(
            description = "Estimated economic loss associated with this incident (in local currency)",
            example = "1500000.00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_05", precision = 18, scale = 2)
    private BigDecimal estimatedLoss;

    @Schema(
            description = "Total downtime in minutes caused by this incident",
            example = "180",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_06")
    private Long downtimeMinutes;

    @Schema(
            description = "Impact level classification (e.g., LOW, MEDIUM, HIGH, CRITICAL)",
            example = "CRITICAL",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 20
    )
    @Column(name = "F_07", length = 20)
    private String impactLevel;
}

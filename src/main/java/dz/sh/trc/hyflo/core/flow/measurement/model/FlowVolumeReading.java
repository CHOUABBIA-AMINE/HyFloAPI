/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: FlowVolumeReading
 * 	@CreatedOn	: 01-21-2026
 * 	@UpdatedOn	: 03-25-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Measurement
 *
 **/

package dz.sh.trc.hyflo.core.flow.measurement.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import dz.sh.trc.hyflo.core.flow.reference.model.ValidationStatus;
import dz.sh.trc.hyflo.core.flow.type.model.OperationType;
import dz.sh.trc.hyflo.core.flow.workflow.model.WorkflowInstance;
import dz.sh.trc.hyflo.core.general.organization.model.Employee;
import dz.sh.trc.hyflo.core.network.common.model.Product;
import dz.sh.trc.hyflo.core.network.topology.model.Infrastructure;
import dz.sh.trc.hyflo.platform.kernel.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Daily flow volume reading recording hydrocarbon movements.
 * Represents production input, transportation, or consumption output operations.
 */
@Schema(description = "Daily flow volume reading tracking hydrocarbon movements (production, transport, consumption)")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FlowVolumeReading")
@Table(
        name = "T_03_04_01",
        indexes = {
                @Index(name = "T_03_04_01_IX_01", columnList = "F_01"),
                @Index(name = "T_03_04_01_IX_02", columnList = "F_06"),
                @Index(name = "T_03_04_01_IX_03", columnList = "F_08")
        },
        uniqueConstraints = @UniqueConstraint(
                name = "T_03_04_01_UK_01",
                columnNames = {"F_01", "F_06", "F_08", "F_09"}
        )
)
public class FlowVolumeReading extends GenericModel {

    @Schema(
            description = "Date of the flow volume reading",
            example = "2026-01-22",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_01", nullable = false)
    private LocalDate readingDate;

    @Schema(
            description = "Volume of product moved (in cubic meters or barrels)",
            example = "25000.50",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_02", precision = 15, scale = 4, nullable = false)
    private BigDecimal volume;

    @Schema(
            description = "Timestamp when this reading was recorded",
            example = "2026-01-22T08:30:00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_03")
    private LocalDateTime recordedAt;

    @Schema(
            description = "Timestamp when this reading was validated by supervisor",
            example = "2026-01-22T08:30:00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Column(name = "F_04")
    private LocalDateTime validatedAt;

    @Schema(
            description = "Additional notes or comments about this reading",
            example = "Normal reading, no anomalies detected",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            maxLength = 500
    )
    @Column(name = "F_05", length = 500)
    private String notes;

    @Schema(
            description = "Infrastructure where this reading occurred (facility, station, terminal)",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_06", foreignKey = @ForeignKey(name = "T_03_04_01_FK_01"), nullable = false)
    private Infrastructure infrastructure;

    @Schema(
            description = "Product type being moved (crude oil, natural gas, condensate, etc.)",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_07", foreignKey = @ForeignKey(name = "T_03_04_01_FK_02"), nullable = false)
    private Product product;

    @Schema(
            description = "Type of reading (production input, transport, consumption output)",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", foreignKey = @ForeignKey(name = "T_03_04_01_FK_03"), nullable = false)
    private OperationType type;

    @Schema(
            description = "Employee who recorded this reading",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_09", foreignKey = @ForeignKey(name = "T_03_04_01_FK_04"), nullable = false)
    private Employee recordedBy;

    @Schema(
            description = "Supervisor who validated this reading",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_10", foreignKey = @ForeignKey(name = "T_03_04_01_FK_05"))
    private Employee validatedBy;

    @Schema(
            description = "Current validation status (pending, approved, rejected)",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_11", foreignKey = @ForeignKey(name = "T_03_04_01_FK_06"), nullable = false)
    private ValidationStatus validationStatus;

    @Schema(
            description = "FK to the workflow instance governing the validation lifecycle of this reading. " +
                    "Null when no workflow process has been opened yet.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_12", referencedColumnName = "F_00",
            foreignKey = @ForeignKey(name = "T_03_04_01_FK_07"))
    private WorkflowInstance workflowInstance;
}

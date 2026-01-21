/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowReading
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="FlowReading")
@Table(name="T_03_03_03", indexes = {@Index(name="T_03_03_03_IX_01", columnList="F_01"),
        							 @Index(name="T_03_03_03_IX_02", columnList="F_11,F_01")},
    					  uniqueConstraints = {@UniqueConstraint(name="T_03_03_03_UK_01", columnNames={"F_14", "F_02", "F_01"})})
public class FlowReading extends GenericModel {
    
    @NotNull(message = "Recording timestamp is required")
    @PastOrPresent(message = "Recording time cannot be in the future")
    @Column(name = "F_01", nullable = false)
    private LocalDateTime recordedAt;
    
    @PositiveOrZero(message = "Pressure must be zero or positive")
    @DecimalMin(value = "0.0", message = "Pressure cannot be negative")
    @DecimalMax(value = "500.0", message = "Pressure exceeds maximum safe operating limit (500 bar)")
    @Column(name = "F_02", precision = 12, scale = 2)
    private Double pressure;
    
    @DecimalMin(value = "-50.0", message = "Temperature below minimum operating range")
    @DecimalMax(value = "200.0", message = "Temperature exceeds maximum operating range")
    @Column(name = "F_03", precision = 12, scale = 2)
    private Double temperature;
    
    @PositiveOrZero(message = "Flow rate must be zero or positive")
    @Column(name = "F_04", precision = 12, scale = 2)
    private Double flowRate;
    
    @PositiveOrZero(message = "Flow rate must be zero or positive")
    @Column(name = "F_05", precision = 12, scale = 2)
    private Double containedVolume;
    
    @PastOrPresent(message = "Validation time cannot be in the future")
    @Column(name = "F_06")
    private LocalDateTime validatedAt;
    
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    @Column(name = "F_07", length = 500)
    private String notes;
    
	@Schema(description = "Employee who recorded this reading", required = true)
    @NotNull(message = "Recording employee is required")
    @ManyToOne
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_03_FK_01"), nullable = false)
    private Employee recordedBy;
    
	@Schema(description = "Supervisor who validated this reading")
    @ManyToOne
    @JoinColumn(name = "F_09", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_03_FK_02"))
    private Employee validatedBy;
    
	@Schema(description = "Current validation status of this reading", required = true)
    @NotNull(message = "Validation status is required")
    @ManyToOne
    @JoinColumn(name = "F_10", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_03_FK_03"), nullable = false)
    private ValidationStatus validationStatus;
    
	@Schema(description = "Pipeline where this reading was taken", required = true)
    @NotNull(message = "Pipeline reference is required")
    @ManyToOne
    @JoinColumn(name = "F_11", referencedColumnName = "F_00", foreignKey = @ForeignKey(name = "T_03_03_03_FK_04"), nullable = false)
    private Pipeline pipeline;
}

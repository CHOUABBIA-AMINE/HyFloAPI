/**
 *
 * 	@Author: MEDJERAB Abir
 *
 * 	@Name: FlowOperation
 * 	@CreatedOn: 01-21-2026
 * 	@UpdatedOn: 01-21-2026
 *
 * 	@Type: Class
 * 	@Layer: Model
 * 	@Package: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.type.model.OperationType;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Daily flow operation tracking produced, transported, and consumed volumes with validation workflow")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity(name = "FlowOperation")
@Table(name = "T_03_03_03", indexes = {@Index(name = "T_03_03_03_IX_01", columnList = "F_01"),
        							   @Index(name = "T_03_03_03_IX_02", columnList = "F_05"),
        							   @Index(name = "T_03_03_03_IX_03", columnList = "F_07")},
    						uniqueConstraints = @UniqueConstraint(name = "T_03_03_03_UK_01", columnNames = {"F_01", "F_05", "F_06", "F_07"}))
public class FlowOperation extends GenericModel {
    
	@Schema(description = "Date of the operation", example = "2026-01-21", required = true)
    @NotNull(message = "Operation date is required")
    @PastOrPresent(message = "Operation date cannot be in the future")
    @Column(name = "F_01", nullable = false)
    private LocalDate date;
    
    @Schema(description = "Volume in cubic meters (m³)", example = "125000.50", required = true, minimum = "0")
    @NotNull(message = "Volume is required")
    @DecimalMin(value = "0.0", message = "Volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    @Column(name = "F_02", nullable = false, precision = 15, scale = 2)
    private Double volume;
    
    @Schema(description = "Timestamp when this operation was validated", example = "2026-01-21T16:00:00")
    @PastOrPresent(message = "Validation time cannot be in the future")
    @Column(name = "F_03")
    private LocalDateTime validatedAt;
    
    @Schema(description = "Additional notes or remarks about this operation", example = "Production affected by scheduled maintenance")
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    @Column(name = "F_04", length = 500)
    private String notes;
    
    @Schema(description = "Infrastructure where the operation occurred", required = true)
    @NotNull(message = "Infrastructure is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_05", foreignKey = @ForeignKey(name = "T_03_03_03_FK_01"), nullable = false)
    private Infrastructure infrastructure;
    
    @Schema(description = "Product being handled in the operation", required = true)
    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_06", foreignKey = @ForeignKey(name = "T_03_03_03_FK_02"), nullable = false)
    private Product product;
    
    @Schema(description = "Type of operation: PRODUCED, TRANSPORTED, or CONSUMED", required = true)
    @NotNull(message = "Operation type is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_07", foreignKey = @ForeignKey(name = "T_03_03_03_FK_03"), nullable = false)
    private OperationType type;
    
    @Schema(description = "Employee who recorded this operation", required = true)
    @NotNull(message = "Recording employee is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", foreignKey = @ForeignKey(name = "T_03_03_03_FK_04"), nullable = false)
    private Employee recordedBy;
    
    @Schema(description = "Supervisor who validated this operation")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_09", foreignKey = @ForeignKey(name = "T_03_03_03_FK_05"))
    private Employee validatedBy;
    
    @Schema(description = "Current validation status of this operation", required = true)
    @NotNull(message = "Validation status is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_10", foreignKey = @ForeignKey(name = "T_03_03_03_FK_06"), nullable = false)
    private ValidationStatus validationStatus;
}

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

import java.time.LocalDate;
import java.time.LocalDateTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;
import dz.sh.trc.hyflo.flow.type.model.OperationType;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity(name = "FlowOperation")
@Table(name = "T_03_03_0-", indexes = {@Index(name = "T_03_03_0-_IX_01", columnList = "F_01"),
        							   @Index(name = "T_03_03_0-_IX_02", columnList = "F_05"),
        							   @Index(name = "T_03_03_0-_IX_03", columnList = "F_07")},
    						uniqueConstraints = @UniqueConstraint(name = "T_03_03_0-_UK_01", columnNames = {"F_01", "F_05", "F_06", "F_07"}))
public class FlowOperation extends GenericModel {
    
    @NotNull(message = "Operation date is required")
    @PastOrPresent(message = "Operation date cannot be in the future")
    @Column(name = "F_01", nullable = false)
    private LocalDate date;
    
    @NotNull(message = "Volume is required")
    @DecimalMin(value = "0.0", message = "Volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    @Column(name = "F_02", nullable = false, precision = 15, scale = 2)
    private Double volume;
    
    @PastOrPresent(message = "Validation time cannot be in the future")
    @Column(name = "F_03")
    private LocalDateTime validatedAt;
    
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    @Column(name = "F_04", length = 500)
    private String notes;
    
    @NotNull(message = "Infrastructure is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_05", foreignKey = @ForeignKey(name = "T_03_03_0-_FK_01"), nullable = false)
    private Infrastructure infrastructure;
    
    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_06", foreignKey = @ForeignKey(name = "T_03_03_0-_FK_02"), nullable = false)
    private Product product;
    
    @NotNull(message = "Operation type is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_07", foreignKey = @ForeignKey(name = "T_03_03_0-_FK_03"), nullable = false)
    private OperationType type;
    
    @NotNull(message = "Recording employee is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", foreignKey = @ForeignKey(name = "T_03_03_0-_FK_04"), nullable = false)
    private Employee recordedBy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_09", foreignKey = @ForeignKey(name = "T_03_03_0-_FK_05"))
    private Employee validatedBy;
    
    @NotNull(message = "Validation status is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_10", foreignKey = @ForeignKey(name = "T_03_03_0-_FK_06"), nullable = false)
    private ValidationStatus validationStatus;
}

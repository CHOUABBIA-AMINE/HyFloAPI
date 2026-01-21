/**
 *
 * 	@Author: MEDJERAB Abir
 *
 * 	@Name: FlowForecast
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

import dz.sh.trc.hyflo.configuration.template.GenericModel;
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
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "T_03_03_02", indexes = {@Index(name = "T_03_03_03_IX_01", columnList = "F_01"),
    		   						   @Index(name = "T_03_03_03_IX_02", columnList = "F_07")},
    						uniqueConstraints = @UniqueConstraint(name = "T_03_03_03_UK_01",columnNames = {"F_01", "F_07", "F_08", "F_09"}))
public class FlowForecast extends GenericModel {
    
    @NotNull(message = "Forecast date is required")
    @Future(message = "Forecast date must be in the future")
    @Column(name = "F_01", nullable = false)
    private LocalDate forecastDate;
    
    @NotNull(message = "Predicted volume is required")
    @DecimalMin(value = "0.0", message = "Predicted volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    @Column(name = "F_02", nullable = false, precision = 15, scale = 2)
    private Double predictedVolume;
    
    @DecimalMin(value = "0.0", message = "Adjusted volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    @Column(name = "F_03", precision = 15, scale = 2)
    private Double adjustedVolume;
    
    @DecimalMin(value = "0.0", message = "Actual volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    @Column(name = "F_04", precision = 15, scale = 2)
    private Double actualVolume;
    
    @DecimalMin(value = "0.0", message = "Accuracy cannot be negative")
    @DecimalMax(value = "100.0", message = "Accuracy cannot exceed 100%")
    @Digits(integer = 3, fraction = 4, message = "Accuracy must have at most 3 integer digits and 4 decimal places")
    @Column(name = "F_05", precision = 6, scale = 4)
    private Double accuracy;
    
    @Size(max = 500, message = "Adjustment notes cannot exceed 500 characters")
    @Column(name = "F_06", length = 500)
    private String adjustmentNotes;
    
    @NotNull(message = "Infrastructure is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_07", foreignKey = @ForeignKey(name = "T_03_03_02_FK_01"), nullable = false)
    private Infrastructure infrastructure;
    
    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", foreignKey = @ForeignKey(name = "T_03_03_02_FK_02"), nullable = false)
    private Product product;
    
    @NotNull(message = "Operation type is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_09", foreignKey = @ForeignKey(name = "T_03_03_02_FK_03"), nullable = false)
    private OperationType operationType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_10", foreignKey = @ForeignKey(name = "T_03_03_02_FK_04"))
    private Employee supervisor;
}

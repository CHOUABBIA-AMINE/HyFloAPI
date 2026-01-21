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

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.type.model.OperationType;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Schema(description = "Flow forecast entity for operational planning with predicted volumes, adjustments, and accuracy tracking")
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
    
	@Schema(description = "Date for which the forecast is made", example = "15-02-2026", required = true)
    @NotNull(message = "Forecast date is required")
    @Future(message = "Forecast date must be in the future")
    @Column(name = "F_01", nullable = false)
    private LocalDate forecastDate;
    
	@Schema(description = "Predicted volume in cubic meters (m³)", example = "128000.00", required = true, minimum = "0")
    @NotNull(message = "Predicted volume is required")
    @DecimalMin(value = "0.0", message = "Predicted volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    @Column(name = "F_02", nullable = false, precision = 15, scale = 2)
    private Double predictedVolume;
    
	@Schema(description = "Adjusted volume after supervisor review in m³", example = "125000.00", minimum = "0")
    @DecimalMin(value = "0.0", message = "Adjusted volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    @Column(name = "F_03", precision = 15, scale = 2)
    private Double adjustedVolume;
    
	@Schema(description = "Actual volume from FlowOperation once date arrives in m³", example = "124500.75", minimum = "0")
    @DecimalMin(value = "0.0", message = "Actual volume cannot be negative")
    @Digits(integer = 13, fraction = 2, message = "Volume must have at most 13 integer digits and 2 decimal places")
    @Column(name = "F_04", precision = 15, scale = 2)
    private Double actualVolume;
    
	@Schema(description = "Forecast accuracy percentage: (1 - |actual-predicted|/predicted) × 100", example = "97.6800", minimum = "0", maximum = "100")
    @DecimalMin(value = "0.0", message = "Accuracy cannot be negative")
    @DecimalMax(value = "100.0", message = "Accuracy cannot exceed 100%")
    @Digits(integer = 3, fraction = 4, message = "Accuracy must have at most 3 integer digits and 4 decimal places")
    @Column(name = "F_05", precision = 6, scale = 4)
    private Double accuracy;
    
    @Schema(description = "Notes explaining forecast adjustments", example = "Adjusted down due to scheduled maintenance on production unit")
    @Size(max = 500, message = "Adjustment notes cannot exceed 500 characters")
    @Column(name = "F_06", length = 500)
    private String adjustmentNotes;
    
	@Schema(description = "Infrastructure for which forecast is made", required = true)
    @NotNull(message = "Infrastructure is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_07", foreignKey = @ForeignKey(name = "T_03_03_02_FK_01"), nullable = false)
    private Infrastructure infrastructure;
    
	@Schema(description = "Product being forecasted", required = true)
    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_08", foreignKey = @ForeignKey(name = "T_03_03_02_FK_02"), nullable = false)
    private Product product;
    
	@Schema(description = "Type of operation being forecasted: PRODUCED, TRANSPORTED, or CONSUMED", required = true)
    @NotNull(message = "Operation type is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_09", foreignKey = @ForeignKey(name = "T_03_03_02_FK_03"), nullable = false)
    private OperationType operationType;
    
	@Schema(description = "Supervisor who adjusted the forecast")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_10", foreignKey = @ForeignKey(name = "T_03_03_02_FK_04"))
    private Employee supervisor;
}

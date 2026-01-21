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
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

/**
 * FlowForecast - Operational forecasting and planning
 * Tracks predicted vs actual volumes with accuracy metrics
 * Supports planning for PRODUCED, TRANSPORTED, CONSUMED operations
 */
@Entity
@Table(name = "T_03_04_01_FlowForecast",
       indexes = {
           @Index(name = "idx_forecast_date", columnList = "F_01"),
           @Index(name = "idx_forecast_infra", columnList = "F_02")
       },
       uniqueConstraints = @UniqueConstraint(
           name = "uk_forecast_daily",
           columnNames = {"F_01", "F_02", "F_03", "F_04"}
       ))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FlowForecast extends GenericModel {
    
    @Column(name = "F_01", nullable = false)
    private LocalDate forecastDate;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_02", nullable = false, foreignKey = @ForeignKey(name = "fk_forecast_infra"))
    @NotNull
    private Infrastructure infrastructure;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_03", nullable = false, foreignKey = @ForeignKey(name = "fk_forecast_product"))
    @NotNull
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "F_04", nullable = false, foreignKey = @ForeignKey(name = "fk_forecast_type"))
    @NotNull
    private OperationType type;  // PRODUCED, TRANSPORTED, CONSUMED
    
    @Column(name = "F_05", nullable = false, precision = 15, scale = 2)
    @DecimalMin("0.0")
    private Double predictedVolume;  // m³
    
    @Column(name = "F_06", precision = 15, scale = 2)
    @DecimalMin("0.0")
    private Double adjustedVolume;   // After supervisor adjustment
    
    @Column(name = "F_07", precision = 15, scale = 2)
    @DecimalMin("0.0")
    private Double actualVolume;     // Populated from FlowOperation
    
    @Column(name = "F_08", precision = 6, scale = 4)
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private Double accuracy;         // Auto-calculated: (1 - |actual-predicted|/predicted) * 100
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "F_09", foreignKey = @ForeignKey(name = "fk_forecast_supervisor"))
    private Employee supervisor;
    
    @Column(name = "F_10", length = 500)
    private String adjustmentNotes;
}

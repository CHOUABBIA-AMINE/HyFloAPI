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
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.model;

import java.time.LocalDateTime;
import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.general.organization.model.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="FlowReading")
@Table(name="T_02_04_10", 
    indexes = {
        @Index(name="T_02_04_10_IDX_01", columnList="F_01"),
        @Index(name="T_02_04_10_IDX_02", columnList="F_08"),
        @Index(name="T_02_04_10_IDX_03", columnList="F_14,F_01"),
        @Index(name="T_02_04_10_IDX_04", columnList="F_12,F_08")
    },
    uniqueConstraints = {
        @UniqueConstraint(name="T_02_04_10_UK_01", 
                         columnNames={"F_14", "F_02", "F_01"})
    })
public class FlowReading extends GenericModel {

    @Column(name="F_01", nullable=false)
    @NotNull(message = "Reading timestamp is required")
    private LocalDateTime readingTimestamp;
    
    @ManyToOne
    @JoinColumn(name="F_02", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_02_04_10_FK_05"), nullable=false)
    @NotNull(message = "Measurement type is required")
    private MeasurementType measurementType;
    
    @Column(name="F_03", nullable=true)
    @DecimalMin(value = "0.0", message = "Flow rate must be positive")
    private Double flowRate;
    
    @Column(name="F_04", nullable=true)
    @DecimalMin(value = "0.0", message = "Pressure must be positive")
    private Double pressure;
    
    @Column(name="F_05", nullable=true)
    @DecimalMin(value = "-50.0", message = "Temperature must be above -50°C")
    @DecimalMax(value = "200.0", message = "Temperature must be below 200°C")
    private Double temperature;
    
    @Column(name="F_06", nullable=true)
    @DecimalMin(value = "0.0", message = "Density must be positive")
    private Double density;
    
    @Column(name="F_07", nullable=true)
    @DecimalMin(value = "0.0", message = "Volume must be positive")
    private Double cumulativeVolume;
    
    @ManyToOne
    @JoinColumn(name="F_08", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_02_04_10_FK_06"), nullable=false)
    @NotNull(message = "Validation status is required")
    private ValidationStatus validationStatus;
    
    @ManyToOne
    @JoinColumn(name="F_09", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_02_04_10_FK_07"), nullable=true)
    private QualityFlag qualityFlag;
    
    @Column(name="F_10", nullable=true, length=1000)
    private String notes;
    
    @Column(name="F_11", nullable=true)
    private LocalDateTime validatedAt;
    
    @ManyToOne
    @JoinColumn(name="F_12", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_02_04_10_FK_01"), nullable=false)
    @NotNull(message = "Operator (Employee) is required")
    private Employee recordedBy;
    
    @ManyToOne
    @JoinColumn(name="F_13", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_02_04_10_FK_02"), nullable=true)
    private Employee validatedBy;
    
    @ManyToOne
    @JoinColumn(name="F_14", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_02_04_10_FK_03"), nullable=false)
    @NotNull(message = "Infrastructure is required")
    private Infrastructure infrastructure;
    
    @ManyToOne
    @JoinColumn(name="F_15", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_02_04_10_FK_04"), nullable=false)
    @NotNull(message = "Product is required")
    private Product product;
    
    @ManyToOne
    @JoinColumn(name="F_16", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_02_04_10_FK_08"), nullable=true)
    private DataSource dataSource;
    
    @Column(name="F_17", nullable=true, length=100)
    private String scadaTagId;
}

/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowThreshold
 *	@CreatedOn	: 01-21-2026
 *	@UpdatedOn	: 01-21-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.flow.type.model.MeasurementType;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.network.core.model.Infrastructure;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
@Entity(name="FlowThreshold")
@Table(name="T_03_03_02",
    indexes = {
        @Index(name="T_03_03_02_IX_01", columnList="F_07"),
        @Index(name="T_03_03_02_IX_02", columnList="F_02,F_03")
    },
    uniqueConstraints = {
        @UniqueConstraint(name="T_03_03_02_UK_01", 
                         columnNames={"F_07", "F_02", "F_03"})
    })
public class FlowThreshold extends GenericModel {
    
    @Column(name="F_01", length=100, nullable=false)
    @NotBlank(message = "Threshold name is required")
    private String name;
    
    @ManyToOne
    @JoinColumn(name="F_02", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_03_03_02_FK_01"), nullable=false)
    @NotNull(message = "Measurement type is required")
    private MeasurementType measurementType;
    
    @Column(name="F_03", length=20, nullable=false)
    @NotBlank(message = "Parameter is required")
    @Pattern(regexp = "FLOW_RATE|PRESSURE|TEMPERATURE|DENSITY|VOLUME", 
             message = "Invalid parameter")
    private String parameter;
    
    @Column(name="F_04", nullable=true)
    private Double minValue;
    
    @Column(name="F_05", nullable=true)
    private Double maxValue;
    
    @Column(name="F_06", length=20, nullable=false)
    @NotBlank(message = "Severity is required")
    @Pattern(regexp = "INFO|WARNING|CRITICAL|EMERGENCY", 
             message = "Invalid severity")
    private String severity;
    
    @ManyToOne
    @JoinColumn(name="F_07", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_03_03_02_FK_02"), nullable=false)
    @NotNull(message = "Infrastructure is required")
    private Infrastructure infrastructure;
    
    @ManyToOne
    @JoinColumn(name="F_08", referencedColumnName="F_00", 
                foreignKey=@ForeignKey(name="T_03_03_02_FK_03"), nullable=true)
    private Product product;
    
    @Column(name="F_09", nullable=false)
    @NotNull(message = "Active status is required")
    private Boolean isActive;
    
    @Column(name="F_10", length=500, nullable=true)
    private String description;
    
    @Column(name="F_11", nullable=true)
    private Integer notificationDelayMinutes;
}

/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: FlowVolume
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.model;

import java.time.LocalDate;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.network.core.model.Pipeline;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Entity(name="FlowVolume")
@Table(name="T_02_04_02", uniqueConstraints = { @UniqueConstraint(name="T_02_04_02_UK_01", columnNames={"F_02", "F_03", "F_04"})})
public class FlowVolume extends GenericModel {

    @Column(name="F_01", nullable=false)
    private Double volume;
    
    @Column(name = "F_02", nullable = false)
    protected LocalDate measurementDate;

    @ManyToOne
    @JoinColumn(name="F_03", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_04_02_FK_01"), nullable=false)
    private MeasurementHour measurementHour;

    @ManyToOne
    @JoinColumn(name="F_04", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_04_02_FK_02"), nullable=false)
    private Pipeline pipeline;
    
}

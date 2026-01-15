/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Station
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import java.util.HashSet;
import java.util.Set;

import dz.sh.trc.hyflo.network.type.model.StationType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Entity(name="Station")
@Table(name="T_02_03_03")
public class Station extends Facility {

    @ManyToOne
    @JoinColumn(name="F_10", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_03_FK_01"), nullable=false)
    private StationType stationType;

    @ManyToOne
    @JoinColumn(name="F_11", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_03_FK_02"), nullable=true)
    private PipelineSystem pipelineSystem;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "R_T020303_T020308",
        joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020303_T020308_FK_01")),
        inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020303_T020308_FK_02")),
        uniqueConstraints = @UniqueConstraint(name = "R_T020303_T020308_UK_01", columnNames = {"F_01", "F_02"})
    )
    private Set<Pipeline> pipelines = new HashSet<>();
    
}

/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: HydrocarbonField
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

import dz.sh.trc.hyflo.network.common.model.Partner;
import dz.sh.trc.hyflo.network.common.model.Product;
import dz.sh.trc.hyflo.network.type.model.HydrocarbonFieldType;
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
@Entity(name="HydrocarbonField")
@Table(name="T_02_03_05")
public class HydrocarbonField extends Facility {

    @ManyToOne
    @JoinColumn(name="F_14", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_05_FK_01"), nullable=false)
    private HydrocarbonFieldType hydrocarbonFieldType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "R_T020305_T020307",
        joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020305_T020307_FK_01")),
        inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020305_T020307_FK_02")),
        uniqueConstraints = @UniqueConstraint(name = "R_T020305_T020307_UK_01", columnNames = {"F_01", "F_02"})
    )
    private Set<Pipeline> pipelines = new HashSet<>();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "R_T020305_T020204",
        joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020305_T020204_FK_01")),
        inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020305_T020204_FK_02")),
        uniqueConstraints = @UniqueConstraint(name = "R_T020305_T020204_UK_01", columnNames = {"F_01", "F_02"})
    )
    private Set<Partner> partners = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "R_T020305_T020201",
        joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020305_T020201_FK_01")),
        inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T020305_T020201_FK_02")),
        uniqueConstraints = @UniqueConstraint(name = "R_T020305_T020201_UK_01", columnNames = {"F_01", "F_02"})
    )
    private Set<Product> products = new HashSet<>();
    
}

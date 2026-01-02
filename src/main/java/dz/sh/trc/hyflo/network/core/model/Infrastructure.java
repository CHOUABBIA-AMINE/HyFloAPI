/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Infrastructure
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import java.time.LocalDate;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.general.organization.model.Region;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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
@Entity(name="Infrastructure")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="T_02_03_01", uniqueConstraints = { @UniqueConstraint(name="T_02_03_01_UK_01", columnNames={"F_01"}) })
public class Infrastructure extends GenericModel {

	@Column(name="F_01", length=20, nullable=false)
	protected String code;

	@Column(name="F_02", length=100, nullable=false)
	protected String name;
    
    @Column(name = "F_03", nullable = true)
    protected LocalDate installationDate;

    @Column(name = "F_04", nullable = true)
    protected LocalDate commissioningDate;

    @Column(name = "F_05", nullable = true)
    protected LocalDate decommissioningDate;
    
    @ManyToOne
    @JoinColumn(name = "F_06", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_01_FK_01"), nullable=false)
    protected OperationalStatus operationalStatus;
    
    @ManyToOne
    @JoinColumn(name = "F_07", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_01_FK_02"), nullable = true)
    private Region region;
    
}

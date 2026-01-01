/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Company
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network
 *
 **/


package dz.sh.trc.hyflo.network.common.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.network.type.model.PartnerType;
import dz.sh.trc.hyflo.system.localization.model.Country;
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
@Entity(name="Partner")
@Table(name="T_03_02_07", uniqueConstraints = { @UniqueConstraint(name="T_03_02_07_UK_01", columnNames={"F_02"})})
public class Partner extends GenericModel {

    @Column(name="F_01", length=100, nullable=true)
    private String name;

    @Column(name="F_02", length=20, nullable=false)
    private String shortName;

	@ManyToOne
    @JoinColumn(name = "F_03", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_02_07_FK_01"), nullable = false)
    private PartnerType partnerType;
	
	@ManyToOne
    @JoinColumn(name = "F_04", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_02_07_FK_02"), nullable = false)
    private Country country;
}
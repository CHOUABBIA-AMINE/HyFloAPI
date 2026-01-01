/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ItemDistribution
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-10-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Business / Plan
 *
 **/

package dz.mdn.iaas.business.plan.model;

import dz.mdn.iaas.common.administration.model.Structure;
import dz.mdn.iaas.configuration.template.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Entity(name="ItemDistribution")
@Table(name="T_02_02_09")
public class ItemDistribution extends GenericModel {
	
	@Column(name="F_01")
	private float quantity;
	
	@ManyToOne
    @JoinColumn(name="F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_02_09_FK_01"), nullable=false)
    private PlannedItem plannedItem;
	
	@ManyToOne
    @JoinColumn(name="F_03", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_02_09_FK_02"), nullable=false)
    private Structure structure;

}

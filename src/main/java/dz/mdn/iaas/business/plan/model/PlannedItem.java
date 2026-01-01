/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PlannedItem
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-10-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Business / Plan
 *
 **/

package dz.mdn.iaas.business.plan.model;

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
@Entity(name="PlannedItem")
@Table(name="T_02_02_08")
public class PlannedItem extends GenericModel {
	
	@Column(name="F_01", length=200, nullable=false)
	private String designation;
	
	@Column(name="F_02")
	private double unitairCost;
	
	@Column(name="F_03")
	private double planedQuantity;
	
	@Column(name="F_04")
	private double allocatedAmount;
	
	@ManyToOne
    @JoinColumn(name="F_05", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_02_08_FK_01"), nullable=false)
    private ItemStatus itemStatus;
	
	@ManyToOne
    @JoinColumn(name="F_06", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_02_08_FK_02"), nullable=false)
    private Item item;
	
	@ManyToOne
    @JoinColumn(name="F_07", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_02_08_FK_03"), nullable=false)
    private FinancialOperation financialOperation;

}

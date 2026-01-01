/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: Item
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-10-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Business / Plan
 *
 **/

package dz.sh.trc.hyflo.business.plan.model;

import java.util.HashSet;
import java.util.Set;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Entity(name="Item")
@Table(name="T_02_02_06", uniqueConstraints = { @UniqueConstraint(name = "T_02_02_06_UK_01", columnNames = { "F_02" })})
public class Item extends GenericModel {
	
	@Column(name="F_01", length=200)
	private String designationAr;

	@Column(name="F_02", length=200)
	private String designationEn;
	
	@Column(name="F_03", length=200, nullable=false)
	private String designationFr;
	
	@ManyToOne
    @JoinColumn(name="F_04", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_02_06_FK_01"), nullable=false)
    private Rubric rubric;
	
	@OneToMany(mappedBy="item")
    private Set<PlannedItem> plannedItems = new HashSet<PlannedItem>();

}

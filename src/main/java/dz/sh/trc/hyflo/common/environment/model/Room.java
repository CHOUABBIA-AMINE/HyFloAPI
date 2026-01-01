/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: Room
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Model
 *	@Layer		: Common / Environment
 *	@Package	: Common / Environment / Model
 *
 **/

package dz.sh.trc.hyflo.common.environment.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
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

/**
 * Room Entity - Extends GenericModel
 * Represents rooms on floors with multilingual designations
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Room")
@Table(name="T_01_01_03", uniqueConstraints = { @UniqueConstraint(name = "T_01_01_03_UK_01", columnNames = { "F_05", "F_04" })})
public class Room extends GenericModel {
	
	@Column(name="F_01", length=20, nullable=false)
	private String code;
	
	@Column(name="F_02", length=100)
	private String designationAr;
	
	@Column(name="F_03", length=100)
	private String designationEn;
	
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;
	
	@ManyToOne
    @JoinColumn(name="F_05", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_01_03_FK_01"), nullable=false)
    private Bloc bloc;
	
	@ManyToOne
    @JoinColumn(name="F_06", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_01_03_FK_02"), nullable=false)
    private Floor floor;
}

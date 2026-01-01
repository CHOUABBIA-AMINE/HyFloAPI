/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: ArchiveBox
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
 * ArchiveBox Entity - Extends GenericModel
 * Represents archive boxes with code and location on shelf floor
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="ArchiveBox")
@Table(name="T_01_01_06", uniqueConstraints = { @UniqueConstraint(name = "T_01_01_06_UK_01", columnNames = { "F_01" })})
public class ArchiveBox extends GenericModel {
	
	@Column(name="F_01", length=50, nullable=false)
	private String code;
	
	@Column(name="F_02", length=200)
	private String description;
	
	@ManyToOne
    @JoinColumn(name="F_03", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_01_06_FK_01"), nullable=false)
    private Shelf shelf;
	
	@ManyToOne
    @JoinColumn(name="F_04", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_01_06_FK_02"), nullable=false)
    private ShelfFloor shelfFloor;
}

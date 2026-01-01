/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: Folder
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Model
 *	@Layer		: Common / Environment
 *	@Package	: Common / Environment / Model
 *
 **/

package dz.sh.trc.hyflo.common.environment.model;

import java.util.List;

import dz.sh.trc.hyflo.common.communication.model.Mail;
import dz.sh.trc.hyflo.common.document.model.Document;
import dz.sh.trc.hyflo.configuration.template.GenericModel;
import jakarta.persistence.Column;
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

/**
 * Folder Entity - Extends GenericModel
 * Represents folders inside archive boxes
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Folder")
@Table(name="T_01_01_07", uniqueConstraints = { @UniqueConstraint(name = "T_01_01_07_UK_01", columnNames = { "F_01" })})
public class Folder extends GenericModel {
	
	@Column(name="F_01", length=50, nullable=false)
	private String code;
	
	@Column(name="F_02", length=200)
	private String description;
	
	@ManyToOne
    @JoinColumn(name="F_03", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_01_07_FK_01"), nullable=false)
    private ArchiveBox archiveBox;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "R_T010107_T010203", 
			joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T010107_T010203_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T010107_T010203_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T010107_T010203_UK_01", columnNames = {"F_01", "F_02"}))
	private List<Mail> mails;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "R_T010107_T010302", 
			joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T010107_T010302_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T010107_T010302_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T010107_T010302_UK_01", columnNames = {"F_01", "F_02"}))
	private List<Document> documents;
}

/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: Mail
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *	@Updated	: 12-30-2025 - Added cascade for referencedMails
 *	@Type		: Model
 *	@Layer		: Common / Communication
 *	@Package	: Common / Communication / Model
 *
 **/

package dz.sh.trc.hyflo.common.communication.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dz.sh.trc.hyflo.common.administration.model.Structure;
import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.system.utility.model.File;
import jakarta.persistence.CascadeType;
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
 * Mail Entity - Extends GenericModel
 * Represents mail correspondence with references and attachments
 */
@Setter
@Getter
@ToString(exclude = {"referencedMails"}) // Avoid circular toString issues
@EqualsAndHashCode(callSuper = true, exclude = {"referencedMails"}) // Avoid circular hashCode issues
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Mail")
@Table(name="T_01_02_03", uniqueConstraints = {@UniqueConstraint(name = "T_01_02_03_UK_01", columnNames = { "F_01" })})
public class Mail extends GenericModel {
	
	@Column(name="F_01", length=100)
	private String reference;
	
	@Column(name="F_02", length=50, nullable=true)
	private String recordNumber;
	
	@Column(name="F_03", length=500)
	private String subject;
	
	@Column(name="F_04")
	private Date mailDate;
	
	@Column(name="F_05")
	private Date recordDate;
	
	@ManyToOne
    @JoinColumn(name="F_06", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_02_03_FK_01"), nullable=false)
    private MailNature mailNature;
	
	@ManyToOne
    @JoinColumn(name="F_07", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_02_03_FK_02"), nullable=false)
    private MailType mailType;
	
	@ManyToOne
    @JoinColumn(name="F_08", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_02_03_FK_03"), nullable=false)
    private Structure structure;
	
	@ManyToOne
    @JoinColumn(name="F_09", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_02_03_FK_04"), nullable=false)
    private File file;
	
	/**
	 * Many-to-Many relationship for referenced mails
	 * Added cascade operations to ensure proper persistence
	 */
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name = "R_T010203_T010203", 
			joinColumns = @JoinColumn(name = "F_01", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T010203_T010203_FK_01")), 
			inverseJoinColumns = @JoinColumn(name = "F_02", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="R_T010203_T010203_FK_02")),
			uniqueConstraints = @UniqueConstraint(name = "R_T010203_T010203_UK_01", columnNames = {"F_01", "F_02"}))
	private List<Mail> referencedMails = new ArrayList<>();
}

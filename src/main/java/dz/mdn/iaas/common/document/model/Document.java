/**
 *	
 *	@author		: CHOUABBIA Amine
 *	@Name		: Document
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *	@Type		: Model
 *	@Layer		: Common / Document
 *	@Package	: Common / Document / Model
 *
 **/

package dz.mdn.iaas.common.document.model;

import java.util.Date;

import dz.mdn.iaas.configuration.template.GenericModel;
import dz.mdn.iaas.system.utility.model.File;
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
 * Document Entity - Extends GenericModel
 * Represents documents with type, reference, and file attachments
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Document")
@Table(name="T_01_03_02", uniqueConstraints = { 
    @UniqueConstraint(name = "T_01_03_02_UK_01", columnNames = { "F_01" })
})
public class Document extends GenericModel {
	
	@Column(name="F_01", length=50, nullable=false)
	private String reference;
	
	@Column(name="F_02", length=500)
	private String subject;
	
	@Column(name="F_03")
	private Date documentDate;
	
	@ManyToOne
    @JoinColumn(name="F_04", foreignKey=@ForeignKey(name="T_01_03_02_FK_01"), nullable=false)
    private DocumentType documentType;
	
	@ManyToOne
    @JoinColumn(name="F_05", foreignKey=@ForeignKey(name="T_01_03_02_FK_02"), nullable=false)
    private File file;
}

/**
 *	
 *	@author		: MEDJERAB ABIR
 *
 *	@Name		: OperationalStatus
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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * OperationalStatus Entity - Extends GenericModel
 * 
 * Database table: T_20_05
 * Primary key: F_00 (id) - inherited from GenericModel
 * 
 * Fields:
 * - F_00: id (inherited) - Primary key
 * - F_01: code - Status code (required)
 * - F_02: designation - Status designation (required)
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="OperationalStatus")
@Table(name="T_03_02_05", uniqueConstraints = { @UniqueConstraint(name="T_03_02_05_UK_01", columnNames={"F_01"}),
												@UniqueConstraint(name="T_03_02_05_UK_02", columnNames={"F_04"})})
public class OperationalStatus extends GenericModel {

    @Column(name="F_01", length=20, nullable=false)
    private String code;

    @Column(name="F_02", length=100)
    private String designationAr;

    @Column(name="F_03", length=100)
    private String designationEn;

    @Column(name="F_04", length=100, nullable=false)
    private String designationFr;
    
    @Column(name="F_05", length=200)
    private String descriptionAr;
    
    @Column(name="F_06", length=200)
    private String descriptionEn;
    
    @Column(name="F_07", length=200)
    private String descriptionFr;
}

/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: VendorType
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Type
 *
 **/


package dz.sh.trc.hyflo.network.type.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classification for vendors and contractors providing goods and services.
 * Examples: Equipment Supplier, Construction Contractor, Maintenance Provider, Engineering Consultant.
 */
@Schema(description = "Type classification for vendors, suppliers, and contractors")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity(name="VendorType")
@Table(name="T_02_01_03")
@PrimaryKeyJoinColumn(name = "F_00", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_01_03_FK_00"))
public class VendorType extends CompanyType {

}

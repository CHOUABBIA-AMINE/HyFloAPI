/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PartnerType
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Type
 *
 **/


package dz.sh.trc.hyflo.core.network.type.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classification for business partners in joint ventures and production sharing.
 * Examples: Joint Venture Partner, Production Sharing Agreement Partner, Technical Partner.
 */
@Schema(description = "Type classification for business partners in joint ventures and collaborative operations")
@Setter
@Getter
@NoArgsConstructor
@Entity(name="PartnerType")
@Table(name="T_02_01_02")
@PrimaryKeyJoinColumn(name = "F_00", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_01_02_FK_00"))
public class PartnerType extends CompanyType {

}

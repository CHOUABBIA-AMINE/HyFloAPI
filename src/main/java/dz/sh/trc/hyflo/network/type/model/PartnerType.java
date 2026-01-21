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


package dz.sh.trc.hyflo.network.type.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classification for business partners in joint ventures and production sharing.
 * Examples: Joint Venture Partner, Production Sharing Agreement Partner, Technical Partner.
 */
@Schema(description = "Type classification for business partners in joint ventures and collaborative operations")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity(name="PartnerType")
@Table(name="T_02_01_02")
public class PartnerType extends CompanyType {

}

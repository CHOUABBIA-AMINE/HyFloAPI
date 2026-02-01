/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ProcessingPlantType
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
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classification for processing plant types.
 * Examples: Refinery, Gas Treatment Plant, Separation Unit, Liquefaction Plant, Fractionation Plant.
 */
@Schema(description = "Type classification for processing plants (refinery, gas treatment, separation, liquefaction)")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity(name="ProcessingPlantType")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="T_02_01_08")
@PrimaryKeyJoinColumn(name = "F_00", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_01_08_FK_00"))
public class ProcessingPlantType extends FacilityType {

}
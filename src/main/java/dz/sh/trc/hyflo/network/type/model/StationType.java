/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: StationType
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
 * Classification for station types in the pipeline network.
 * Examples: Pumping Station, Compression Station, Metering Station, Regulating Station.
 */
@Schema(description = "Type classification for stations (pumping, compression, metering, etc.)")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity(name="StationType")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="T_02_01_05")
@PrimaryKeyJoinColumn(name = "F_00", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_01_05_FK_00"))
public class StationType extends FacilityType {

}
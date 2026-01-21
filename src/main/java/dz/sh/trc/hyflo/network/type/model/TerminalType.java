/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: TerminalType
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
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Classification for terminal types in the pipeline network.
 * Examples: Export Terminal, Import Terminal, Storage Terminal, Distribution Terminal, Marine Terminal.
 */
@Schema(description = "Type classification for terminals (export, import, storage, distribution, marine)")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity(name="TerminalType")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="T_02_01_06")
public class TerminalType extends FacilityType {

}
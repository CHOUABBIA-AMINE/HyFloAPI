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

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity(name="StationType")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="T_02_01_05")
public class StationType extends FacilityType {

}

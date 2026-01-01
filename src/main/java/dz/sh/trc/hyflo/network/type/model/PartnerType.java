/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: PartnerType
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network
 *
 **/


package dz.sh.trc.hyflo.network.type.model;

import jakarta.persistence.Entity;
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
@Entity(name="PartnerType")
@Table(name="T_03_01_02")
public class PartnerType extends CompanyType {

}
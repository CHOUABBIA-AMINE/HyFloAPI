/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Activity
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: General / Organization
 *
 **/

package dz.sh.trc.hyflo.general.organization.model;

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

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Activity")
@Table(name="T_01_03_01", uniqueConstraints = { @UniqueConstraint(name="T_01_03_01_UK_01", columnNames={"F_01"}),
												@UniqueConstraint(name="T_01_03_01_UK_02", columnNames={"F_04"})})

public class Activity extends GenericModel {
    
    @Column(name="F_01", length=10, nullable=false)
    private String code;

	@Column(name="F_02", length=100)
	private String designationAr;

	@Column(name="F_03", length=100)
	private String designationEn;
	
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;
}

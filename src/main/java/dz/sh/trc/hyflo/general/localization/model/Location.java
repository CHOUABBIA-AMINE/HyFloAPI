/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Location
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity(name="Location")
@Table(name="T_01_02_05", uniqueConstraints = { @UniqueConstraint(name="T_01_02_05_UK_01", columnNames={"F_01"})})
public class Location extends GenericModel {

    @Column(name="F_01", length=10, nullable=false)
    private String code;

	@Column(name="F_02", length=100)
	private String designationAr;

	@Column(name="F_03", length=100)
	private String designationEn;
	
	@Column(name="F_04", length=100, nullable=false)
	private String designationFr;

    @Column(name="F_05", nullable=false)
    private Double latitude;

    @Column(name="F_06", nullable=false)
    private Double longitude;

	@Column(name = "F_07")
    private Double elevation;

    @ManyToOne
    @JoinColumn(name = "F_08", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_02_05_FK_01"), nullable = false)
    private Locality locality;

}

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
@Table(name="T_01_02_06")
public class Location extends GenericModel {

    @Column(name="F_01", nullable=false)
    private int sequence;

    @Column(name="F_02", length=50, nullable=false)
    private String placeName;

    @Column(name="F_03", nullable=false)
    private Double latitude;

    @Column(name="F_04", nullable=false)
    private Double longitude;

	@Column(name = "F_05")
    private Double elevation;

    @ManyToOne
    @JoinColumn(name = "F_06", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_01_02_06_FK_01"), nullable = true)
    private Locality locality;

}

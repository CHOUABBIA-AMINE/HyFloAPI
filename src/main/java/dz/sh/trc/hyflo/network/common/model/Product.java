/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: Product
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Common
 *
 **/

package dz.sh.trc.hyflo.network.common.model;

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
@Entity(name="Product")
@Table(name="T_02_02_01", uniqueConstraints = { @UniqueConstraint(name="T_02_02_01_UK_01", columnNames={"F_01"}),
												@UniqueConstraint(name="T_02_02_01_UK_01", columnNames={"F_04"})})
public class Product extends GenericModel {

    @Column(name="F_01", length=20, nullable=false)
    private String code;

    @Column(name="F_02", length=100, nullable=true)
    private String designationAr;

    @Column(name="F_03", length=100, nullable=true)
    private String designationEn;

    @Column(name="F_04", length=100, nullable=false)
    private String designationFr;

    @Column(name="F_05", nullable=false)
    private Double density;

    @Column(name="F_06", nullable=false)
    private Double viscosity;

    @Column(name="F_07", nullable=false)
    private Double flashPoint;

    @Column(name="F_08", nullable=false)
    private Double sulfurContent;

    @Column(name="F_09", nullable=false)
    private Boolean isHazardous;
}

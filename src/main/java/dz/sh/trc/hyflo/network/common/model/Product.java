/**
 *	
 *	@Author		: MEDJERAB ABIR
 *
 *	@Name		: Product
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network
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

/**
 * Product Entity - Extends GenericModel
 * 
 * Database table: T_03_02_04
 * Primary key: F_00 (id) - inherited from GenericModel
 * 
 * Fields:
 * - F_00: id (inherited) - Primary key
 * - F_01: name - Product name (required)
 * - F_02: code - Product code (unique, required)
 * - F_03: density - Density (required)
 * - F_04: viscosity - Viscosity (required)
 * - F_05: flashPoint - Flash point (required)
 * - F_06: sulfurContent - Sulfur content (required)
 * - F_07: isHazardous - Hazardous flag (required)
 * - F_08: description - Description (required)
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Product")
@Table(name="T_03_02_04", uniqueConstraints = { @UniqueConstraint(name="T_03_02_04_UK_01", columnNames={"F_01"}),
												@UniqueConstraint(name="T_03_02_04_UK_01", columnNames={"F_04"})})
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

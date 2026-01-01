/**
 *	
 *	@author		: MEDJERAB ABIR
 *
 *	@Name		: PipelineSystem
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network
 *
 **/

package dz.mdn.iaas.network.core.model;

import dz.mdn.iaas.configuration.template.GenericModel;
import dz.mdn.iaas.network.common.model.OperationalStatus;
import dz.mdn.iaas.network.common.model.Product;
import dz.mdn.iaas.network.common.model.Region;
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

/**
 * PipelineSystem Entity - Extends GenericModel
 * 
 * Database table: T_03_02_05
 * Primary key: F_00 (id) - inherited from GenericModel
 * 
 * Fields:
 * - F_00: id (inherited) - Primary key
 * - F_01: code - System code (unique, required)
 * - F_02: designationAr - Arabic designation (required)
 * - F_03: designationEn - English designation (required)
 * - F_04: designationFr - French designation (required)
 * - F_05: product - Product relationship (required)
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="PipelineSystem")
@Table(name="T_03_03_06", uniqueConstraints = { @UniqueConstraint(name="T_03_03_06_UK_01", columnNames={"F_01"}) })
public class PipelineSystem extends GenericModel {

    @Column(name="F_01", length=50, nullable=false)
    private String code;

    @Column(name="F_02", length=100, nullable=false)
    private String name;

    @ManyToOne
    @JoinColumn(name="F_03", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_06_FK_01"), nullable=false)
    private Product product;

    @ManyToOne
    @JoinColumn(name="F_04", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_06_FK_02"), nullable=false)
    private OperationalStatus operationalStatus;

    @ManyToOne
    @JoinColumn(name="F_05", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_03_06_FK_03"), nullable=false)
    private Region region;
}

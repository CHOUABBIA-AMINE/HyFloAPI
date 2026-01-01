/**
 *	
 *	@author		: MEDJERAB ABIR
 *
 *	@Name		: PipelineSegment
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network
 *
 **/

package dz.mdn.iaas.network.core.model;

import dz.mdn.iaas.network.common.model.Alloy;
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

/**
 * PipelineSegment Entity - Extends GenericModel
 * 
 * Database table: T_03_02_07
 * Primary key: F_00 (id) - inherited from GenericModel
 * 
 * Fields:
 * - F_00: id (inherited) - Primary key
 * - F_01: code - Segment code (required)
 * - F_02: pipeline - Pipeline relationship (required)
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="PipelineSegment")
@Table(name="T_03_03_08")
public class PipelineSegment extends Infrastructure {

	@Column(name="F_07", nullable=false)
    private Double diameter;

    @Column(name="F_08", nullable=false)
    private Double length;

    @Column(name="F_09", nullable=false)
    private Double thikness;

    @Column(name="F_10", nullable=false)
    private Double roughness;

    @Column(name="F_11", nullable=false)
    private Double startPoint;

    @Column(name="F_12", nullable=false)
    private Double endPoint;

    @ManyToOne
    @JoinColumn(name="F_13", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_02_09_FK_01"), nullable=true)
    private Alloy constructionMaterial;

    @ManyToOne
    @JoinColumn(name="F_14", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_02_09_FK_02"), nullable=true)
    private Alloy exteriorCoating;

    @ManyToOne
    @JoinColumn(name="F_15", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_02_09_FK_03"), nullable=true)
    private Alloy interiorCoating;

    @ManyToOne
    @JoinColumn(name="F_16", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_03_02_10_FK_01"), nullable=false)
    private Pipeline pipeline;
}

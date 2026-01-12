/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PipelineSegment
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import dz.sh.trc.hyflo.network.common.model.Alloy;
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
@Entity(name="PipelineSegment")
@Table(name="T_02_03_09")
public class PipelineSegment extends Infrastructure {

	@Column(name="F_08", nullable=false)
    private Double diameter;

    @Column(name="F_09", nullable=false)
    private Double length;

    @Column(name="F_10", nullable=false)
    private Double thickness;

    @Column(name="F_11", nullable=false)
    private Double roughness;

    @Column(name="F_12", nullable=false)
    private Double startPoint;

    @Column(name="F_13", nullable=false)
    private Double endPoint;

    @ManyToOne
    @JoinColumn(name="F_14", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_09_FK_01"), nullable=true)
    private Alloy constructionMaterial;

    @ManyToOne
    @JoinColumn(name="F_15", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_09_FK_02"), nullable=true)
    private Alloy exteriorCoating;

    @ManyToOne
    @JoinColumn(name="F_16", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_09_FK_03"), nullable=true)
    private Alloy interiorCoating;

    @ManyToOne
    @JoinColumn(name="F_17", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_09_FK_04"), nullable=false)
    private Pipeline pipeline;
    
}

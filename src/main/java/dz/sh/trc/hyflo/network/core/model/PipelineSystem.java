/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: PipelineSystem
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.general.organization.model.Region;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.common.model.Product;
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
@Entity(name="PipelineSystem")
@Table(name="T_02_03_06", uniqueConstraints = { @UniqueConstraint(name="T_02_03_06_UK_01", columnNames={"F_01"}) })
public class PipelineSystem extends GenericModel {

    @Column(name="F_01", length=50, nullable=false)
    private String code;

    @Column(name="F_02", length=100, nullable=false)
    private String name;

    @ManyToOne
    @JoinColumn(name="F_03", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_06_FK_01"), nullable=false)
    private Product product;

    @ManyToOne
    @JoinColumn(name="F_04", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_06_FK_02"), nullable=false)
    private OperationalStatus operationalStatus;

    @ManyToOne
    @JoinColumn(name="F_05", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_06_FK_03"), nullable=false)
    private Region region;
}

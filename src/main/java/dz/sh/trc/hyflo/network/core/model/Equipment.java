/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Equipment
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import java.time.LocalDate;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.network.common.model.OperationalStatus;
import dz.sh.trc.hyflo.network.common.model.Vendor;
import dz.sh.trc.hyflo.network.type.model.EquipmentType;
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
@Entity(name="Equipment")
@Table(name="T_02_03_10", uniqueConstraints = { @UniqueConstraint(name="T_02_03_10_UK_01", columnNames={"F_02"}) })
public class Equipment extends GenericModel {

    @Column(name="F_01", length=100, nullable=false)
    private String name;

    @Column(name="F_02", length=50, nullable=false)
    private String code;

    @Column(name="F_03", length=50, nullable=true)
    private String modelNumber;

    @Column(name="F_04", length=100, nullable=true)
    private String serialNumber;

    @Column(name="F_05", nullable=true)
    private LocalDate manufacturingDate;

    @Column(name="F_06", nullable=true)
    private LocalDate installationDate;

    @Column(name="F_07", nullable=true)
    private LocalDate commissioningDate;

    @Column(name="F_08", nullable=true)
    private LocalDate lastMaintenanceDate;

    @Column(name="F_09", nullable=true)
    private LocalDate decommissioningDate;

    @ManyToOne
    @JoinColumn(name="F_10", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_10_FK_01"), nullable=false)
    private OperationalStatus operationalStatus;

    @ManyToOne
    @JoinColumn(name="F_11", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_10_FK_02"), nullable=false)
    private EquipmentType equipmentType;

    @ManyToOne
    @JoinColumn(name="F_12", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_10_FK_03"), nullable=false)
    private Facility facility;

    @ManyToOne
    @JoinColumn(name="F_13", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_10_FK_04"), nullable=false)
    private Vendor manufacturer;
}

/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Facility
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.model;

import java.util.HashSet;
import java.util.Set;

import dz.sh.trc.hyflo.general.localization.model.Locality;
import dz.sh.trc.hyflo.network.common.model.Vendor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Facility")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="T_02_03_02")
public class Facility extends Infrastructure {
	
	@Column(name="F_08", length=100, nullable=false)
	private String placeName;

    @Column(name="F_09", nullable=false)
    private Double latitude;

    @Column(name="F_10", nullable=false)
    private Double longitude;

	@Column(name="F_11")
    private Double elevation;

	@ManyToOne
    @JoinColumn(name = "F_12", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_02_FK_01"), nullable = false)
    protected Vendor vendor;

    @ManyToOne
    @JoinColumn(name = "F_13", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_02_03_02_FK_02"), nullable = false)
    private Locality locality;
    
    @Builder.Default
    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL)
    protected Set<Equipment> equipments = new HashSet<>();

}

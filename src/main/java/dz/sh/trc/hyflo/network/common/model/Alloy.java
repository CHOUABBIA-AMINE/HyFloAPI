/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: Alloy
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
@Entity(name="Alloy")
@Table(name="T_02_02_03", uniqueConstraints = { @UniqueConstraint(name="T_02_02_03_UK_01", columnNames = "F_01"),
												@UniqueConstraint(name="T_02_02_03_UK_02", columnNames = "F_04")})
public class Alloy extends GenericModel {

	@Column(name="F_01", length=20, nullable=false)
    private String code;

    @Column(name="F_02", length=100)
    private String designationAr;

    @Column(name="F_03", length=100)
    private String designationEn;

    @Column(name="F_04", length=100, nullable=false)
    private String designationFr;
    
    @Column(name="F_05", length=200)
    private String descriptionAr;
    
    @Column(name="F_06", length=200)
    private String descriptionEn;
    
    @Column(name="F_07", length=200)
    private String descriptionFr;

    /*@OneToMany(mappedBy = "constructionMaterial", cascade = CascadeType.ALL)
    private Set<PipelineSegment> pipelineSegments = new HashSet<>();

    @OneToMany(mappedBy = "interiorCoating", cascade = CascadeType.ALL)
    private Set<PipelineSegment> segmentsInnerCaoted = new HashSet<>();

    @OneToMany(mappedBy = "exteriorCoating", cascade = CascadeType.ALL)
    private Set<PipelineSegment> segmentsOuterCaoted = new HashSet<>();*/
    
}
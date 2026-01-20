/**
 *	
 *	@Author		: CHOUABBIA-AMINE
 *
 *	@Name		: ValidationStatus
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="ValidationStatus")
@Table(name="T_02_04_12", 
    uniqueConstraints = {
        @UniqueConstraint(name="T_02_04_12_UK_01", columnNames={"F_01"})
    })
public class ValidationStatus extends GenericModel {
    
    @Column(name="F_01", length=50, nullable=false)
    @NotBlank(message = "Code is required")
    private String code;
    
    @Column(name="F_02", length=100, nullable=true)
    private String designationAr;
    
    @Column(name="F_03", length=100, nullable=true)
    private String designationEn;
    
    @Column(name="F_04", length=100, nullable=false)
    @NotBlank(message = "French designation is required")
    private String designationFr;
    
    @Column(name="F_05", length=255, nullable=true)
    private String descriptionAr;
    
    @Column(name="F_06", length=255, nullable=true)
    private String descriptionEn;
    
    @Column(name="F_07", length=255, nullable=true)
    private String descriptionFr;
}

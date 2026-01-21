/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: QualityFlag
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
@Entity(name = "QualityFlag")
@Table(name = "T_03_02_05", uniqueConstraints = {@UniqueConstraint(name="T_03_02_02_UK_01", columnNames={"F_01"})})
public class QualityFlag extends GenericModel {
    
    @NotBlank(message = "Quality flag code is required")
    @Size(min = 2, max = 50, message = "Code must be between 2 and 50 characters")
    @Column(name = "F_01", length = 50, nullable = false)
    private String code;
    
    @Size(max = 100, message = "Arabic designation cannot exceed 100 characters")
    @Column(name = "F_02", length = 100)
    private String designationAr;
    
    @Size(max = 100, message = "English designation cannot exceed 100 characters")
    @Column(name = "F_03", length = 100)
    private String designationEn;
    
    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation cannot exceed 100 characters")
    @Column(name = "F_04", length = 100, nullable = false)
    private String designationFr;
    
    @Size(max = 255, message = "Arabic description cannot exceed 255 characters")
    @Column(name = "F_05", length = 255)
    private String descriptionAr;
    
    @Size(max = 255, message = "English description cannot exceed 255 characters")
    @Column(name = "F_06", length = 255)
    private String descriptionEn;
    
    @Size(max = 255, message = "French description cannot exceed 255 characters")
    @Column(name = "F_07", length = 255)
    private String descriptionFr;
    
    @Pattern(regexp = "^(LOW|MEDIUM|HIGH|CRITICAL)$", message = "Severity level must be LOW, MEDIUM, HIGH, or CRITICAL")
    @Size(max = 20, message = "Severity level cannot exceed 20 characters")
    @Column(name = "F_08", length = 20)
    private String severityLevel;
}

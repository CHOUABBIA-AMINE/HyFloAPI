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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Schema(description = "Quality flag indicating data reliability, completeness, and validation status for flow measurements")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="QualityFlag")
@Table(name="T_03_02_02", uniqueConstraints = {@UniqueConstraint(name="T_03_02_02_UK_01", columnNames={"F_01"})})
public class QualityFlag extends GenericModel {
    
    @Schema(description = "Unique code for quality flag", example = "VERIFIED", required = true)
    @NotBlank(message = "Quality flag code is required")
    @Size(min = 2, max = 50, message = "Code must be between 2 and 50 characters")
    @Column(name = "F_01", length = 50, nullable = false)
    private String code;
    
    @Schema(description = "Quality flag designation in Arabic")
    @Size(max = 100, message = "Arabic designation cannot exceed 100 characters")
    @Column(name = "F_02", length = 100)
    private String designationAr;
    
    @Schema(description = "Quality flag designation in English", example = "Verified Data")
    @Size(max = 100, message = "English designation cannot exceed 100 characters")
    @Column(name = "F_03", length = 100)
    private String designationEn;
    
    @Schema(description = "Quality flag designation in French", example = "Données Vérifiées", required = true)
    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation cannot exceed 100 characters")
    @Column(name = "F_04", length = 100, nullable = false)
    private String designationFr;
    
    @Schema(description = "Detailed description in Arabic")
    @Size(max = 255, message = "Arabic description cannot exceed 255 characters")
    @Column(name = "F_05", length = 255)
    private String descriptionAr;
    
    @Schema(description = "Detailed description in English")
    @Size(max = 255, message = "English description cannot exceed 255 characters")
    @Column(name = "F_06", length = 255)
    private String descriptionEn;
    
    @Schema(description = "Detailed description in French")
    @Size(max = 255, message = "French description cannot exceed 255 characters")
    @Column(name = "F_07", length = 255)
    private String descriptionFr;
    
    @Schema(description = "Severity level for data quality issues", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH", "CRITICAL"})
    @Pattern(regexp = "^(LOW|MEDIUM|HIGH|CRITICAL)$", message = "Severity level must be LOW, MEDIUM, HIGH, or CRITICAL")
    @Size(max = 20, message = "Severity level cannot exceed 20 characters")
    @Column(name = "F_08", length = 20)
    private String severityLevel;
}

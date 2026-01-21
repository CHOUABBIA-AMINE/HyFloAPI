/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: DataSource
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
import jakarta.validation.constraints.Size;
import lombok.*;

@Schema(description = "Data source entity defining the origin of flow measurements (SCADA, manual entry, IoT sensor)")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "DataSource")
@Table(name = "T_03_02_01", uniqueConstraints = {@UniqueConstraint(name = "T_03_02_01_UK_01", columnNames = {"F_01"})})
public class DataSource extends GenericModel {
    
    @Schema(description = "Unique code identifying the data source", example = "SCADA_SYS", required = true)
    @NotBlank(message = "Data source code is required")
    @Size(min = 2, max = 50, message = "Code must be between 2 and 50 characters")
    @Column(name = "F_01", length = 50, nullable = false)
    private String code;
    
    @Schema(description = "Data source designation in Arabic", example = "نظام سكادا")
    @Size(max = 100, message = "Arabic designation cannot exceed 100 characters")
    @Column(name = "F_02", length = 100)
    private String designationAr;
    
    @Schema(description = "Data source designation in English", example = "SCADA System")
    @Size(max = 100, message = "English designation cannot exceed 100 characters")
    @Column(name = "F_03", length = 100)
    private String designationEn;
    
    @Schema(description = "Data source designation in French", example = "Système SCADA", required = true)
    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation cannot exceed 100 characters")
    @Column(name = "F_04", length = 100, nullable = false)
    private String designationFr;
    
    @Schema(description = "Detailed description in Arabic")
    @Size(max = 255, message = "Arabic description cannot exceed 255 characters")
    @Column(name = "F_05", length = 255)
    private String descriptionAr;
    
    @Schema(description = "Detailed description in English", example = "Supervisory Control and Data Acquisition system for real-time monitoring")
    @Size(max = 255, message = "English description cannot exceed 255 characters")
    @Column(name = "F_06", length = 255)
    private String descriptionEn;
    
    @Schema(description = "Detailed description in French")
    @Size(max = 255, message = "French description cannot exceed 255 characters")
    @Column(name = "F_07", length = 255)
    private String descriptionFr;
}


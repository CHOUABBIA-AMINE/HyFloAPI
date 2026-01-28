/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ReadingSlot
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-22-2026
 *
 *	@Type		: Class
 *	@Layer		: Model
 *	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.model;

import java.time.LocalTime;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "Validation status for flow data approval and quality control workflow")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ReadingSlot")
@Table(name = "T_03_02_07", uniqueConstraints = {@UniqueConstraint(name = "T_03_02_07_UK_01", columnNames = {"F_01", "F_02"})})
public class ReadingSlot extends GenericModel {
    
	@Schema(
    		description = "Slot Code",
    		requiredMode = Schema.RequiredMode.REQUIRED,
    		maxLength = 20
    )
    @NotBlank
    @Size(max = 20)
    @Column(name = "F_01", length = 20, nullable = false)
    private String code; // e.g., "SLOT_1", "SLOT_2"
    
    @Schema(
    		description = "Start time of the slot",
    		requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Column(name = "F_02", nullable = false)
    private LocalTime startTime; // e.g., 06:00
    
    @Schema(
    		description = "End time of the slot",
    		requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull
    @Column(name = "F_03", nullable = false)
    private LocalTime endTime; // e.g., 08:00
    
    @Schema(
    		description = "Designation in Arabic",
    		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
    		maxLength = 100
    )
    @Size(max = 100)
    @Column(name = "F_04", length = 100)
    private String designationAr;
    
    @Schema(
    		description = "Designation in French",
    		requiredMode = Schema.RequiredMode.NOT_REQUIRED,
    		maxLength = 100
    )
    @NotBlank
    @Size(max = 100)
    @Column(name = "F_05", length = 100)
    private String designationEn; // e.g., "Morning Early"
    
    @Schema(
    		description = "Designation in French",
    		requiredMode = Schema.RequiredMode.REQUIRED,
    		maxLength = 100
    )
    @Size(max = 100)
    @Column(name = "F_06", length = 100)
    private String designationFr;
    
    @Schema(
    		description = "optional or mandatory",
    		requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Column(name = "F_07")
    private Integer displayOrder; // For UI sorting
}

/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ReadingSlotDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026 - CRITICAL: Fixed validation types and added missing @NotBlank
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.common.model.ReadingSlot;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for ReadingSlot entity.
 * Used for API requests and responses related to time slot configuration.
 */
@Schema(description = "Reading slot DTO for configuring time-based data collection windows")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReadingSlotDTO extends GenericDTO<ReadingSlot> {

    @Schema(
        description = "Unique code identifying the reading slot",
        example = "SLOT_1",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20
    )
    @NotBlank(message = "Reading slot code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;
    
    @Schema(
        description = "Start time of the reading slot (HH:mm format)",
        example = "06:00",
        requiredMode = Schema.RequiredMode.REQUIRED,
        type = "string",
        format = "time"
    )
    @NotNull(message = "Reading slot start time is required")
    private LocalTime startTime;
    
    @Schema(
        description = "End time of the reading slot (HH:mm format)",
        example = "08:00",
        requiredMode = Schema.RequiredMode.REQUIRED,
        type = "string",
        format = "time"
    )
    @NotNull(message = "Reading slot end time is required")
    private LocalTime endTime;

    @Schema(
        description = "Reading slot designation in Arabic",
        example = "الفترة الصباحية المبكرة",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Schema(
        description = "Reading slot designation in English (required for system use)",
        example = "Morning Early",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "English designation is required")
    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @Schema(
        description = "Reading slot designation in French",
        example = "Matin Tôt",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;
    
    @Schema(
        description = "Display order for UI sorting (lower numbers appear first)",
        example = "1",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private Integer displayOrder;

    @Override
    public ReadingSlot toEntity() {
        ReadingSlot entity = new ReadingSlot();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setStartTime(this.startTime);
        entity.setEndTime(this.endTime);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setDisplayOrder(this.displayOrder);
        return entity;
    }

    @Override
    public void updateEntity(ReadingSlot entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.startTime != null) entity.setStartTime(this.startTime);
        if (this.endTime != null) entity.setEndTime(this.endTime);
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
        if (this.displayOrder != null) entity.setDisplayOrder(this.displayOrder);
    }

    /**
     * Converts a ReadingSlot entity to its DTO representation.
     *
     * @param entity the ReadingSlot entity to convert
     * @return ReadingSlotDTO or null if entity is null
     */
    public static ReadingSlotDTO fromEntity(ReadingSlot entity) {
        if (entity == null) return null;
        
        return ReadingSlotDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .displayOrder(entity.getDisplayOrder())
                .build();
    }
}

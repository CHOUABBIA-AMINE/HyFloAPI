/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: ReadingSlotDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Quality flag DTO for flow measurement quality assessment")
public class ReadingSlotDTO extends GenericDTO<ReadingSlot> {

    @NotBlank(message = "Reading Slot code is required")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
    @Schema(description = "Unique code identifying the reading slot", 
            example = "Slot:06:00-08:00", 
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 20)
    private String code;
    
    @NotBlank(message = "Reading Slot start time is required")
    @Schema(description = "identifying the reading slot start time", 
            example = "06:00", 
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 20)
    LocalTime startTime;
    
    @NotBlank(message = "Reading Slot start time is required")
    @Schema(description = "identifying the reading slot start time", 
            example = "08:00", 
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 20)
    LocalTime endTime;

    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    @Schema(description = "Quality flag designation in Arabic", 
            example = "جودة جيدة")
    private String designationAr;

    @Size(max = 100, message = "English designation must not exceed 100 characters")
    @Schema(description = "Quality flag designation in English", 
            example = "Good Quality",
            maxLength = 100)
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    @Schema(description = "Quality flag designation in French (required)", 
            example = "Bonne Qualité", 
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100)
    private String designationFr;
    
    @Schema(description = "Display Order for UI sorting", 
            example = "1, 2, ...")
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

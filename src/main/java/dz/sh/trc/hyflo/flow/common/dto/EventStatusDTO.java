/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: EventStatusDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.common.model.EventStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@Schema(description = "Event status DTO for operational event lifecycle tracking")
public class EventStatusDTO extends GenericDTO<EventStatus> {

    @NotBlank(message = "Event status code is required")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", 
             message = "Code must contain only uppercase letters, numbers, hyphens, and underscores")
    @Schema(description = "Unique code identifying the event status", 
            example = "IN_PROGRESS", 
            required = true,
            maxLength = 20)
    private String code;

    @Size(max = 50, message = "Arabic designation must not exceed 50 characters")
    @Schema(description = "Event status designation in Arabic", 
            example = "قيد التنفيذ")
    private String designationAr;

    @Size(max = 50, message = "English designation must not exceed 50 characters")
    @Schema(description = "Event status designation in English", 
            example = "In Progress",
            maxLength = 50)
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 50, message = "French designation must not exceed 50 characters")
    @Schema(description = "Event status designation in French (required)", 
            example = "En Cours", 
            required = true,
            maxLength = 50)
    private String designationFr;

    @Override
    public EventStatus toEntity() {
        EventStatus entity = new EventStatus();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        return entity;
    }

    @Override
    public void updateEntity(EventStatus entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
    }

    public static EventStatusDTO fromEntity(EventStatus entity) {
        if (entity == null) return null;
        
        return EventStatusDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .build();
    }
}

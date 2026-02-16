/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: EventTypeDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 02-16-2026 - Fixed validation and added comprehensive @Schema
 *
 * 	@Type		: Class
 * 	@Layer		: DTO
 * 	@Package	: Flow / Type
 *
 **/

package dz.sh.trc.hyflo.flow.type.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.flow.type.model.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for EventType entity.
 * Used for API requests and responses related to operational event type classification.
 */
@Schema(description = "Event type DTO for classifying operational activities and incidents in hydrocarbon infrastructure")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventTypeDTO extends GenericDTO<EventType> {

    @Schema(
        description = "Unique code identifying the event type (uppercase letters, numbers, hyphens, underscores)",
        example = "EMERGENCY_SHUTDOWN",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20
    )
    @NotBlank(message = "Event type code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", 
             message = "Code must contain only uppercase letters, numbers, hyphens, and underscores")
    private String code;

    @Schema(
        description = "Event type designation in Arabic",
        example = "إيقاف طارئ",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Schema(
        description = "Event type designation in French (required for system use)",
        example = "Arrêt d'Urgence",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;

    @Schema(
        description = "Event type designation in English",
        example = "Emergency Shutdown",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @Override
    public EventType toEntity() {
        EventType entity = new EventType();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationFr(this.designationFr);
        entity.setDesignationEn(this.designationEn);
        return entity;
    }

    @Override
    public void updateEntity(EventType entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
    }

    /**
     * Converts an EventType entity to its DTO representation.
     *
     * @param entity the EventType entity to convert
     * @return EventTypeDTO or null if entity is null
     */
    public static EventTypeDTO fromEntity(EventType entity) {
        if (entity == null) return null;
        
        return EventTypeDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .designationAr(entity.getDesignationAr())
                .designationFr(entity.getDesignationFr())
                .designationEn(entity.getDesignationEn())
                .build();
    }
}

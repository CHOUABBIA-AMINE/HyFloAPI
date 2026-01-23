/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: EventTypeDTO
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
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
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Event type DTO for classifying operational activities and incidents")
public class EventTypeDTO extends GenericDTO<EventType> {

    @NotBlank(message = "Event type code is required")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9_-]+$", 
             message = "Code must contain only uppercase letters, numbers, hyphens, and underscores")
    @Schema(description = "Unique code identifying the event type", 
            example = "EMERGENCY_SHUTDOWN", 
            required = true,
            maxLength = 20)
    private String code;

    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    @Schema(description = "Event type designation in Arabic", 
            example = "إيقاف طارئ")
    private String designationAr;

    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    @Schema(description = "Event type designation in French (required)", 
            example = "Arrêt d'Urgence", 
            required = true,
            maxLength = 100)
    private String designationFr;

    @Size(max = 100, message = "English designation must not exceed 100 characters")
    @Schema(description = "Event type designation in English", 
            example = "Emergency Shutdown",
            maxLength = 100)
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

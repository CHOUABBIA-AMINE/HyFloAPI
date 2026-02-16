/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationTypeDTO
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-16-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO
 *  @Package    : System / Notification / Type
 *
 **/

package dz.sh.trc.hyflo.system.notification.type.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.system.notification.type.model.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for notification type configuration.
 * Defines notification categories with multilingual support (AR, EN, FR).
 */
@Schema(description = "Data Transfer Object for notification type configuration with multilingual designations")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationTypeDTO extends GenericDTO<NotificationType> {

    @Schema(
        description = "Unique code identifier for the notification type (uppercase with underscores)",
        example = "READING_VALIDATION",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 50,
        pattern = "^[A-Z_]+$"
    )
    @NotBlank(message = "Notification type code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    @Pattern(regexp = "^[A-Z_]+$", message = "Code must be uppercase with underscores")
    private String code;

    @Schema(
        description = "Type designation in Arabic",
        example = "قراءة مقدمة",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Schema(
        description = "Type designation in English",
        example = "Reading Submitted",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @Schema(
        description = "Type designation in French (primary language for SONATRACH)",
        example = "Lecture Soumise",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;

    @Schema(
        description = "Optional description of when this notification type is used",
        example = "Triggered when a Reader user submits operational readings for validation",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 500
    )
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Schema(
        description = "Icon class for UI representation (FontAwesome class)",
        example = "fa-file-check",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 50
    )
    @Size(max = 50, message = "Icon class must not exceed 50 characters")
    private String iconClass;

    @Schema(
        description = "Color code for UI representation (hex format)",
        example = "#4CAF50",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 7,
        pattern = "^#[0-9A-Fa-f]{6}$"
    )
    @Size(max = 7, message = "Color code must not exceed 7 characters")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex color code (#RRGGBB)")
    private String colorCode;

    @Schema(
        description = "Priority level (1=Low, 2=Medium, 3=High, 4=Critical)",
        example = "3",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        minimum = "1",
        maximum = "4"
    )
    @Positive(message = "Priority must be a positive integer")
    private Integer priority;

    @Schema(
        description = "Indicates whether this notification type is active and can be used",
        example = "true",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        defaultValue = "true"
    )
    private Boolean active;

    @Override
    public NotificationType toEntity() {
        NotificationType type = NotificationType.builder()
                .code(this.code)
                .designationAr(this.designationAr)
                .designationEn(this.designationEn)
                .designationFr(this.designationFr)
                .description(this.description)
                .iconClass(this.iconClass)
                .colorCode(this.colorCode)
                .priority(this.priority)
                .active(this.active != null ? this.active : true)
                .build();

        if (getId() != null) {
            type.setId(getId());
        }

        return type;
    }

    @Override
    public void updateEntity(NotificationType entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
        if (this.description != null) entity.setDescription(this.description);
        if (this.iconClass != null) entity.setIconClass(this.iconClass);
        if (this.colorCode != null) entity.setColorCode(this.colorCode);
        if (this.priority != null) entity.setPriority(this.priority);
        if (this.active != null) entity.setActive(this.active);
    }

    public static NotificationTypeDTO fromEntity(NotificationType entity) {
        if (entity == null) return null;

        return NotificationTypeDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .description(entity.getDescription())
                .iconClass(entity.getIconClass())
                .colorCode(entity.getColorCode())
                .priority(entity.getPriority())
                .active(entity.getActive())
                .build();
    }
}

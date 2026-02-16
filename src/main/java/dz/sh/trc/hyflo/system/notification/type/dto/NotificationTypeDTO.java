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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for notification type configuration.
 * Defines notification categories, priorities, and delivery channels.
 */
@Schema(description = "Data Transfer Object for notification type configuration and categorization")
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
        description = "Human-readable name of the notification type",
        example = "Reading Validation Request",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Notification type name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Schema(
        description = "Detailed description of when this notification type is triggered",
        example = "Sent to validators when a new pipeline reading is submitted and awaits validation",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 500
    )
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Schema(
        description = "Priority level for notification delivery and display",
        example = "HIGH",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        defaultValue = "NORMAL",
        allowableValues = {"LOW", "NORMAL", "HIGH", "URGENT"}
    )
    @Pattern(regexp = "^(LOW|NORMAL|HIGH|URGENT)$", message = "Priority must be LOW, NORMAL, HIGH, or URGENT")
    private String priority;

    @Schema(
        description = "Icon identifier for UI display (Font Awesome class or custom icon name)",
        example = "fa-check-circle",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 50
    )
    @Size(max = 50, message = "Icon must not exceed 50 characters")
    private String icon;

    @Schema(
        description = "Color code for notification display (hex format)",
        example = "#28a745",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        pattern = "^#[0-9A-Fa-f]{6}$"
    )
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex color code (#RRGGBB)")
    private String color;

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
                .name(this.name)
                .description(this.description)
                .priority(this.priority)
                .icon(this.icon)
                .color(this.color)
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
        if (this.name != null) entity.setName(this.name);
        if (this.description != null) entity.setDescription(this.description);
        if (this.priority != null) entity.setPriority(this.priority);
        if (this.icon != null) entity.setIcon(this.icon);
        if (this.color != null) entity.setColor(this.color);
        if (this.active != null) entity.setActive(this.active);
    }

    public static NotificationTypeDTO fromEntity(NotificationType entity) {
        if (entity == null) return null;

        return NotificationTypeDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .priority(entity.getPriority())
                .icon(entity.getIcon())
                .color(entity.getColor())
                .active(entity.getActive())
                .build();
    }
}

/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationDTO
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-16-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO
 *  @Package    : System / Notification
 *
 **/

package dz.sh.trc.hyflo.system.notification.core.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.system.notification.core.model.Notification;
import dz.sh.trc.hyflo.system.notification.type.dto.NotificationTypeDTO;
import dz.sh.trc.hyflo.system.security.dto.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Data Transfer Object for user notifications with comprehensive validation.
 * Supports real-time notification delivery, read status tracking, and entity linkage.
 */
@Schema(description = "Data Transfer Object for user notification management and real-time delivery")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDTO extends GenericDTO<Notification> {

    @Schema(
        description = "Notification title summarizing the event",
        example = "New Reading Awaiting Validation",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 200
    )
    @NotBlank(message = "Notification title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @Schema(
        description = "Detailed notification message body",
        example = "Pipeline P-1234 reading submitted by John Doe requires your validation",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 1000
    )
    @NotBlank(message = "Notification message is required")
    @Size(max = 1000, message = "Message must not exceed 1000 characters")
    private String message;

    @Schema(
        description = "ID of the entity related to this notification (e.g., reading ID, pipeline ID)",
        example = "1234",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 50
    )
    @Size(max = 50, message = "Related entity ID must not exceed 50 characters")
    private String relatedEntityId;

    @Schema(
        description = "Type of the related entity for navigation purposes",
        example = "Reading",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 50,
        allowableValues = {"Reading", "Pipeline", "Station", "Approval", "Alert"}
    )
    @Size(max = 50, message = "Related entity type must not exceed 50 characters")
    private String relatedEntityType;

    @Schema(
        description = "Indicates whether the notification has been read by the recipient",
        example = "false",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        defaultValue = "false"
    )
    private Boolean isRead;

    @Schema(
        description = "Timestamp when the notification was marked as read",
        example = "2026-02-16T19:30:00",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        format = "yyyy-MM-dd'T'HH:mm:ss"
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime readAt;

    @Schema(
        description = "Timestamp when the notification was created (automatically set)",
        example = "2026-02-16T18:00:00",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        format = "yyyy-MM-dd'T'HH:mm:ss",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(
        description = "ID of the notification type defining category and behavior",
        example = "5",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Notification type ID is required")
    @Positive(message = "Notification type ID must be positive")
    private Long typeId;

    @Schema(
        description = "Full notification type details (populated when fetching with type information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private NotificationTypeDTO type;

    @Schema(
        description = "ID of the recipient user",
        example = "100",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Recipient user ID is required")
    @Positive(message = "Recipient user ID must be positive")
    private Long recipientId;

    @Schema(
        description = "Full recipient user details (populated when fetching with user information)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private UserDTO recipient;

    @Override
    public Notification toEntity() {
        Notification notification = Notification.builder()
                .title(this.title)
                .message(this.message)
                .relatedEntityId(this.relatedEntityId)
                .relatedEntityType(this.relatedEntityType)
                .isRead(this.isRead != null ? this.isRead : false)
                .readAt(this.readAt)
                .createdAt(this.createdAt != null ? this.createdAt : LocalDateTime.now())
                .build();

        if (getId() != null) {
            notification.setId(getId());
        }

        return notification;
    }

    @Override
    public void updateEntity(Notification entity) {
        if (this.title != null) entity.setTitle(this.title);
        if (this.message != null) entity.setMessage(this.message);
        if (this.relatedEntityId != null) entity.setRelatedEntityId(this.relatedEntityId);
        if (this.relatedEntityType != null) entity.setRelatedEntityType(this.relatedEntityType);
        if (this.isRead != null) entity.setIsRead(this.isRead);
        if (this.readAt != null) entity.setReadAt(this.readAt);
    }

    public static NotificationDTO fromEntity(Notification entity) {
        return fromEntity(entity, true, true);
    }

    public static NotificationDTO fromEntity(Notification entity, boolean includeType, boolean includeRecipient) {
        if (entity == null) return null;

        return NotificationDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .relatedEntityId(entity.getRelatedEntityId())
                .relatedEntityType(entity.getRelatedEntityType())
                .isRead(entity.getIsRead())
                .readAt(entity.getReadAt())
                .createdAt(entity.getCreatedAt())
                .typeId(entity.getType() != null ? entity.getType().getId() : null)
                .type(includeType && entity.getType() != null ? NotificationTypeDTO.fromEntity(entity.getType()) : null)
                .recipientId(entity.getRecipient() != null ? entity.getRecipient().getId() : null)
                .recipient(includeRecipient && entity.getRecipient() != null ? UserDTO.fromEntity(entity.getRecipient(), false, false) : null)
                .build();
    }
}

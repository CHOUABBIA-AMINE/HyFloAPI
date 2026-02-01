/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationDTO
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO
 *  @Package    : System / Notification
 *
 **/

package dz.sh.trc.hyflo.system.notification.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.system.notification.core.model.Notification;
import dz.sh.trc.hyflo.system.notification.type.dto.NotificationTypeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Notification Data Transfer Object
 */
@Schema(description = "Notification data transfer object")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDTO extends GenericDTO<Notification> {

    @Schema(description = "Notification title")
    @NotBlank(message = "Title is mandatory")
    @Size(max = 200)
    private String title;

    @Schema(description = "Notification message")
    @NotBlank(message = "Message is mandatory")
    @Size(max = 1000)
    private String message;

    @Schema(description = "Notification type")
    @NotNull(message = "Type is mandatory")
    private NotificationTypeDTO type;

    @Schema(description = "Recipient username")
    private String recipientUsername;

    @Schema(description = "Related entity ID")
    @Size(max = 50)
    private String relatedEntityId;

    @Schema(description = "Related entity type")
    @Size(max = 50)
    private String relatedEntityType;

    @Schema(description = "Read status")
    private Boolean isRead;

    @Schema(description = "Creation timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Read timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime readAt;

    @Override
    public Notification toEntity() {
        Notification notification = new Notification();
        notification.setId(getId());
        notification.setTitle(this.title);
        notification.setMessage(this.message);
        notification.setRelatedEntityId(this.relatedEntityId);
        notification.setRelatedEntityType(this.relatedEntityType);
        notification.setIsRead(this.isRead != null ? this.isRead : false);
        notification.setCreatedAt(this.createdAt != null ? this.createdAt : LocalDateTime.now());
        return notification;
    }

    @Override
    public void updateEntity(Notification entity) {
        if (this.title != null) {
            entity.setTitle(this.title);
        }
        if (this.message != null) {
            entity.setMessage(this.message);
        }
        if (this.isRead != null) {
            entity.setIsRead(this.isRead);
            if (this.isRead && entity.getReadAt() == null) {
                entity.setReadAt(LocalDateTime.now());
            }
        }
    }

    public static NotificationDTO fromEntity(Notification notification) {
        if (notification == null) return null;

        return NotificationDTO.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(NotificationTypeDTO.fromEntity(notification.getType()))
                .recipientUsername(notification.getRecipient().getUsername())
                .relatedEntityId(notification.getRelatedEntityId())
                .relatedEntityType(notification.getRelatedEntityType())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .readAt(notification.getReadAt())
                .build();
    }
}

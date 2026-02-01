/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : Notification
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : Model
 *  @Package    : System / Notification
 *
 **/

package dz.sh.trc.hyflo.system.notification.core.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import dz.sh.trc.hyflo.system.notification.type.model.NotificationType;
import dz.sh.trc.hyflo.system.security.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Notification entity for storing user notifications
 */
@Schema(description = "User notification entity")
@Setter
@Getter
@ToString(exclude = {"recipient", "type"})
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Notification")
@Table(
    name = "T_00_05_01",
    indexes = {
        @Index(name = "IDX_T000501_F04", columnList = "F_04"),
        @Index(name = "IDX_T000501_F07", columnList = "F_07"),
        @Index(name = "IDX_T000501_F09", columnList = "F_09")
    }
)
public class Notification extends GenericModel {

    @Schema(description = "Notification title", example = "New Reading Awaiting Validation")
    @NotBlank(message = "Notification title is mandatory")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    @Column(name = "F_01", length = 200, nullable = false)
    private String title;

    @Schema(description = "Notification message body")
    @NotBlank(message = "Notification message is mandatory")
    @Size(max = 1000, message = "Message must not exceed 1000 characters")
    @Column(name = "F_02", length = 1000, nullable = false)
    private String message;

    @Schema(description = "Related entity ID")
    @Column(name = "F_03", length = 50)
    private String relatedEntityId;

    @Schema(description = "Related entity type")
    @Column(name = "F_04", length = 50)
    private String relatedEntityType;

    @Schema(description = "Read status")
    @Builder.Default
    @Column(name = "F_05", nullable = false)
    private Boolean isRead = false;

    @Schema(description = "Read timestamp")
    @Column(name = "F_06")
    private LocalDateTime readAt;

    @Schema(description = "Creation timestamp")
    @Builder.Default
    @Column(name = "F_07", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Schema(description = "Notification type")
    @NotNull(message = "Notification type is mandatory")
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="F_08", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_00_05_01_FK_01"), nullable=false)
    private NotificationType type;

    @Schema(description = "Recipient user")
    @NotNull(message = "Recipient is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="F_09", referencedColumnName = "F_00", foreignKey=@ForeignKey(name="T_00_05_01_FK_02"), nullable=false)
    private User recipient;

    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }
}

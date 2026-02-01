/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationType
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : Model
 *  @Package    : System / Notification
 *
 **/

package dz.sh.trc.hyflo.system.notification.type.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Notification Type Entity
 * 
 * Represents different categories of system notifications.
 * Used as reference data for notification classification.
 * 
 * Examples:
 * - READING_SUBMITTED: When a reader submits operational readings
 * - READING_VALIDATED: When a validator approves readings
 * - READING_REJECTED: When a validator rejects readings
 * - SYSTEM_ALERT: Critical system notifications
 * - WARNING: Warning messages
 * - INFO: General information
 */
@Schema(description = "Notification type classification for system notifications")
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "NotificationType")
@Table(
    name = "T_00_05_02",
    uniqueConstraints = {
        @UniqueConstraint(name = "T_00_05_02_UK_01", columnNames = "F_01")
    }
)
public class NotificationType extends GenericModel {

    @Schema(
        description = "Unique type code (uppercase with underscores)",
        example = "READING_SUBMITTED",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 50
    )
    @NotBlank(message = "Notification type code is mandatory")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    @Column(name = "F_01", length = 50, nullable = false, unique = true)
    private String code;

    @Schema(
        description = "Type designation in Arabic",
        example = "قراءة مقدمة",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    @Column(name = "F_02", length = 100)
    private String designationAr;

    @Schema(
        description = "Type designation in English",
        example = "Reading Submitted",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 100
    )
    @Size(max = 100, message = "English designation must not exceed 100 characters")
    @Column(name = "F_03", length = 100)
    private String designationEn;

    @Schema(
        description = "Type designation in French (primary language for SONATRACH)",
        example = "Lecture Soumise",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "French designation is mandatory")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    @Column(name = "F_04", length = 100, nullable = false)
    private String designationFr;

    @Schema(
        description = "Optional description of when this notification type is used",
        example = "Triggered when a Reader user submits operational readings for validation",
        maxLength = 500
    )
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "F_05", length = 500)
    private String description;

    @Schema(
        description = "Icon class for UI representation (e.g., FontAwesome class)",
        example = "fa-file-check",
        maxLength = 50
    )
    @Size(max = 50, message = "Icon class must not exceed 50 characters")
    @Column(name = "F_06", length = 50)
    private String iconClass;

    @Schema(
        description = "Color code for UI representation (hex format)",
        example = "#4CAF50",
        maxLength = 7
    )
    @Size(max = 7, message = "Color code must not exceed 7 characters")
    @Column(name = "F_07", length = 7)
    private String colorCode;

    @Schema(
        description = "Priority level (1=Low, 2=Medium, 3=High, 4=Critical)",
        example = "3"
    )
    @Column(name = "F_08")
    private Integer priority;

    @Schema(
        description = "Whether this notification type is active",
        example = "true"
    )
    @Builder.Default
    @Column(name = "F_09", nullable = false)
    private Boolean active = true;
}

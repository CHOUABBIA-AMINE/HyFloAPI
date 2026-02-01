/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : NotificationTypeDTO
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-01-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO
 *  @Package    : System / Notification
 *
 **/

package dz.sh.trc.hyflo.system.notification.type.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.system.notification.type.model.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * NotificationType Data Transfer Object
 */
@Schema(description = "Notification type data transfer object")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationTypeDTO extends GenericDTO<NotificationType> {

    @Schema(description = "Type code", example = "READING_SUBMITTED")
    @NotBlank(message = "Type code is mandatory")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    @Schema(description = "Arabic designation", example = "قراءة مقدمة")
    @Size(max = 100)
    private String designationAr;

    @Schema(description = "English designation", example = "Reading Submitted")
    @Size(max = 100)
    private String designationEn;

    @Schema(description = "French designation", example = "Lecture Soumise")
    @NotBlank(message = "French designation is mandatory")
    @Size(max = 100)
    private String designationFr;

    @Schema(description = "Description")
    @Size(max = 500)
    private String description;

    @Schema(description = "Icon class", example = "fa-file-check")
    @Size(max = 50)
    private String iconClass;

    @Schema(description = "Color code", example = "#4CAF50")
    @Size(max = 7)
    private String colorCode;

    @Schema(description = "Priority level", example = "3")
    private Integer priority;

    @Schema(description = "Active status", example = "true")
    private Boolean active;

    @Override
    public NotificationType toEntity() {
        return NotificationType.builder()
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
    }

    @Override
    public void updateEntity(NotificationType entity) {
        if (this.code != null) {
            entity.setCode(this.code);
        }
        if (this.designationAr != null) {
            entity.setDesignationAr(this.designationAr);
        }
        if (this.designationEn != null) {
            entity.setDesignationEn(this.designationEn);
        }
        if (this.designationFr != null) {
            entity.setDesignationFr(this.designationFr);
        }
        if (this.description != null) {
            entity.setDescription(this.description);
        }
        if (this.iconClass != null) {
            entity.setIconClass(this.iconClass);
        }
        if (this.colorCode != null) {
            entity.setColorCode(this.colorCode);
        }
        if (this.priority != null) {
            entity.setPriority(this.priority);
        }
        if (this.active != null) {
            entity.setActive(this.active);
        }
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

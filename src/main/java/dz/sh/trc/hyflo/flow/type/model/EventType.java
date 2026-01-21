/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: EventType
 * 	@CreatedOn	: 01-21-2026
 * 	@UpdatedOn	: 01-21-2026
 *
 * 	@Type		: Class
 * 	@Layer		: Model
 * 	@Package	: Flow / Type
 *
 **/

package dz.sh.trc.hyflo.flow.type.model;

import dz.sh.trc.hyflo.configuration.template.GenericModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity(name = "EventType")
@Table(name = "T_03_01_02")
public class EventType extends GenericModel {
    
    @NotBlank(message = "Event type code is required")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
    @Column(name = "F_01", length = 20, unique = true, nullable = false)
    @Schema(description = "Unique code for operation type", example = "PRODUCED", required = true)
    private String code;
    
    @Schema(description = "Event type designation in Arabic", example = "منتج")
    @Size(max = 100, message = "Arabic designation cannot exceed 100 characters")
    @Column(name = "F_02", length = 100)
    private String designationAr;
    
    @Schema(description = "Event type designation in French", example = "Produit")
    @Size(max = 100, message = "French designation cannot exceed 100 characters")
    @Column(name = "F_03", length = 100)
    private String designationFr;
    
    @Schema(description = "Event type designation in English", example = "Produced")
    @Size(max = 100, message = "English designation cannot exceed 100 characters")
    @Column(name = "F_04", length = 100)
    private String designationEn;
}


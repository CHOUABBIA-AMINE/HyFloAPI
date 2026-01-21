/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: OperationType
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

@Schema(description = "Flow operation type classification (production input, transportation, consumption output)")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity(name = "OperationType")
@Table(name = "T_03_01_01")
public class OperationType extends GenericModel {
    
    @NotBlank(message = "Operation type code is required")
    @Size(min = 2, max = 20, message = "Code must be between 2 and 20 characters")
    @Column(name = "F_01", length = 20, unique = true, nullable = false)
    @Schema(description = "Unique code for operation type", example = "PRODUCED", required = true)
    private String code;
    
    @Schema(description = "Operation type designation in Arabic", example = "منتج")
    @Size(max = 100, message = "Arabic designation cannot exceed 100 characters")
    @Column(name = "F_02", length = 100)
    private String designationAr;
    
    @Schema(description = "Operation type designation in French", example = "Produit")
    @Size(max = 100, message = "French designation cannot exceed 100 characters")
    @Column(name = "F_03", length = 100)
    private String designationFr;
    
    @Schema(description = "Operation type designation in English", example = "Produced")
    @Size(max = 100, message = "English designation cannot exceed 100 characters")
    @Column(name = "F_04", length = 100)
    private String designationEn;
}


/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ParameterDTO
 *	@CreatedOn	: 06-26-2023
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: System / Setting
 *
 **/

package dz.sh.trc.hyflo.system.setting.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.system.setting.model.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Parameter Data Transfer Object - Extends GenericDTO
 * 
 * Fields:
 * - id (F_00) - inherited from GenericDTO
 * - key (F_01) - required
 * - value (F_02) - required
 * - type (F_03) - required
 * - description (F_04) - optional
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParameterDTO extends GenericDTO<Parameter> {

    @NotBlank(message = "Key is required")
    @Size(max = 100, message = "Key must not exceed 100 characters")
    private String key;

    @NotBlank(message = "Value is required")
    @Size(max = 250, message = "Value must not exceed 250 characters")
    private String value;

    @NotBlank(message = "Type is required")
    @Size(max = 20, message = "Type must not exceed 20 characters")
    private String type;

    @Size(max = 250, message = "File type must not exceed 250 characters")
    private String description;

    // ========== GENERIC DTO IMPLEMENTATION ==========

    @Override
    public Parameter toEntity() {
    	Parameter parameter = new Parameter();
        parameter.setId(getId());
        parameter.setKey(this.key);
        parameter.setValue(this.value);
        parameter.setType(this.type);
        parameter.setDescription(this.description);
        return parameter;
    }

    @Override
    public void updateEntity(Parameter parameter) {
        if (this.key != null) {parameter.setKey(this.key);}
        if (this.value != null) {parameter.setValue(this.value);}
        if (this.type != null) {parameter.setType(this.type);}
        if (this.description != null) {parameter.setDescription(this.description);}
    }

    // ========== FACTORY METHOD ==========

    /**
     * Create DTO from entity
     */
    public static ParameterDTO fromEntity(Parameter parameter) {
        if (parameter == null) return null;
        
        return ParameterDTO.builder()
                .id(parameter.getId())
                .key(parameter.getKey())
                .value(parameter.getValue())
                .type(parameter.getType())
                .description(parameter.getDescription())
                .build();
    }
}

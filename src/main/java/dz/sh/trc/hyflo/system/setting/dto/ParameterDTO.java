/**
 *  
 *  @Author     : MEDJERAB Abir
 *
 *  @Name       : ParameterDTO
 *  @CreatedOn  : 02-01-2026
 *  @UpdatedOn  : 02-16-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO
 *  @Package    : System / Setting
 *
 **/

package dz.sh.trc.hyflo.system.setting.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.system.setting.model.Parameter;
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
 * Data Transfer Object for system configuration parameters.
 * Supports key-value configuration storage with type specification.
 */
@Schema(description = "Data Transfer Object for system configuration parameter management")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParameterDTO extends GenericDTO<Parameter> {

    @Schema(
        description = "Unique parameter key identifier",
        example = "flow_reading_validator",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100
    )
    @NotBlank(message = "Parameter key is required")
    @Size(max = 100, message = "Key must not exceed 100 characters")
    private String key;

    @Schema(
        description = "Parameter value",
        example = "FK:3 -> TRC",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 250
    )
    @NotBlank(message = "Parameter value is required")
    @Size(max = 250, message = "Value must not exceed 250 characters")
    private String value;

    @Schema(
        description = "Type of the parameter value for validation and parsing",
        example = "FOREIGN_KEY",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 20,
        allowableValues = {"NUMBER", "STRING", "FOREIGN_KEY", "OTHER"}
    )
    @NotBlank(message = "Parameter type is required")
    @Size(max = 20, message = "Type must not exceed 20 characters")
    @Pattern(
        regexp = "^(NUMBER|STRING|FOREIGN_KEY|OTHER)$",
        message = "Type must be one of: NUMBER, STRING, FOREIGN_KEY, OTHER"
    )
    private String type;

    @Schema(
        description = "Parameter description explaining its purpose and usage",
        example = "The parameter is used to define the default validator for flow readings",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 250
    )
    @Size(max = 250, message = "Description must not exceed 250 characters")
    private String description;

    @Override
    public Parameter toEntity() {
        Parameter parameter = new Parameter();
        parameter.setKey(this.key);
        parameter.setValue(this.value);
        parameter.setType(this.type);
        parameter.setDescription(this.description);

        if (getId() != null) {
            parameter.setId(getId());
        }

        return parameter;
    }

    @Override
    public void updateEntity(Parameter entity) {
        if (this.key != null) entity.setKey(this.key);
        if (this.value != null) entity.setValue(this.value);
        if (this.type != null) entity.setType(this.type);
        if (this.description != null) entity.setDescription(this.description);
    }

    public static ParameterDTO fromEntity(Parameter entity) {
        if (entity == null) return null;

        return ParameterDTO.builder()
                .id(entity.getId())
                .key(entity.getKey())
                .value(entity.getValue())
                .type(entity.getType())
                .description(entity.getDescription())
                .build();
    }
}

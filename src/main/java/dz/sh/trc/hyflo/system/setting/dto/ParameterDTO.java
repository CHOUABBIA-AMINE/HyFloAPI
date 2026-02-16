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
 * Supports key-value configuration storage with metadata and validation.
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
        description = "Unique parameter key identifier (dot-notation hierarchy, lowercase)",
        example = "system.notification.retention.days",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 100,
        pattern = "^[a-z][a-z0-9.]*[a-z0-9]$"
    )
    @NotBlank(message = "Parameter key is required")
    @Size(max = 100, message = "Key must not exceed 100 characters")
    @Pattern(regexp = "^[a-z][a-z0-9.]*[a-z0-9]$", message = "Key must be lowercase with dots (e.g., system.config.value)")
    private String key;

    @Schema(
        description = "Parameter value (can be string, number, boolean, or JSON)",
        example = "30",
        requiredMode = Schema.RequiredMode.REQUIRED,
        maxLength = 1000
    )
    @NotBlank(message = "Parameter value is required")
    @Size(max = 1000, message = "Value must not exceed 1000 characters")
    private String value;

    @Schema(
        description = "Human-readable parameter name",
        example = "Notification Retention Period",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 200
    )
    @Size(max = 200, message = "Name must not exceed 200 characters")
    private String name;

    @Schema(
        description = "Detailed description of parameter purpose and valid values",
        example = "Number of days to retain notifications before automatic deletion (1-365)",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 500
    )
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Schema(
        description = "Parameter data type for validation and UI rendering",
        example = "INTEGER",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        allowableValues = {"STRING", "INTEGER", "DECIMAL", "BOOLEAN", "JSON", "DATE", "EMAIL", "URL"}
    )
    @Pattern(
        regexp = "^(STRING|INTEGER|DECIMAL|BOOLEAN|JSON|DATE|EMAIL|URL)$",
        message = "Type must be one of: STRING, INTEGER, DECIMAL, BOOLEAN, JSON, DATE, EMAIL, URL"
    )
    private String type;

    @Schema(
        description = "Category for parameter grouping and organization",
        example = "NOTIFICATION",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        maxLength = 50
    )
    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;

    @Schema(
        description = "Indicates whether this parameter is editable via UI",
        example = "true",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        defaultValue = "true"
    )
    private Boolean editable;

    @Schema(
        description = "Indicates whether this parameter contains sensitive data (e.g., passwords, API keys)",
        example = "false",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        defaultValue = "false"
    )
    private Boolean sensitive;

    @Override
    public Parameter toEntity() {
        Parameter parameter = Parameter.builder()
                .key(this.key)
                .value(this.value)
                .name(this.name)
                .description(this.description)
                .type(this.type)
                .category(this.category)
                .editable(this.editable != null ? this.editable : true)
                .sensitive(this.sensitive != null ? this.sensitive : false)
                .build();

        if (getId() != null) {
            parameter.setId(getId());
        }

        return parameter;
    }

    @Override
    public void updateEntity(Parameter entity) {
        if (this.key != null) entity.setKey(this.key);
        if (this.value != null) entity.setValue(this.value);
        if (this.name != null) entity.setName(this.name);
        if (this.description != null) entity.setDescription(this.description);
        if (this.type != null) entity.setType(this.type);
        if (this.category != null) entity.setCategory(this.category);
        if (this.editable != null) entity.setEditable(this.editable);
        if (this.sensitive != null) entity.setSensitive(this.sensitive);
    }

    public static ParameterDTO fromEntity(Parameter entity) {
        return fromEntity(entity, false);
    }

    public static ParameterDTO fromEntity(Parameter entity, boolean maskSensitive) {
        if (entity == null) return null;

        String value = entity.getValue();
        if (maskSensitive && Boolean.TRUE.equals(entity.getSensitive())) {
            value = "********";
        }

        return ParameterDTO.builder()
                .id(entity.getId())
                .key(entity.getKey())
                .value(value)
                .name(entity.getName())
                .description(entity.getDescription())
                .type(entity.getType())
                .category(entity.getCategory())
                .editable(entity.getEditable())
                .sensitive(entity.getSensitive())
                .build();
    }
}

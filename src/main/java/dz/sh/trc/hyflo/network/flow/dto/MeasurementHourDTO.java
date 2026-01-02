/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MeasurementHourDTO
 *	@CreatedOn	: 12-11-2025
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Data Transfer Object
 *	@Layer		: Network / DTO
 *	@Package	: Network / DTO
 *
 **/

package dz.sh.trc.hyflo.network.flow.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.network.flow.model.MeasurementHour;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * MeasurementHour Data Transfer Object - Extends GenericDTO
 * 
 * Fields:
 * - id (F_00) - inherited from GenericDTO
 * - code (F_01) - required
 * - designation (F_02) - required
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasurementHourDTO extends GenericDTO<MeasurementHour> {

    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    @Override
    public MeasurementHour toEntity() {
        MeasurementHour type = new MeasurementHour();
        type.setId(getId());
        type.setCode(this.code);
        return type;
    }

    @Override
    public void updateEntity(MeasurementHour type) {
        if (this.code != null) type.setCode(this.code);
    }

    public static MeasurementHourDTO fromEntity(MeasurementHour type) {
        if (type == null) return null;
        
        return MeasurementHourDTO.builder()
                .id(type.getId())
                .code(type.getCode())
                .build();
    }
}

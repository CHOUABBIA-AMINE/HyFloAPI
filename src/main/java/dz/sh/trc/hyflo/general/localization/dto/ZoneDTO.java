/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ZoneDTO
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 01-02-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: General / Localization
 *
 **/

package dz.sh.trc.hyflo.general.localization.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.general.localization.model.Zone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZoneDTO extends GenericDTO<Zone> {
    
    @NotBlank(message = "Code is required")
    @Size(max = 10, message = "Code must not exceed 10 characters")
    private String code;
    
    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;

    @Override
    public Zone toEntity() {
        Zone entity = new Zone();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        return entity;
    }

    @Override
    public void updateEntity(Zone entity) {
        if (this.code != null) { entity.setCode(this.code); }
        if (this.designationAr != null) { entity.setDesignationAr(this.designationAr); }
        if (this.designationEn != null) { entity.setDesignationEn(this.designationEn); }
        if (this.designationFr != null) { entity.setDesignationFr(this.designationFr); }
    }

    public static ZoneDTO fromEntity(Zone entity) {
        if (entity == null) return null;
        
        return ZoneDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .build();
    }
}

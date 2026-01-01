/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: RegionDTO
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: System / Organization
 *
 **/

package dz.sh.trc.hyflo.system.organization.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.system.localization.dto.ZoneDTO;
import dz.sh.trc.hyflo.system.localization.model.Zone;
import dz.sh.trc.hyflo.system.organization.model.Activity;
import dz.sh.trc.hyflo.system.organization.model.Region;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Region Data Transfer Object - Extends GenericDTO
 * 
 * Fields:
 * - id (F_00) - inherited from GenericDTO
 * - name (F_01) - required
 * - zoneId (F_02) - Zone relationship ID, required
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionDTO extends GenericDTO<Region> {

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
    
    @Size(max = 100, message = "Arabic decsription must not exceed 100 characters")
    private String descriptionAr;

    @Size(max = 100, message = "English decsription must not exceed 100 characters")
    private String descriptionEn;

    @NotBlank(message = "French decsription is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String descriptionFr;

    @NotNull(message = "Zone is required")
    private Long zoneId;

    @NotNull(message = "Activity is required")
    private Long activityId;
    
    private ZoneDTO zone;
    
    private ActivityDTO activity;

    @Override
    public Region toEntity() {
        Region entity = new Region();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setDescriptionAr(this.descriptionAr);
        entity.setDescriptionEn(this.descriptionEn);
        entity.setDescriptionFr(this.descriptionFr);

        if (this.zoneId != null) {
            Zone zone = new Zone();
            zone.setId(this.zoneId);
            entity.setZone(zone);
        }

        if (this.activityId != null) {
        	Activity activity = new Activity();
            activity.setId(this.activityId);
            entity.setActivity(activity);
        }
        
        return entity;
    }

    @Override
    public void updateEntity(Region entity) {
    	if (this.code != null) { entity.setCode(this.code); }
        if (this.designationAr != null) { entity.setDesignationAr(this.designationAr); }
        if (this.designationEn != null) { entity.setDesignationEn(this.designationEn); }
        if (this.designationFr != null) { entity.setDesignationFr(this.designationFr); }
        if (this.descriptionAr != null) { entity.setDescriptionAr(this.descriptionAr); }
        if (this.descriptionEn != null) { entity.setDescriptionEn(this.descriptionEn); }
        if (this.descriptionFr != null) { entity.setDescriptionFr(this.descriptionFr); }

        if (this.zoneId != null) {
            Zone zone = new Zone();
            zone.setId(this.zoneId);
            entity.setZone(zone);
        }

        if (this.activityId != null) {
        	Activity activity = new Activity();
            activity.setId(this.activityId);
            entity.setActivity(activity);
        }
    }

    public static RegionDTO fromEntity(Region entity) {
        if (entity == null) return null;
        
        return RegionDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .descriptionAr(entity.getDescriptionAr())
                .descriptionEn(entity.getDescriptionEn())
                .descriptionFr(entity.getDescriptionFr())
                .zoneId(entity.getZone() != null ? entity.getZone().getId() : null)
                .activityId(entity.getActivity() != null ? entity.getActivity().getId() : null)
                
                .zone(entity.getZone() != null ? ZoneDTO.fromEntity(entity.getZone()) : null)
                .activity(entity.getActivity() != null ? ActivityDTO.fromEntity(entity.getActivity()) : null)
                .build();
    }
}

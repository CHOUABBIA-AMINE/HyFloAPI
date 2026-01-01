/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: MilitaryRankDTO
 *	@CreatedOn	: 10-14-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Common / Administration
 *
 **/

package dz.mdn.iaas.common.administration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.mdn.iaas.common.administration.model.MilitaryRank;
import dz.mdn.iaas.configuration.template.GenericDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * MilitaryRank Data Transfer Object
 * Extends GenericDTO for automatic entity conversion
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MilitaryRankDTO extends GenericDTO<MilitaryRank> {

    @Size(max = 100, message = "Arabic designation must not exceed 100 characters")
    private String designationAr;

    @Size(max = 100, message = "English designation must not exceed 100 characters")
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 100, message = "French designation must not exceed 100 characters")
    private String designationFr;

    @Size(max = 100, message = "Arabic acronym must not exceed 100 characters")
    private String acronymAr;

    @Size(max = 100, message = "English acronym must not exceed 100 characters")
    private String acronymEn;

    @NotBlank(message = "French acronym is required")
    @Size(max = 100, message = "French acronym must not exceed 100 characters")
    private String acronymFr;

    @NotNull(message = "Military Category ID is required")
    private Long militaryCategoryId;

    @Override
    public MilitaryRank toEntity() {
        MilitaryRank entity = new MilitaryRank();
        entity.setId(this.getId());
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setAcronymAr(this.acronymAr);
        entity.setAcronymEn(this.acronymEn);
        entity.setAcronymFr(this.acronymFr);
        return entity;
    }

    @Override
    public void updateEntity(MilitaryRank entity) {
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
        if (this.acronymAr != null) entity.setAcronymAr(this.acronymAr);
        if (this.acronymEn != null) entity.setAcronymEn(this.acronymEn);
        if (this.acronymFr != null) entity.setAcronymFr(this.acronymFr);
    }

    public static MilitaryRankDTO fromEntity(MilitaryRank entity) {
        if (entity == null) return null;
        return MilitaryRankDTO.builder()
                .id(entity.getId())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .acronymAr(entity.getAcronymAr())
                .acronymEn(entity.getAcronymEn())
                .acronymFr(entity.getAcronymFr())
                .militaryCategoryId(entity.getMilitaryCategory() != null ? entity.getMilitaryCategory().getId() : null)
                .build();
    }
}
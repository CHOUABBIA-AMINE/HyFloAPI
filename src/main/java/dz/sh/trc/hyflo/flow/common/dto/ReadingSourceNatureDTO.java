/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : ReadingSourceNatureDTO
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Class
 *  @Layer      : DTO
 *  @Package    : Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dz.sh.trc.hyflo.flow.common.model.ReadingSourceNature;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Reading source nature reference DTO")
public class ReadingSourceNatureDTO {

    @Schema(description = "Entity ID", example = "1")
    private Long id;

    @NotBlank(message = "Code is required")
    @Size(max = 50)
    @Schema(description = "Unique code", example = "DIRECT", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    @Size(max = 100)
    @Schema(description = "Designation in Arabic", example = "قراءة مباشرة")
    private String designationAr;

    @Size(max = 100)
    @Schema(description = "Designation in English", example = "Direct Reading")
    private String designationEn;

    @NotBlank(message = "French designation is required")
    @Size(max = 100)
    @Schema(description = "Designation in French", example = "Lecture directe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String designationFr;

    @Size(max = 255)
    @Schema(description = "Description in Arabic")
    private String descriptionAr;

    @Size(max = 255)
    @Schema(description = "Description in English")
    private String descriptionEn;

    @Size(max = 255)
    @Schema(description = "Description in French")
    private String descriptionFr;

    @Schema(description = "Whether this nature is active", example = "true")
    private Boolean active;

    public static ReadingSourceNatureDTO fromEntity(ReadingSourceNature entity) {
        if (entity == null) return null;
        return ReadingSourceNatureDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .descriptionAr(entity.getDescriptionAr())
                .descriptionEn(entity.getDescriptionEn())
                .descriptionFr(entity.getDescriptionFr())
                .active(entity.getActive())
                .build();
    }

    public ReadingSourceNature toEntity() {
        ReadingSourceNature entity = new ReadingSourceNature();
        entity.setCode(this.code);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setDescriptionAr(this.descriptionAr);
        entity.setDescriptionEn(this.descriptionEn);
        entity.setDescriptionFr(this.descriptionFr);
        entity.setActive(this.active != null ? this.active : Boolean.TRUE);
        return entity;
    }

    public void updateEntity(ReadingSourceNature entity) {
        entity.setCode(this.code);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setDescriptionAr(this.descriptionAr);
        entity.setDescriptionEn(this.descriptionEn);
        entity.setDescriptionFr(this.descriptionFr);
        if (this.active != null) {
            entity.setActive(this.active);
        }
    }
}

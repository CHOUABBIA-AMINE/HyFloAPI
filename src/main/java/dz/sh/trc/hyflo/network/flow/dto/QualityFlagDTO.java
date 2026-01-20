/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: QualityFlagDTO
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.dto;

import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.network.flow.model.QualityFlag;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QualityFlagDTO extends GenericDTO<QualityFlag> {
    
    @NotBlank(message = "Code is required")
    private String code;
    
    private String designationAr;
    private String designationEn;
    
    @NotBlank(message = "French designation is required")
    private String designationFr;
    
    private String descriptionAr;
    private String descriptionEn;
    private String descriptionFr;
    private String severityLevel;
    
    @Override
    public QualityFlag toEntity() {
        QualityFlag entity = new QualityFlag();
        entity.setId(getId());
        entity.setCode(this.code);
        entity.setDesignationAr(this.designationAr);
        entity.setDesignationEn(this.designationEn);
        entity.setDesignationFr(this.designationFr);
        entity.setDescriptionAr(this.descriptionAr);
        entity.setDescriptionEn(this.descriptionEn);
        entity.setDescriptionFr(this.descriptionFr);
        entity.setSeverityLevel(this.severityLevel);
        return entity;
    }
    
    @Override
    public void updateEntity(QualityFlag entity) {
        if (this.code != null) entity.setCode(this.code);
        if (this.designationAr != null) entity.setDesignationAr(this.designationAr);
        if (this.designationEn != null) entity.setDesignationEn(this.designationEn);
        if (this.designationFr != null) entity.setDesignationFr(this.designationFr);
        if (this.descriptionAr != null) entity.setDescriptionAr(this.descriptionAr);
        if (this.descriptionEn != null) entity.setDescriptionEn(this.descriptionEn);
        if (this.descriptionFr != null) entity.setDescriptionFr(this.descriptionFr);
        if (this.severityLevel != null) entity.setSeverityLevel(this.severityLevel);
    }
    
    public static QualityFlagDTO fromEntity(QualityFlag entity) {
        if (entity == null) return null;
        return QualityFlagDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .designationAr(entity.getDesignationAr())
                .designationEn(entity.getDesignationEn())
                .designationFr(entity.getDesignationFr())
                .descriptionAr(entity.getDescriptionAr())
                .descriptionEn(entity.getDescriptionEn())
                .descriptionFr(entity.getDescriptionFr())
                .severityLevel(entity.getSeverityLevel())
                .build();
    }
}

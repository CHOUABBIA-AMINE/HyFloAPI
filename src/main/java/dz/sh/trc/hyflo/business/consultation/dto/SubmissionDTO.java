/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: SubmissionDTO
 *	@CreatedOn	: 10-16-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Class
 *	@Layer		: DTO
 *	@Package	: Business / Consultation
 *
 **/

package dz.sh.trc.hyflo.business.consultation.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import dz.sh.trc.hyflo.configuration.template.GenericDTO;
import dz.sh.trc.hyflo.business.consultation.model.Submission;
import jakarta.validation.constraints.NotNull;
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
public class SubmissionDTO extends GenericDTO<Submission> {

    private Date submissionDate;
    
    private double financialOffer;
    
    @NotNull(message = "Consultation is required")
    private Long consultationId;
    
    @NotNull(message = "Tender (Provider) is required")
    private Long tenderId;
    
    private Long administrativePartId;
    
    private Long technicalPartId;
    
    private Long financialPartId;

    @Override
    public Submission toEntity() {
        Submission entity = new Submission();
        entity.setId(getId());
        entity.setSubmissionDate(this.submissionDate);
        entity.setFinancialOffer(this.financialOffer);
        return entity;
    }

    @Override
    public void updateEntity(Submission entity) {
        if (this.submissionDate != null) entity.setSubmissionDate(this.submissionDate);
        entity.setFinancialOffer(this.financialOffer);
    }

    public static SubmissionDTO fromEntity(Submission entity) {
        if (entity == null) return null;
        return SubmissionDTO.builder()
                .id(entity.getId())
                .submissionDate(entity.getSubmissionDate())
                .financialOffer(entity.getFinancialOffer())
                .consultationId(entity.getConsultation() != null ? entity.getConsultation().getId() : null)
                .tenderId(entity.getTender() != null ? entity.getTender().getId() : null)
                .administrativePartId(entity.getAdministrativePart() != null ? entity.getAdministrativePart().getId() : null)
                .technicalPartId(entity.getTechnicalPart() != null ? entity.getTechnicalPart().getId() : null)
                .financialPartId(entity.getFinancialPart() != null ? entity.getFinancialPart().getId() : null)
                .build();
    }
}

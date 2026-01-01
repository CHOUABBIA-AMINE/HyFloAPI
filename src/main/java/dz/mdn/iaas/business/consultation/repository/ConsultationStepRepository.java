/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ConsultationStepRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-13-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Consultation
 *	@Package	: Business / Consultation / Repository
 *
 **/

package dz.mdn.iaas.business.consultation.repository;

import dz.mdn.iaas.business.consultation.model.ConsultationStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ConsultationStep Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface ConsultationStepRepository extends JpaRepository<ConsultationStep, Long> {
    
    /**
     * Find all consultation steps by phase ID
     * @param phaseId the consultation phase ID
     * @return list of consultation steps
     */
    List<ConsultationStep> findByConsultationPhaseId(Long phaseId);
}

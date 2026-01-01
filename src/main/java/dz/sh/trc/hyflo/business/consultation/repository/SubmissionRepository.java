/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: SubmissionRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-13-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Consultation
 *	@Package	: Business / Consultation / Repository
 *
 **/

package dz.sh.trc.hyflo.business.consultation.repository;

import dz.sh.trc.hyflo.business.consultation.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Submission Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    
    /**
     * Find all submissions by consultation ID
     * @param consultationId the consultation ID
     * @return list of submissions
     */
    List<Submission> findByConsultationId(Long consultationId);
}

/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ConsultationPhaseRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-13-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Consultation
 *	@Package	: Business / Consultation / Repository
 *
 **/

package dz.sh.trc.hyflo.business.consultation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.business.consultation.model.ConsultationPhase;

/**
 * ConsultationPhase Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface ConsultationPhaseRepository extends JpaRepository<ConsultationPhase, Long> {

}

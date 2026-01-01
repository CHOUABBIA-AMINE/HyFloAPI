/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: AmendmentStepRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Amendment
 *	@Package	: Business / Amendment / Repository
 *
 **/

package dz.sh.trc.hyflo.business.amendment.repository;

import dz.sh.trc.hyflo.business.amendment.model.AmendmentStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AmendmentStep Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface AmendmentStepRepository extends JpaRepository<AmendmentStep, Long> {
    
    /**
     * Find all amendment steps by phase ID
     * @param phaseId the amendment phase ID
     * @return list of amendment steps
     */
    List<AmendmentStep> findByAmendmentPhaseId(Long phaseId);
}

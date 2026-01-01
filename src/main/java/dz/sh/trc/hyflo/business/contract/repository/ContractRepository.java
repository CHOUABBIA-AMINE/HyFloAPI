/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ContractRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Contract
 *	@Package	: Business / Contract / Repository
 *
 **/

package dz.sh.trc.hyflo.business.contract.repository;

import dz.sh.trc.hyflo.business.contract.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Contract Repository
 * Contract belongs to Consultation
 */
@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Find contracts by consultation ID
     * Used by ContractService.getByConsultationId()
     */
    List<Contract> findByConsultationId(Long consultationId);
}

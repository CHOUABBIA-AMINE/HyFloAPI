/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ContractStepRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Contract
 *	@Package	: Business / Contract / Repository
 *
 **/

package dz.sh.trc.hyflo.business.contract.repository;

import dz.sh.trc.hyflo.business.contract.model.ContractStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ContractStep Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface ContractStepRepository extends JpaRepository<ContractStep, Long> {
    
    /**
     * Find all contract steps by phase ID
     * @param phaseId the contract phase ID
     * @return list of contract steps
     */
    List<ContractStep> findByContractPhaseId(Long phaseId);
}

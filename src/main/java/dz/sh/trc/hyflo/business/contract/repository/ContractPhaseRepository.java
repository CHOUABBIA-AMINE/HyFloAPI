/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ContractPhaseRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Contract
 *	@Package	: Business / Contract / Repository
 *
 **/

package dz.sh.trc.hyflo.business.contract.repository;

import dz.sh.trc.hyflo.business.contract.model.ContractPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ContractPhase Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface ContractPhaseRepository extends JpaRepository<ContractPhase, Long> {
    // All basic CRUD operations inherited from JpaRepository
}

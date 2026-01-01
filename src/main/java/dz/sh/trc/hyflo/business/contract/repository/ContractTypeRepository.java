/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ContractTypeRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-11-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Contract
 *	@Package	: Business / Contract / Repository
 *
 **/

package dz.sh.trc.hyflo.business.contract.repository;

import dz.sh.trc.hyflo.business.contract.model.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ContractType Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface ContractTypeRepository extends JpaRepository<ContractType, Long> {
    // All basic CRUD operations inherited from JpaRepository
}

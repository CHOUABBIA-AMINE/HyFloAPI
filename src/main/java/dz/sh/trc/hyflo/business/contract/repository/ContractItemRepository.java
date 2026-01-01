/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: ContractItemRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-12-2025
 *
 *	@Type		: Repository
 *	@Layer		: Business / Contract
 *	@Package	: Business / Contract / Repository
 *
 **/

package dz.sh.trc.hyflo.business.contract.repository;

import dz.sh.trc.hyflo.business.contract.model.ContractItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ContractItem Repository
 * Basic CRUD operations provided by JpaRepository
 */
@Repository
public interface ContractItemRepository extends JpaRepository<ContractItem, Long> {
    
    /**
     * Find all contract items by contract ID
     * @param contractId the contract ID
     * @return list of contract items
     */
    List<ContractItem> findByContractId(Long contractId);
}

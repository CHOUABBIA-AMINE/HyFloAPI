/**
 *	
 *	@Author		: CHOUABBIA-AMINE
 *
 *	@Name		: ValidationStatusRepository
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: Network / Flow
 *
 **/

package dz.sh.trc.hyflo.network.flow.repository;

import dz.sh.trc.hyflo.network.flow.model.ValidationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ValidationStatusRepository extends JpaRepository<ValidationStatus, Long>, 
                                                     JpaSpecificationExecutor<ValidationStatus> {
    Optional<ValidationStatus> findByCode(String code);
    boolean existsByCode(String code);
}

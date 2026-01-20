/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ValidationStatusRepository
 *	@CreatedOn	: 01-20-2026
 *	@UpdatedOn	: 01-20-2026
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;

@Repository
public interface ValidationStatusRepository extends JpaRepository<ValidationStatus, Long>, 
                                                     JpaSpecificationExecutor<ValidationStatus> {
    Optional<ValidationStatus> findByCode(String code);
    boolean existsByCode(String code);
}

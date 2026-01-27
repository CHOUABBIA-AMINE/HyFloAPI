/**
 *
 * 	@Author		: CHOUABBIA Amine
 *
 * 	@Name		: ValidationStatusRepository
 * 	@CreatedOn	: 01-27-2026
 * 	@UpdatedOn	: 01-27-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.common.model.ValidationStatus;

@Repository
public interface ValidationStatusRepository extends JpaRepository<ValidationStatus, Long> {
    
    /**
     * Find validation status by its unique code
     * 
     * @param code The status code (e.g., "PENDING", "VALIDATED", "REJECTED")
     * @return Optional containing the ValidationStatus if found
     */
    Optional<ValidationStatus> findByCode(String code);
    
    /**
     * Check if a validation status with the given code exists
     * 
     * @param code The status code
     * @return true if exists, false otherwise
     */
    boolean existsByCode(String code);
}

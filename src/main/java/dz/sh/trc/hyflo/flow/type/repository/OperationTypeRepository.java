/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: OperationTypeRepository
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Type
 *
 **/

package dz.sh.trc.hyflo.flow.type.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.type.model.OperationType;

/**
 * Repository interface for OperationType entity.
 * Provides CRUD operations and custom queries for flow operation type classification.
 */
@Repository
public interface OperationTypeRepository extends JpaRepository<OperationType, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Check if an operation type with the given code exists.
     *
     * @param code the operation type code
     * @return true if exists, false otherwise
     */
    boolean existsByCode(String code);
    
    /**
     * Check if an operation type with the given code exists, excluding a specific ID.
     * Useful for update operations to avoid duplicate code validation.
     *
     * @param code the operation type code
     * @param id the ID to exclude from the check
     * @return true if exists, false otherwise
     */
    boolean existsByCodeAndIdNot(String code, Long id);
    
    /**
     * Find operation type by code.
     *
     * @param code the operation type code (PRODUCED, TRANSPORTED, CONSUMED)
     * @return Optional containing the operation type if found
     */
    Optional<OperationType> findByCode(String code);
    
    /**
     * Find all operation types ordered by code.
     *
     * @return list of operation types
     */
    List<OperationType> findAllByOrderByCodeAsc();

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    /**
     * Search operation types by any field (code or names in any language).
     *
     * @param search the search term
     * @param pageable pagination parameters
     * @return page of matching operation types
     */
    @Query("SELECT ot FROM OperationType ot WHERE " +
           "LOWER(ot.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(ot.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(ot.designationFr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(ot.designationEn) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<OperationType> searchByAnyField(@Param("search") String search, Pageable pageable);
}

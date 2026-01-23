/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: DataSourceRepository
 * 	@CreatedOn	: 01-23-2026
 * 	@UpdatedOn	: 01-23-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 * 	@Package	: Flow / Common
 *
 **/

package dz.sh.trc.hyflo.flow.common.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.common.model.DataSource;

/**
 * Repository interface for DataSource entity.
 * Provides CRUD operations and custom queries for data source classification.
 */
@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {

    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Check if a data source with the given code exists.
     *
     * @param code the data source code
     * @return true if exists, false otherwise
     */
    boolean existsByCode(String code);
    
    /**
     * Check if a data source with the given code exists, excluding a specific ID.
     *
     * @param code the data source code
     * @param id the ID to exclude from the check
     * @return true if exists, false otherwise
     */
    boolean existsByCodeAndIdNot(String code, Long id);
    
    /**
     * Check if a data source with the given French designation exists.
     *
     * @param designationFr the French designation
     * @return true if exists, false otherwise
     */
    boolean existsByDesignationFr(String designationFr);
    
    /**
     * Check if a data source with the given French designation exists, excluding a specific ID.
     *
     * @param designationFr the French designation
     * @param id the ID to exclude from the check
     * @return true if exists, false otherwise
     */
    boolean existsByDesignationFrAndIdNot(String designationFr, Long id);
    
    /**
     * Find data source by code.
     *
     * @param code the data source code
     * @return Optional containing the data source if found
     */
    Optional<DataSource> findByCode(String code);
    
    /**
     * Find data source by French designation.
     *
     * @param designationFr the French designation
     * @return Optional containing the data source if found
     */
    Optional<DataSource> findByDesignationFr(String designationFr);
    
    /**
     * Find all data sources ordered by code.
     *
     * @return list of data sources
     */
    List<DataSource> findAllByOrderByCodeAsc();

    // ========== CUSTOM QUERIES (Complex multi-field search) ==========
    
    /**
     * Search data sources by any field (code, designations, descriptions).
     *
     * @param search the search term
     * @param pageable pagination parameters
     * @return page of matching data sources
     */
    @Query("SELECT ds FROM DataSource ds WHERE " +
           "LOWER(ds.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(ds.designationAr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(ds.designationEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(ds.designationFr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(ds.descriptionAr) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(ds.descriptionEn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(ds.descriptionFr) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<DataSource> searchByAnyField(@Param("search") String search, Pageable pageable);
}

/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: ParameterRepository
 *	@CreatedOn	: 06-26-2023
 *	@UpdatedOn	: 12-11-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Setting
 *
 **/

package dz.sh.trc.hyflo.system.setting.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.system.setting.model.Parameter;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    /**
     * Check if file exists by path
     */
    boolean existsKey(String key);

    /**
     * Find file by path
     */
    Optional<Parameter> findByKey(String key);

    /**
     * Find files by file type
     */
    Page<Parameter> findByType(String type, Pageable pageable);

    /**
     * Global search across all fields
     */
    @Query("SELECT f FROM Parameter f WHERE " +
           "LOWER(f.key) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(f.value) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(f.type) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Parameter> searchParameters(@Param("query") String query, Pageable pageable);

    /**
     * Count files by extension
     */
    @Query("SELECT COUNT(p) FROM Parameter p WHERE p.type = :type")
    long countByType(@Param("type") String type);

}

/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: EmployeeRepository
 *	@CreatedOn	: 06-26-2025
 *	@Updated	: 12-19-2025
 *
 *	@Type		: Repository
 *	@Layer		: Common / Administration
 *	@Package	: Common / Administration / Repository
 *
 **/

package dz.mdn.iaas.common.administration.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.mdn.iaas.common.administration.model.Employee;

/**
 * Employee Repository
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // ========== SPRING DERIVED QUERIES (Optimized) ==========
    
    /**
     * Find employees by structure ID
     * Used by EmployeeService.getByStructureId()
     */
    List<Employee> findByStructureId(Long structureId);
    
    List<Employee> findByJobId(Long jobId);
    
    List<Employee> findByCountryId(Long countryId);
    
    List<Employee> findByMilitaryRankId(Long militaryRankId);
    
    @Query("SELECT e FROM Employee e WHERE "
            + "LOWER(e.registrationNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(e.lastNameAr) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(e.firstNameAr) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(e.lastNameLt) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(e.firstNameLt) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Employee> searchByAnyField(@Param("search") String search, Pageable pageable);
}

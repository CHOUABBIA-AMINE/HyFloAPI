/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AuthorityRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-12-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.system.security.model.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    
    /**
     * Find authority by name
     */
    Optional<Authority> findByName(String name);
    
    /**
     * Find authorities by type
     */
    List<Authority> findByType(String type);
    
    /**
     * Check if authority exists by name
     */
    boolean existsByName(String name);
    
    @Query("SELECT a FROM Authority a WHERE "
    		+ "LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR "
    		+ "LOWER(a.type) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Authority> searchByAnyField(@Param("search") String search, Pageable pageable);
}

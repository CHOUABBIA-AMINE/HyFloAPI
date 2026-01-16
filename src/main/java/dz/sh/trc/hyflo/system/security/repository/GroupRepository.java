/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: GroupRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 12-12-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Security
 *
 **/

package dz.sh.trc.hyflo.system.security.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.system.security.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    
    /**
     * Find group by name
     */
    Optional<Group> findByName(String name);
    
    /**
     * Check if group exists by name
     */
    boolean existsByName(String name);
    
    @Query("SELECT g FROM Group g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Group> searchByAnyField(@Param("search") String search, Pageable pageable);
}

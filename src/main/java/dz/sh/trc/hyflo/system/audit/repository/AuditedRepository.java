/**
 *	
 *	@Author		: MEDJERAB Abir
 *
 *	@Name		: AuditedRepository
 *	@CreatedOn	: 06-26-2025
 *	@UpdatedOn	: 11-18-2025
 *
 *	@Type		: Interface
 *	@Layer		: Repository
 *	@Package	: System / Audit
 *
 **/

package dz.sh.trc.hyflo.system.audit.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.system.audit.model.Audited;

@Repository
public interface AuditedRepository extends JpaRepository<Audited, Long> {

    // Spring Data derived queries (no custom implementation needed)
    List<Audited> findByEntityNameAndEntityId(String entityName, Long entityId);
    
    Page<Audited> findByUsername(String username, Pageable pageable);
    
    Page<Audited> findByAction(String action, Pageable pageable);
    
    Page<Audited> findByModule(String module, Pageable pageable);
    
    Page<Audited> findByStatus(String status, Pageable pageable);

    // Only essential custom queries that cannot be derived
    @Query("SELECT a FROM Audited a WHERE a.timestamp BETWEEN :startDate AND :endDate")
    Page<Audited> findByDateRange(@Param("startDate") Date startDate, 
                                   @Param("endDate") Date endDate, 
                                   Pageable pageable);

    @Query("SELECT COUNT(a) FROM Audited a WHERE a.username = :username AND a.timestamp >= :since")
    long countByUsernameAndTimestampAfter(@Param("username") String username, @Param("since") Date since);

    @Query("SELECT a.action, COUNT(a) FROM Audited a WHERE a.username = :username " +
           "AND a.timestamp >= :since GROUP BY a.action")
    List<Object[]> getUserActivitySummary(@Param("username") String username, @Param("since") Date since);
}

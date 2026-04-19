/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 * 	@Name		: IncidentRepository
 * 	@CreatedOn	: 03-25-2026
 * 	@UpdatedOn	: 03-26-2026
 *
 * 	@Type		: Interface
 * 	@Layer		: Repository
 *  @Package    : Crisis / Emergency
 *
 **/

package dz.sh.trc.hyflo.core.crisis.emergency.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.core.crisis.emergency.model.Incident;

/**
 * Repository for Incident entities.
 */
@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    Optional<Incident> findByCode(String code);

    List<Incident> findByActive(Boolean active);

    List<Incident> findByPipelineSegmentId(Long pipelineSegmentId);

    List<Incident> findBySeverityId(Long severityId);

    @Query("SELECT i FROM Incident i WHERE "
            + "LOWER(i.code) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(i.title) LIKE LOWER(CONCAT('%', :search, '%')) OR "
            + "LOWER(i.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Incident> searchByAnyField(@Param("search") String search, Pageable pageable);
}

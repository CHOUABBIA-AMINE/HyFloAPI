/**
 *
 * 	@Author		: MEDJERAB Abir
 *
 *  @Name       : IncidentImpactRepository
 *  @CreatedOn  : 03-26-2026
 * 	@UpdatedOn	: 03-26-2026
 *
 *  @Type       : Interface
 *  @Layer      : Repository
 *  @Package    : Crisis / Core
 *
 **/

package dz.sh.trc.hyflo.crisis.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.crisis.core.model.IncidentImpact;

@Repository
public interface IncidentImpactRepository extends JpaRepository<IncidentImpact, Long> {

    List<IncidentImpact> findByIncidentId(Long incidentId);

    Page<IncidentImpact> findByIncidentId(Long incidentId, Pageable pageable);
}

package dz.sh.trc.hyflo.core.crisis.emergency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dz.sh.trc.hyflo.core.crisis.emergency.model.IncidentImpact;

@Repository
public interface IncidentImpactRepository extends JpaRepository<IncidentImpact, Long> {
}

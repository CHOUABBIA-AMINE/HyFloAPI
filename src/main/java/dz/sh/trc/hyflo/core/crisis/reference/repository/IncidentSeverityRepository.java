package dz.sh.trc.hyflo.core.crisis.reference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dz.sh.trc.hyflo.core.crisis.reference.model.IncidentSeverity;

@Repository
public interface IncidentSeverityRepository extends JpaRepository<IncidentSeverity, Long> {
}

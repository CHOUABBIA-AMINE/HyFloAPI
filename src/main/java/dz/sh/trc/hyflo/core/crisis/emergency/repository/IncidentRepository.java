package dz.sh.trc.hyflo.core.crisis.emergency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dz.sh.trc.hyflo.core.crisis.emergency.model.Incident;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
}

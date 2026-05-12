package dz.sh.trc.hyflo.core.flow.monitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowIncident;

@Repository
public interface FlowIncidentRepository extends JpaRepository<FlowIncident, Long> {
}

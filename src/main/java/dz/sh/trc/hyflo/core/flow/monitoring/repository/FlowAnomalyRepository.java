package dz.sh.trc.hyflo.core.flow.monitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dz.sh.trc.hyflo.core.flow.monitoring.model.FlowAnomaly;

@Repository
public interface FlowAnomalyRepository extends JpaRepository<FlowAnomaly, Long> {
}

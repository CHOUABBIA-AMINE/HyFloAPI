package dz.sh.trc.hyflo.core.flow.planning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dz.sh.trc.hyflo.core.flow.planning.model.FlowForecast;

@Repository
public interface FlowForecastRepository extends JpaRepository<FlowForecast, Long> {
}

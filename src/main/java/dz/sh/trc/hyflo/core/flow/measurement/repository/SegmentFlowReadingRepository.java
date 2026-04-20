package dz.sh.trc.hyflo.core.flow.measurement.repository;
import org.springframework.stereotype.Repository;
import dz.sh.trc.hyflo.core.flow.measurement.model.SegmentFlowReading;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface SegmentFlowReadingRepository extends JpaRepository<SegmentFlowReading, Long> {}
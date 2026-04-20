package dz.sh.trc.hyflo.core.flow.measurement.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import dz.sh.trc.hyflo.core.flow.measurement.model.FlowReading;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface FlowReadingRepository extends JpaRepository<FlowReading, Long> {
    List<FlowReading> findByPipelineIdAndReadingDateBetween(Long pipelineId, LocalDate start, LocalDate end);
    Page<FlowReading> findByPipelineIdAndReadingDateBetween(Long pipelineId, LocalDate start, LocalDate end, Pageable pageable);
    List<FlowReading> findByValidationStatusId(Long statusId);
    Page<FlowReading> findByValidationStatusId(Long statusId, Pageable pageable);
    Page<FlowReading> findByPipelineId(Long pipelineId, Pageable pageable);
    Page<FlowReading> findByReadingDateBetween(LocalDate start, LocalDate end, Pageable pageable);
}
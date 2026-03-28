/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowPlanRepository
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Repository
 *  @Package    : Flow / Core
 *
 **/

package dz.sh.trc.hyflo.flow.core.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dz.sh.trc.hyflo.flow.core.model.FlowPlan;

@Repository
public interface FlowPlanRepository extends JpaRepository<FlowPlan, Long> {

    List<FlowPlan> findByPipelineId(Long pipelineId);

    Page<FlowPlan> findByPipelineId(Long pipelineId, Pageable pageable);

    List<FlowPlan> findByPipelineIdAndPlanDate(Long pipelineId, LocalDate planDate);

    List<FlowPlan> findByPipelineIdAndPlanDateBetween(
            Long pipelineId, LocalDate from, LocalDate to);

    @Query("SELECT fp FROM FlowPlan fp WHERE fp.pipeline.id = :pipelineId " +
           "AND fp.status.code = :statusCode ORDER BY fp.planDate DESC")
    List<FlowPlan> findByPipelineIdAndStatusCode(
            @Param("pipelineId") Long pipelineId,
            @Param("statusCode") String statusCode);

    @Query("SELECT fp FROM FlowPlan fp WHERE fp.pipeline.id = :pipelineId " +
           "AND fp.status.code = 'APPROVED' " +
           "AND fp.planDate = :planDate " +
           "ORDER BY fp.approvedAt DESC")
    Optional<FlowPlan> findLatestApprovedByPipelineAndDate(
            @Param("pipelineId") Long pipelineId,
            @Param("planDate") LocalDate planDate);

    @Query("SELECT fp FROM FlowPlan fp WHERE fp.scenarioCode = :scenarioCode " +
           "ORDER BY fp.planDate DESC")
    Page<FlowPlan> findByScenarioCode(
            @Param("scenarioCode") String scenarioCode, Pageable pageable);

    @Query("SELECT fp FROM FlowPlan fp WHERE fp.pipeline.id = :pipelineId " +
           "AND fp.status.code = 'APPROVED' ORDER BY fp.planDate DESC")
    Page<FlowPlan> findApprovedByPipeline(
            @Param("pipelineId") Long pipelineId, Pageable pageable);
}

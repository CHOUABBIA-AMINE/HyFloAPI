/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowForecastService
 *  @CreatedOn  : 03-28-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.intelligence.service;

import dz.sh.trc.hyflo.intelligence.dto.FlowForecastReadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FlowForecastService {

    Page<FlowForecastReadDTO> findAll(Pageable pageable);

    Optional<FlowForecastReadDTO> findById(Long id);

    List<FlowForecastReadDTO> findByPipeline(Long pipelineId);

    Page<FlowForecastReadDTO> globalSearch(String query, Pageable pageable);
}

/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : FlowAnomalyService
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-28-2026 — refactor: converted from @Service class to interface
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Flow / Intelligence
 *
 **/

package dz.sh.trc.hyflo.flow.intelligence.service;

import dz.sh.trc.hyflo.flow.intelligence.dto.FlowAnomalyReadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FlowAnomalyService {

    Page<FlowAnomalyReadDTO> getAll(Pageable pageable);

    Page<FlowAnomalyReadDTO> searchByQuery(String query, Pageable pageable);

    List<FlowAnomalyReadDTO> getByReadingId(Long readingId);

    List<FlowAnomalyReadDTO> getByPipelineSegmentId(Long segmentId);
}

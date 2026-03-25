/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineSegmentQueryService
 *  @CreatedOn  : 03-25-2026
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Network / Core
 *
 *  Phase 3 — Commit 23
 *
 **/

package dz.sh.trc.hyflo.network.core.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.network.core.dto.query.PipelineSegmentReadDto;

/**
 * Query contract for PipelineSegment read operations.
 *
 * Segment infrastructure truth is owned by the network module.
 * All methods return PipelineSegmentReadDto — never raw entities.
 *
 * Phase 3 — Commit 23
 */
public interface PipelineSegmentQueryService {

    PipelineSegmentReadDto getById(Long id);

    Page<PipelineSegmentReadDto> getAll(Pageable pageable);

    List<PipelineSegmentReadDto> getByPipeline(Long pipelineId);

    List<PipelineSegmentReadDto> search(String query);
}

/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineSegmentQueryService
 *  @CreatedOn  : 03-25-2026
 *  @UpdatedOn  : 03-26-2026 — Task 3: search(String) → search(String, Pageable), returns Page
 *
 *  @Type       : Interface
 *  @Layer      : Service
 *  @Package    : Network / Core
 *
 **/

package dz.sh.trc.hyflo.network.core.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dz.sh.trc.hyflo.network.core.dto.query.PipelineSegmentReadDto;

public interface PipelineSegmentQueryService {

    PipelineSegmentReadDto getById(Long id);

    Page<PipelineSegmentReadDto> getAll(Pageable pageable);

    List<PipelineSegmentReadDto> getByPipeline(Long pipelineId);

    Page<PipelineSegmentReadDto> search(String query, Pageable pageable);
}

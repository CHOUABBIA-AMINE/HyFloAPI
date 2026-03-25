/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineSegmentCommandService
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

import dz.sh.trc.hyflo.network.core.dto.command.PipelineSegmentCommandDto;
import dz.sh.trc.hyflo.network.core.dto.query.PipelineSegmentReadDto;

/**
 * Command contract for PipelineSegment write operations.
 *
 * Phase 3 — Commit 23
 */
public interface PipelineSegmentCommandService {

    PipelineSegmentReadDto createSegment(PipelineSegmentCommandDto command);

    PipelineSegmentReadDto updateSegment(Long id, PipelineSegmentCommandDto command);

    void deleteSegment(Long id);
}

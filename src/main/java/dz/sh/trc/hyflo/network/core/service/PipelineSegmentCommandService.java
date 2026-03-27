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

import dz.sh.trc.hyflo.network.core.dto.command.PipelineSegmentCommandDTO;
import dz.sh.trc.hyflo.network.core.dto.query.PipelineSegmentReadDTO;

/**
 * Command contract for PipelineSegment write operations.
 *
 * Phase 3 — Commit 23
 */
public interface PipelineSegmentCommandService {

    PipelineSegmentReadDTO createSegment(PipelineSegmentCommandDTO command);

    PipelineSegmentReadDTO updateSegment(Long id, PipelineSegmentCommandDTO command);

    void deleteSegment(Long id);
}

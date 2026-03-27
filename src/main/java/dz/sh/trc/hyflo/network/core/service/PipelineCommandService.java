/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineCommandService
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

import dz.sh.trc.hyflo.network.core.dto.command.PipelineCommandDTO;
import dz.sh.trc.hyflo.network.core.dto.query.PipelineReadDTO;

/**
 * Command contract for Pipeline write operations.
 *
 * Network module owns the authoritative pipeline infrastructure model.
 * All methods return PipelineReadDTO — never raw entities.
 *
 * Phase 3 — Commit 23
 */
public interface PipelineCommandService {

    PipelineReadDTO createPipeline(PipelineCommandDTO command);

    PipelineReadDTO updatePipeline(Long id, PipelineCommandDTO command);

    void deletePipeline(Long id);
}

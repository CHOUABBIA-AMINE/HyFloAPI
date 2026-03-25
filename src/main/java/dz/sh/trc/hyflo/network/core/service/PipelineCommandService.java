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

import dz.sh.trc.hyflo.network.core.dto.command.PipelineCommandDto;
import dz.sh.trc.hyflo.network.core.dto.query.PipelineReadDto;

/**
 * Command contract for Pipeline write operations.
 *
 * Network module owns the authoritative pipeline infrastructure model.
 * All methods return PipelineReadDto — never raw entities.
 *
 * Phase 3 — Commit 23
 */
public interface PipelineCommandService {

    PipelineReadDto createPipeline(PipelineCommandDto command);

    PipelineReadDto updatePipeline(Long id, PipelineCommandDto command);

    void deletePipeline(Long id);
}

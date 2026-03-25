/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineQueryService
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

import dz.sh.trc.hyflo.network.core.dto.query.PipelineReadDto;

/**
 * Query contract for Pipeline read operations.
 *
 * Network module owns all pipeline infrastructure truth.
 * All methods return PipelineReadDto — never raw entities.
 *
 * Phase 3 — Commit 23
 */
public interface PipelineQueryService {

    PipelineReadDto getById(Long id);

    Page<PipelineReadDto> getAll(Pageable pageable);

    List<PipelineReadDto> getByPipelineSystem(Long systemId);

    List<PipelineReadDto> search(String query);
}

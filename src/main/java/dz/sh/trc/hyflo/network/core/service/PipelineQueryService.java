/**
 *
 *  @Author     : HyFlo v2
 *
 *  @Name       : PipelineQueryService
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

import dz.sh.trc.hyflo.network.core.dto.query.PipelineReadDto;

public interface PipelineQueryService {

    PipelineReadDto getById(Long id);

    Page<PipelineReadDto> getAll(Pageable pageable);

    List<PipelineReadDto> getByPipelineSystem(Long systemId);

    Page<PipelineReadDto> search(String query, Pageable pageable);
}
